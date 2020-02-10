package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.TableExportDao;
import com.piesat.dm.dao.TableIndexDao;
import com.piesat.dm.entity.TableIndexEntity;
import com.piesat.dm.rpc.api.TableExportService;
import com.piesat.dm.rpc.api.TableIndexService;
import com.piesat.dm.rpc.dto.TableIndexDto;
import com.piesat.dm.rpc.mapper.TableIndexMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 表结构导出
 *
 * @author wulei
 * @date 2020年 2月 5日 10:40:38
 */
@Service
public class TableExportServiceImpl extends BaseService<TableIndexEntity> implements TableExportService {
    @Autowired
    private TableExportDao tableExportDao;

    @Override
    public BaseDao<TableIndexEntity> getBaseDao() {
        return null;
    }

    @Override
    public Map<String, Object> getExportMap(String physicalDBParam, String dataClassIdParam) {
        try {
            dataClassIdParam = "'"+dataClassIdParam.replaceAll(",", "','")+"'";
            System.out.println(dataClassIdParam);
            //获取导出的资料集合
            List<Map<String,Object>> dataList = tableExportDao.getDataList(dataClassIdParam,physicalDBParam);
            //循环获取table_id
            String tableId = "";
            if(dataList!=null&&dataList.size()>0){
                StringBuilder tableIdSB = new StringBuilder();
                for(Map<String,Object> map : dataList){
                    tableIdSB.append(map.get("TABLE_ID")).append(",");
                }
                tableId = tableIdSB.toString();
                if(tableId.length()>0){
                    tableId = tableId.substring(0,tableId.length()-1);
                }
            }
            if(tableId.length()>0){
                tableId = "'"+tableId.replaceAll(",", "','")+"'";
            }else{
                return null;
            }
            //获取字段信息
            List<Map<String,Object>> columnList = new ArrayList<Map<String,Object>>();
            columnList.addAll(tableExportDao.getColumnList(tableId));
            //获取资料索引信息
            List<Map<String,Object>> indexList = new ArrayList<Map<String,Object>>();
            indexList.addAll(tableExportDao.getIndexList(tableId));
            //获取资料分库分表信息
            List<Map<String,Object>> sharedList = new ArrayList<Map<String,Object>>();
            sharedList.addAll(tableExportDao.getSharedList(tableId,physicalDBParam));
            for(int i=0; i<dataList.size(); i++){
                Map<String,Object> dataMap = dataList.get(i);
                String table_id = dataMap.get("TABLE_ID").toString();
                //处理字段信息
                int columnNum = 0;
                List<Map<String,Object>> subColumnList = new ArrayList<Map<String,Object>>();
                for(int j=0; j<columnList.size(); j++){
                    Map<String,Object> map = columnList.get(j);
                    String id = map.get("TABLE_ID").toString();
                    if(table_id.equals(id)){
                        columnNum++;
                        map.put("column_num", columnNum);
                        String ele_name = map.get("ELE_NAME")==null?"":map.get("ELE_NAME").toString();
                        ele_name = ele_name.replaceAll("<", "");
                        ele_name = ele_name.replaceAll(">", "");
                        ele_name = ele_name.replaceAll("≥", "");
                        map.put("ele_name", ele_name);
                        map.put("db_ele_code", map.get("DB_ELE_CODE"));
                        String type = getStr(map.get("TYPE").toString());
                        map.put("type", type);
                        //map.put("accuracy", map.get("ACCURACY"));
                        String accuracy = (String)map.get("ACCURACY");
                        if(accuracy != null && !"".equals(accuracy)){
                            accuracy = accuracy.replace(".", ",");
                            map.put("type", map.get("TYPE") + "("+accuracy+")");
                        }
                        String constraint = "";
                        if((map.get("IS_NULL")+"").equals("true")){
                            constraint = "可为空";
                        }else{
                            constraint = "不为空";
                        }
                        if((map.get("IS_PREMARY_KEY")+"").equals("true")){
                            constraint = constraint+",主键";
                        }
                        map.put("constraint", constraint);
                        map.put("description", map.get("NAME_CN")==null?"":map.get("NAME_CN"));
                        subColumnList.add(map);
                    }
                }
                dataMap.put("columnList", subColumnList);
                //处理索引信息
                List<Map<String,Object>> subIndexList = new ArrayList<Map<String,Object>>();
                int indexNum = 0;
                for(int j=0; j<indexList.size(); j++){
                    Map<String,Object> map = indexList.get(j);
                    String id = map.get("TABLE_ID").toString();
                    if(table_id.equals(id)){
                        indexNum++;
                        map.put("index_num", indexNum);
                        map.put("index_type", map.get("INDEX_TYPE"));
                        map.put("index_name", map.get("INDEX_NAME"));
                        map.put("index_column", map.get("INDEX_COLUMN"));
                        subIndexList.add(map);
                    }
                }
                dataMap.put("indexList", subIndexList);
                if (subIndexList.size()==0){
                    dataMap.put("haveIndex","N");
                }else {
                    dataMap.put("haveIndex","Y");
                }				//处理分区信息
                int sharedNum = 0;
                List<Map<String,Object>> subSharedList = new ArrayList<Map<String,Object>>();
                for(int j=0; j<sharedList.size(); j++){
                    Map<String,Object> map = sharedList.get(j);
                    String id = map.get("TABLE_ID").toString();
                    if(table_id.equals(id)){
                        sharedNum++;
                        map.put("shared_num", sharedNum);
                        String type = map.get("SHARDING_TYPE").toString().equals("D")?"分库键":"分表键";
                        map.put("shared_type", type);
                        map.put("shared_column", map.get("FIELD"));
                        subSharedList.add(map);
                    }
                }
                dataMap.put("sharedList", subSharedList);
                if (subSharedList.size()==0){
                    dataMap.put("haveShared","N");
                }else {
                    dataMap.put("haveShared","Y");
                }
            }

            //获取资料分类集合
            List<Map<String,Object>> typeList = new ArrayList<Map<String,Object>>();
            typeList.addAll(tableExportDao.getTypeList(dataClassIdParam));
            for(int i=0; i<typeList.size(); i++){
                Map<String,Object> typemap = typeList.get(i);
                String typeCode = typemap.get("DATA_CLASS_ID").toString();
                typemap.put("menu_type_name", (i+1)+"."+typemap.get("CLASS_NAME"));
                List<Map<String,Object>> typeDataList = new ArrayList<Map<String,Object>>();
                int num = 0;

                for(int j=0; j<dataList.size(); j++){
                    int num_min = 0;
                    Map<String,Object> dataMap = dataList.get(j);
                    /*ModeArea m = new ModeArea();
                    m.setArea_id(dataMap.get("DATA_CLASS_ID").toString());
                    ModeArea modearea = null;
                    if ("FIDB".equals(physicalDBParam))	{
                        modearea = this.modeEleDefDao.getAreaById(m);
                        num_min = 1;
                    }*/


                    if(dataMap.get("DATA_CLASS_ID").toString().startsWith(typeCode)){
                        num++;
                        //标题赋值
                        dataMap.put("menu_data_name", (i+1)+"."+num+"."+getStr(dataMap.get("NAME_CN").toString()));
                        /*if (num_min == 1){
                            dataMap.put("menu_sa_description_name", (i+1)+"."+num+"."+(num_min)+".存储目录");
                            dataMap.put("directory_specification", modearea == null ? "" : modearea.getArea_name());
                            dataMap.put("haveDS","Y");
                        }else {
                            dataMap.put("haveDS","N");
                        }*/
                        dataMap.put("menu_sub_description", (i+1)+"."+num+"."+(num_min+1)+".表内容概述");
                        dataMap.put("menu_sub_table", (i+1)+"."+num+"."+(num_min+2)+".表结构信息");
                        dataMap.put("menu_sub_index", (i+1)+"."+num+"."+(num_min+3)+".索引信息");
                        dataMap.put("menu_sub_shared", (i+1)+"."+num+"."+(num_min+4)+".分库分表信息");
                        //表内容表格
                        dataMap.put("data_name", getStr(dataMap.get("NAME_CN").toString()));
                        dataMap.put("table_name", dataMap.get("TABLE_NAME").toString().toUpperCase());
                        dataMap.put("d_data_id", dataMap.get("D_DATA_ID").toString().toUpperCase());
                        dataMap.put("primary_key","");
                        typeDataList.add(dataMap);
                    }
                }
                typemap.put("dataInfoList", typeDataList);
            }
            Map<String,Object> infoMap = new HashMap<String, Object>();
            infoMap.put("typeInfoList", typeList);
            infoMap.put("word_date", new SimpleDateFormat("yyyy年M月d日").format(new Date()));
            return infoMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getLogicName(String database_logic) {
        return null;
    }

    @Override
    public String getDatabaseName(String physical_id) {
        return null;
    }

    private String getStr(String str){
        return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
