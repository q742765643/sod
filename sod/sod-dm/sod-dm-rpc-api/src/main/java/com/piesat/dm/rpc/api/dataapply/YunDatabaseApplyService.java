package com.piesat.dm.rpc.api.dataapply;

import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyDto;
import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

public interface YunDatabaseApplyService {

    PageBean selectPageList(PageForm<YunDatabaseApplyDto> pageForm);

    YunDatabaseApplyDto saveDto(YunDatabaseApplyDto yunDatabaseApplyDto);
    YunDatabaseApplyDto updateDto(YunDatabaseApplyDto yunDatabaseApplyDto);
    YunDatabaseApplyDto addorUpdate(Map<String, String[]> parameterMap, String filePath);
    YunDatabaseApplyDto getDotById(String id);

    public void deleteById(String id);

    List<YunDatabaseApplyDto> getByUserId(String userId);

    List<YunDatabaseApplyDto> getByUserIdStorageLogic(String userId,String storageLogic);


    List<YunDatabaseApplyDto> getByDNES(String userId,String storageLogic,String examineStatus,String displayname);
    YunDatabaseApplyDto updateExamineStatus(String id,String examineStatus);

    Map<String, Object> getRecentTime(String classDataId, String ctsCode);


}
