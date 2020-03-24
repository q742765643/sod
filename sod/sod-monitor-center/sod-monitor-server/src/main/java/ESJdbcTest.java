import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-19 17:40
 **/
public class ESJdbcTest {
    public static void main(String[] args) {
        try  {
            Connection connection = DriverManager.getConnection("jdbc:es://http://10.211.55.7:9200");//将ES-IP换位ES服务器的IP
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(
                    "select data from location-index1  LIMIT 5");
            while(results.next()){
                System.out.println(results.getString("data"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

