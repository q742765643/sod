package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.FeedbackManageDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * 用户反馈管理
 */
public interface FeedbackManageService {

    PageBean selectPageList(PageForm<FeedbackManageDto> pageForm);

    FeedbackManageDto getDotById(String id);
}
