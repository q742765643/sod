package com.piesat.schedule.client.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.annotation.HtJson;
import com.piesat.schedule.client.datasource.DynamicDataSource;
import com.piesat.schedule.client.vo.ConnectVo;
import com.piesat.schedule.client.vo.EiSendVo;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zzj
 * @program: backup
 * @描述
 * @创建时间 2019/5/8 11:00
 **/
@Component
public class EiSendUtil {
    private static final Logger logger = LoggerFactory.getLogger(EiSendUtil.class);
    private static String EIDIURL;
    private static String TRANSFERURL;

    @Value("${EIDI.URL}")
    public void setEidiUrl(String eidiUrl) {
        EIDIURL = eidiUrl;
    }
    @Value("${TRANSFER.URL}")
    public  void setTransferUrl(String transferUrl) {
        TRANSFERURL = transferUrl;
    }
    public static void send(EiSendVo eiSendVo, int type, String kIndex, long occurTime, ResultT<String> resultT) {
        eiSendVo.setSystem("SOD");
        eiSendVo.setKindex(kIndex);
        eiSendVo.setOrgTime(new SimpleDateFormat("yyyyMMdd'T'HHmmss").format(new Date()));
        eiSendVo.setEventTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));

        String code = "";
        setEventSuggest(resultT.getEiCode(), kIndex, type, eiSendVo, resultT);
        if (resultT.getEiCode() <= 9) {
            code = "0" + resultT.getEiCode();
        } else {
            code = String.valueOf(resultT.getEiCode());
        }
        eiSendVo.setMsgType("03");
        eiSendVo.setColType("01");
        eiSendVo.setDataFrom("BABJ");
        eiSendVo.setEventType(eiSendVo.getGroudId() + "_1-20-" + code);
        eiSendVo.setEventLevel("02");
        eiSendVo.setKcomment(resultT.getMsg());
        eiSendVo.setEventControl("0");
        eiSendVo.setEventExt2(getLocalHostLANAddress());
        LinkedHashMap map = new LinkedHashMap();
        map.put("type", "SYSTEM.ALARM.EI");
        map.put("name", "EI告警信息");
        map.put("message", "EI告警信息");
        Map mapFields = new HashMap(10);
        LinkedHashMap mapSend = new LinkedHashMap();
        Field[] fields = eiSendVo.getClass().getDeclaredFields();
        for (Field f : fields) {
            String name = "";
            if (f.isAnnotationPresent(HtJson.class)) {
                HtJson column = f.getAnnotation(HtJson.class);
                name = column.name();
            } else {
                name = f.getName();
            }
            mapSend.put(name, getFieldValueByName(f.getName(), eiSendVo));

        }
        map.put("fields", mapSend);
        logger.info("ei-json信息:{}", JSON.toJSONString(map, SerializerFeature.WriteMapNullValue));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> httpEntity = new HttpEntity<>("[" + JSONObject.toJSONString(map, SerializerFeature.WriteNullStringAsEmpty) + "]", headers);
        RestTemplate rst = new RestTemplate();
        try {
            ResponseEntity<String> stringResponseEntity = rst.postForEntity(TRANSFERURL, httpEntity, String.class);
            logger.info("ei发送返回信息:{}", JSON.toJSONString(stringResponseEntity));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Object getFieldValueByName(String name, EiSendVo eiSendVo) {
        String firstletter = name.substring(0, 1).toUpperCase();
        String getter = "get" + firstletter + name.substring(1);
        Method method;
        Object value;
        try {
            method = eiSendVo.getClass().getMethod(getter);
            value = method.invoke(eiSendVo, new EiSendVo[]{});
            return value;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void backUp(EiSendVo eiSendVo) {
        // eiSendVo.setKobject("数据备份");
        //eiSendVo.setKevent("数据备份异常");
        eiSendVo.setKresult("数据备份无法备份文件");


    }

    public static void move(EiSendVo eiSendVo) {
        //eiSendVo.setKobject("数据迁移");
        //eiSendVo.setKevent("数据迁移异常");
        eiSendVo.setKresult("无法完成数据迁移");
    }

    public static void clear(EiSendVo eiSendVo) {
        // eiSendVo.setKobject("数据清除");
        //eiSendVo.setKevent("数据清除异常");
        eiSendVo.setKresult("无法完成数据清除");
    }

    public static void setEventSuggest(int eiCode, String taskName, int type, EiSendVo eiSendVo, ResultT<String> resultT) {
        if (eiCode < 2 || eiCode > 16) {
            eiCode = 8;
            unknownException(resultT);
        }

        try {
            String value = ReturnCodeEnum.getValue(eiCode);
            logger.error("eivalues{}", value);
            if (type == 0) {
                eiSendVo.setGroudId("OP_SOD_F");
                backUp(eiSendVo);
                eiSendVo.setEventTitle("备份任务(" + value + "):${" + taskName + "}");
            }
            if (type == 1) {
                eiSendVo.setGroudId("OP_SOD_C");
                move(eiSendVo);
                eiSendVo.setEventTitle("迁移任务(" + value + "):${" + taskName + "}");
            }
            if (type == 2) {
                eiSendVo.setGroudId("OP_SOD_D");
                clear(eiSendVo);
                eiSendVo.setEventTitle("清除任务(" + value + "):${" + taskName + "}");
            }
            eiSendVo.setKobject(resultT.getkObject());
            eiSendVo.setKevent(resultT.getkEvent());
            eiSendVo.setEventTrag(resultT.getEventTrag());
            eiSendVo.setEventSuggest(resultT.getEventSuggest());

        } catch (Exception e) {
            logger.error("ei错误{}", eiCode);
        }
    }

    public static void executeSqlException(String parentId, String sql, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        try {
            ConnectVo connectVo = dynamicDataSource.getConnectVo(parentId);
            resultT.setkObject("物理库URL:${" + connectVo.getUrl() + "}");
            resultT.setkEvent("sql语句:${"+sql+"},物理库URL:${"+connectVo.getUrl()+"}"+"${"+resultT.getMsg()+"}");

            StringBuilder  eventTrag=new StringBuilder() ;
            eventTrag.append("1.表不存在\n").append("2.存储管理系统资料信息配置错误\n");
            eventTrag.append("3.数据库缺少必要字段\n").append("4.数据库连接错误");
            StringBuilder  eventSuggest=new StringBuilder() ;
            eventSuggest.append("1.检查该资料是否需要配置(进入存储管理系统查看表结构)\n").append("2.备份清除需要字段d_data_id,d_datetime\n");
            eventSuggest.append("3.迁移需要字段d_data_id,d_datetime,d_file_save_hierarchy\n").append("4.检查存储管理系统该资料是否配置外健信息(进入存储管理系统查看表结构外键信息)索引库一般为d_file_id,结构库为d_record_id针对多表资料\n");
            eventSuggest.append("5.检查数据库连接是否正常\n") .append("6.以上是否正常，不正常重启应用程序看是否能解决") ;
            resultT.setEventTrag(eventTrag.toString());
            resultT.setEventSuggest(eventSuggest.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }

    public static void executeSqlException(ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        try {
            ConnectVo connectVo = dynamicDataSource.getConnectVo("primary");
            if (null != connectVo) {
                resultT.setkObject("物理库URL:${" + connectVo.getUrl() + "}");
                resultT.setkEvent("物理库URL:${" + connectVo.getUrl() + "}" + "${" + resultT.getMsg() + "}");
                resultT.setEventTrag("1.数据库连接错误\n" +
                        "2.用户密码被修改");
                resultT.setEventSuggest("1.查看存储管理数据库数据库连接是否正常\n" +
                        "2.查看用户密码是否修改");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void partitionException(String parentId, String partition, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        try {
            ConnectVo connectVo = dynamicDataSource.getConnectVo(parentId);
            if (null != connectVo) {
                resultT.setkObject("物理库URL:${" + connectVo.getUrl() + "}");
                resultT.setkEvent("分区名称:${" + partition + "},${" + resultT.getMsg() + "}");
                resultT.setEventTrag("1.虚谷加锁超时\n" +
                        "2.数据库链接异常");
                resultT.setEventSuggest("请联系虚谷工程师");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    public static void fileException(String path, ResultT<String> resultT) {
        resultT.setkObject(path);
        resultT.setkEvent("文件路径:${" + path + "}${" + resultT.getMsg() + "}");
        resultT.setEventTrag("1.路径错误\n" +
                "2.程序无操作权限\n" +
                "3.NAS出现错误");
        resultT.setEventSuggest("1、检查NAS盘和目录是否正确(检查异常信息中文件路径是否正确)；\n" +
                "2、检查程序权限（检查sod用户是否具有操作权限）");
    }

    public static void gbaseException(String parentId, String shell, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        try {
            ConnectVo connectVo = dynamicDataSource.getConnectVo(parentId);
            if (null != connectVo) {
                resultT.setkObject("物理库URL:${" + connectVo.getUrl() + "}");
                resultT.setkEvent("shell命令:${" + shell + "},${" + resultT.getMsg() + "}");
                resultT.setEventTrag("1.路径错误\n" +
                        "2.GBASE客户端安装错误\n" +
                        "3.NAS出现错误");
                resultT.setEventSuggest("1.建shell命令拷贝至服务器执行看是否成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void xuguException(String parentId, ResultT<String> resultT) {
        DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
        try {
            ConnectVo connectVo = dynamicDataSource.getConnectVo(parentId);
            resultT.setkObject("物理库URL:${" + connectVo.getUrl() + "}");
            resultT.setkEvent(" 物理库URL:${" + connectVo.getUrl() + "}" + "${" + resultT.getMsg() + "}");
            resultT.setEventTrag("1.表不存在\n" +
                    "2.存储管理系统资料信息配置错误\n" +
                    "3.数据库缺少必要字段\n" +
                    "4.数据库连接错误");
            resultT.setEventSuggest("1.检查该资料是否需要配置(进入存储管理系统查看表结构)\n" +
                    "2.备份清除需要字段d_data_id,d_datetime\n" +
                    "3.迁移需要字段d_data_id,d_datetime,d_file_save_hierarchy\n" +
                    "4.检查存储管理系统该资料是否配置外健信息(进入存储管理系统查看表结构外键信息)索引库一般为d_file_id,结构库为d_record_id针对多表资料\n" +
                    "4.检查数据库连接是否正常\n" +
                    "5.以上是否正常，不正常重启应用程序看是否能解决");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }


    }

    public static void unknownException(ResultT<String> resultT) {
        resultT.setkObject("未知");
        resultT.setkEvent("程序内部错误:${" + resultT.getMsg() + "}");
        resultT.setEventTrag("程序内部错误");
        resultT.setEventSuggest("前往存管系统查看相应调度日志");

    }
    public static String getLocalHostLANAddress(){
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
