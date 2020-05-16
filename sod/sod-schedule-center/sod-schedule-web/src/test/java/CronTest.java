import com.piesat.schedule.util.CronExpression;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-16 22:34
 **/
public class CronTest {
    public static void main(String[] args){
        try {
            Date nextValidTime = new CronExpression("55 35 1 1/1 * ?").getNextValidTimeAfter(new Date());
            TimeZone timeZone=new CronExpression("55 35 1 1/1 * ?").getTimeZone();
            String a="1";
            System.out.println("1s".indexOf("GMT"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

