import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.monitor.entity.ssh.SshEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-19 17:40
 **/
public class ESJdbcTest {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        SshEntity sshEntity=new SshEntity();
        sshEntity.setUserName("111");
        System.out.println(objectMapper.writeValueAsString(sshEntity));
        try  {
            Connection connection = DriverManager.getConnection("jdbc:es://http://10.211.55.7:9200");//将ES-IP换位ES服务器的IP
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(
                    "SELECT \"@timestamp\" from \"metricbeat-6.8.7-2020.03.31\" limit 10");
            while(results.next()){
                System.out.println(results.getString("@timestamp"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

