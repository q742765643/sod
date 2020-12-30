package com.piesat.dm.rpc.api.dataapply;


import com.piesat.dm.rpc.dto.dataapply.YunDatabaseApplyFeedbackDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

public interface YunDatabaseApplyFeedbackService {
    PageBean selectPageList(PageForm<YunDatabaseApplyFeedbackDto> pageForm);
    YunDatabaseApplyFeedbackDto saveDto(YunDatabaseApplyFeedbackDto yunDatabaseApplyFeedbackDto);
    YunDatabaseApplyFeedbackDto addFeedback(Map<String, String[]> parameterMap);
    YunDatabaseApplyFeedbackDto getFeeById(String id);
    YunDatabaseApplyFeedbackDto updateDto(YunDatabaseApplyFeedbackDto yunDatabaseApplyDto);
    List<YunDatabaseApplyFeedbackDto> getByItserviceId(String feedbackId, String feedbackStatus);
    YunDatabaseApplyFeedbackDto updateFeedbackStatus(String id,String feedbackStatus);
    public void deleteByFeedbackId(String feedbackId, String feedbackStatus);
    public void deleteById(String id);
}
