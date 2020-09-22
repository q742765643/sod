package com.piesat.schedule.client.util;

import com.alibaba.fastjson.JSON;
import com.piesat.common.utils.OwnException;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-27 18:09
 **/
@Slf4j
public class CmdUtil {
    public static int expCmd(String[] commands, ResultT<String> resultT){
        StringBuilder cmd=new StringBuilder();
        Runtime r = Runtime.getRuntime();
        StringBuilder msg=new StringBuilder();
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        int exitVal=-1;
        try {

            log.info("执行cmd {}", JSON.toJSONString(commands));
            Process proc = r.exec(commands);
            exitVal = proc.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(proc.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                msg.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                msg.append(line).append('\n');
            }
            if(null!=msg) {
                log.info(msg.toString());
            }

        } catch (Exception e) {
            log.error(OwnException.get(e));
        }finally {
            if(null!=bufrIn){
                try {
                    bufrIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null!=bufrError){
                try {
                    bufrError.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return exitVal;
    }

}

