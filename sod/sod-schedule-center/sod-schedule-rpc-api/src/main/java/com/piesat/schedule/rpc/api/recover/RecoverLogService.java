package com.piesat.schedule.rpc.api.recover;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.dto.recover.RecoverLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-05 18:12
 **/
public interface RecoverLogService {
    public PageBean selectRecoverLogList(PageForm<RecoverLogDto> pageForm);

    public  RecoverLogDto findRecoverLogById(String recoverLogId);

    public void deleteRecoverLogByIds(String[] recoverLogIds);

    public List<TreeVo> getDataFileList(String databaseId, String dataClassId,String path);

    public List<TreeVo> getFileChidren(String childrenPath);

    public void recoverStructedData(RecoverLogDto recoverLogDto);

    public List<Map<String,Object>> md5Check(List<String> paths);

}


