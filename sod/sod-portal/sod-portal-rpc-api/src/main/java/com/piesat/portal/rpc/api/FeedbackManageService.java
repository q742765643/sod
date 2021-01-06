package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.FeedbackManageDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * 用户反馈管理
 */
public interface FeedbackManageService {

    PageBean selectPageList(PageForm<FeedbackManageDto> pageForm);

    FeedbackManageDto getDotById(String id);

    FeedbackManageDto updateDto(FeedbackManageDto feedbackManageDto);

    public void deleteById(String id);

    public void deleteRecordByIds(List<String> ids);

    FeedbackManageDto addFeedback(Map<String, String[]> parameterMap);
    List<FeedbackManageDto> getFeedList(String userName, String isShow, String status);
    List<FeedbackManageDto> getIsShow(String isShow);
}
