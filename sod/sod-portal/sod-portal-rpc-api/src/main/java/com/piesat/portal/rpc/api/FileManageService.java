package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.FileManageDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.Map;

/**
 * 文件管理
 */
public interface FileManageService {

    PageBean selectPageList(PageForm<FileManageDto> pageForm);

    FileManageDto saveDto(FileManageDto fileManageDto);

    void delete(String id);

    FileManageDto getDotById(String id);

    FileManageDto updateDto(FileManageDto fileManageDto);
}
