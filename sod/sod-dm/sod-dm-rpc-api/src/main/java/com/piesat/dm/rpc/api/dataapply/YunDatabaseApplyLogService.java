package com.piesat.dm.rpc.api.dataapply;


import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyLogDto;


import java.util.List;
import java.util.Map;

public interface YunDatabaseApplyLogService {
    YunDatabaseApplyLogDto saveLog(YunDatabaseApplyLogDto yunDatabaseApplyLogDto);
    YunDatabaseApplyLogDto addLogEdit(Map<String, String[]> parameterMap);
    YunDatabaseApplyLogDto addLogEdit1(Map<String, String[]> parameterMap,String logId,String examineMaterial);
    List<YunDatabaseApplyLogDto> getByLogId(String logId);
    public void deleteByLogId(String logId);
}
