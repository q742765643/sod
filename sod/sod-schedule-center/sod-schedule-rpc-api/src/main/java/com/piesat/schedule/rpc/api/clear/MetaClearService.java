package com.piesat.schedule.rpc.api.clear;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.dto.clear.MetaClearDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 15:28
 **/
public interface MetaClearService {

    public PageBean selectMetaClearList(PageForm<MetaClearDto> pageForm);

    public MetaClearDto findMetaClearById(String metaClearId);

    public void saveMetaClear(MetaClearDto metaClearDto);

    public void updateMetaClear(MetaClearDto metaClearDto);

    public void deleteMeteClearByIds(String[] metaClearIds);

    public List<TreeVo> findAllTableByIp(String databaseId);

    public List<Map<String,String>> findDataBase();

    public void exportExcel(MetaClearDto metaClearDto);
}

