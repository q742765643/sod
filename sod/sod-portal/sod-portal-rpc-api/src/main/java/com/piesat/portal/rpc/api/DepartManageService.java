package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.DepartManageDto;
import com.piesat.portal.rpc.util.PortalTreeSelect;
import com.piesat.ucenter.rpc.util.TreeSelect;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
public interface DepartManageService {

    PageBean selectPageList(PageForm<DepartManageDto> pageForm);

    List<DepartManageDto> findAllByDeptCodeAsc();

    List<DepartManageDto> findByDeptunicode(String deptunicode);

    List<DepartManageDto> findByDeptcode(String deptcode);

    DepartManageDto getDotById(String id);

    List getTreeSelectDept(DepartManageDto departManageDto);

    DepartManageDto saveDto(DepartManageDto departManageDto);

    DepartManageDto updateDto(DepartManageDto departManageDto);

    void delete(String id);

    List<DepartManageDto> findAllDept();
}
