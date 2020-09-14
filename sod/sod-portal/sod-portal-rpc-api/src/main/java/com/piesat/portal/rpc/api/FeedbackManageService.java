package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.FeedbackManageDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * 用户反馈管理
 */
public interface FeedbackManageService {

    PageBean selectPageList(PageForm<FeedbackManageDto> pageForm);

    FeedbackManageDto getDotById(String id);

    FeedbackManageDto updateDto(FeedbackManageDto feedbackManageDto);

    void delete(String id);

    public void deleteRecordByIds(List<String> ids);
}
