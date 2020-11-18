package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.ApiManageDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 接口管理
 */
public interface ApiManageService {

    PageBean selectPageList(PageForm<ApiManageDto> pageForm);

    Map<String,Object> getDotById(String id);

    void deleteApi(String id);

    ApiManageDto saveDto(ApiManageDto apiManageDto);

    ApiManageDto updateDto(ApiManageDto apiManageDto);

    ResultT uploadFile(MultipartFile file,String apiSys);

    List<ApiManageDto> findByApiSys(ApiManageDto apiManageDto);

    List<ApiManageDto> findApiAll();
}
