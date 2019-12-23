import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.web.controller.ScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:59
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class JobTest {
    @Autowired
    private BackupService backupService;
    @Test
    public void test1() throws Exception {
        BackUpDto backUpDto=new BackUpDto();
        backUpDto.setDatabaseId("STDB");
        backUpDto.setDataClassId("A.0001.0001.M001");
        backUpDto.setProfileName("测试资料");
        backUpDto.setTableName("T_SOD_T");
        backUpDto.setConditions("where 1=1");
        backUpDto.setSecondConditions("where 1=1");
        backUpDto.setDdataId("A.0001.0001.S001");
        backUpDto.setJobCron("0/1 * * * * ? ");
        backUpDto.setJobDesc("测试");
        backUpDto.setStorageDirectory("/zzj/date");
        backUpDto.setExecutorHandler("AAA");
        backUpDto.setExecutorFailRetryCount(3);
        backUpDto.setExecutorTimeout(120);
        backUpDto.setExecutorBlockStrategy("单机串行");
        backUpDto.setExecutorRouteStrategy("hash");
        backupService.saveBackup(backUpDto);

    }
}

