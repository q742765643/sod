package com.piesat.sql;

import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-22 16:35
 **/
@RestController
@Api(value="迁移接口",tags = {"迁移接口"})
@RequestMapping("/test/tesMove")
public class MoveTestController {
    private static Connection conn = null;
    private static Statement sm = null;
    @ApiOperation(value = "添加迁移数据", notes = "添加迁移数据")
    @GetMapping("/list")
    public ResultT<String> list(String path)
    {
        try {
            connectSQL("com.xugu.cloudjdbc.Driver","jdbc:xugu://10.20.64.38:5138/BABJ_FIDB?ips=10.20.64.39,10.20.64.40,10.20.64.47,10.20.64.51,10.20.64.52,10.20.64.53&char_set=utf8&recv_mode=0",
                    "usr_manager","manager_123");
            for(int i=0;i<=10000;i++){
                int k = (int)(10+Math.random()*(20-10+1));
                File file = new File(path+"/SATE/FY3-C/L1/MWHSX/1919/191905"+k+"/Z_SATE_C_BAWX_20180816021216_P_FY3C_MWHSX_GBAL_L1_20140521_0003_015KM_MS_"+i+".txt");
                if(!file.exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                String sql="INSERT INTO USR_SOD.SATE_PRODUCT_PART1_FILE_TAB_TEST\n" +
                        "(D_FILE_ID, D_DATA_ID, D_IYMDHM, D_RYMDHM, D_DATETIME, D_STORAGE_SITE, D_FILE_SIZE, V_FILETIME, V_LEVEL, V_CCCC, V01007_01, V02019_01, V05310, V01310, V02402, V29001_01, V04001, V04002, V04003, V04004, V04005, V05301, V01015_REV, V_FILE_FORMAT, V_FILE_NAME, V40410_1, V40410_2, V40410_3, V_RETAIN1_C, V_RETAIN2_C, V_RETAIN3_C, D_FILE_SAVE_HIERARCHY, D_SOURCE_ID)\n" +
                        "VALUES('"+file.getName()+"', 'K.0060.0001.S002', '2019-12-10 08:46:16.000', '2018-08-16 02:13:33.000', '1919-05-21 00:03:00.000', '"+file.getPath()+"', 18794126, '20180816021216', 'L1', 'BAWX', 'FY3C', 'MWHSX', 'GBAL', '', '', '', 2014, 5, 21, 0, 3, '015KM', 'MS', 'HDF', 'Z_SATE_C_BAWX_20180816021216_P_FY3C_MWHSX_GBAL_L1_20140521_0003_015KM_MS.HDF', '', '', '', '', '', '', 0, 'K.0539.0001.R001')";
                // write
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("测试数据");
                bw.flush();
                bw.close();
                fw.close();
                sm.execute(sql);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                sm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ResultT<>();
    }
    public static void connectSQL(String driver, String url, String UserName, String Password) {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, UserName, Password);
            sm = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

