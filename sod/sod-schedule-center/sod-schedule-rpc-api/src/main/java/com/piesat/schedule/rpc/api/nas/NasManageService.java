package com.piesat.schedule.rpc.api.nas;

import com.piesat.schedule.rpc.dto.nas.NasManageDto;
import com.piesat.schedule.rpc.dto.nas.NasManageFileDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @ClassName : NasManageService
 * @Description : nas申请
 * @Author : zzj
 * @Date: 2020-09-07 14:33
 */
public interface NasManageService {
    public PageBean selectPageList(PageForm<NasManageDto> pageForm);

    public NasManageDto findById(String id);

    public void add(NasManageFileDto nasManageDto, ResultT<String> resultT);

    public void  audit(NasManageDto nasManageDto, ResultT<String> resultT);

    public void deleteIds(String[] ids);
}

