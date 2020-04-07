import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.piesat.monitor.entity.ssh.SshEntity;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-19 17:40
 **/
public class ESJdbcTest {
    public static void main(String[] args) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        SshEntity sshEntity = new SshEntity();
        sshEntity.setUserName("111");
        sshEntity.setCreateTime(new Date());
        Function<SshEntity, String> a = new Function<SshEntity, String>() {
            @Override
            public String apply(SshEntity sshEntity) {
                return sshEntity.getIp();
            }
        };
        List<SshEntity> list = new ArrayList();
        List<String> ips = list.stream().map(a).collect(Collectors.toList());

        try {
            System.out.println(objectMapper.writeValueAsString(sshEntity));
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        /*try  {
            Connection connection = DriverManager.getConnection("jdbc:es://http://10.211.55.7:9200");//将ES-IP换位ES服务器的IP
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(
                    "select\n" +
                            "\t\"@timestamp\",\n" +
                            "\t\"host.name\",\n" +
                            "\t\"event.dataset\",\n" +
                            "\"process.args\",\n" +
                            "\t\"process.working_directory\",\n" +
                            "\t\"process.pgid\",\n" +
                            "\t\"process.pid\",\n" +
                            "\t\"process.ppid\",\n" +
                            "\t\"system.process.state\",\n" +
                            "\t\"user.name\"\n" +
                            "from\n" +
                            "\t\"metricbeat-*\"\n" +
                            "where\n" +
                            "\t1 = 1\n" +
                            "\tAND \"event.dataset\" = 'system.process'  ORDER BY \"@timestamp\" DESC");
            while(results.next()){
                String[] a= (String[]) results.getObject("process.args");
                System.out.println(a);
            }*/


    }

}

