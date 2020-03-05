package com.piesat.schedule.rpc.api.recover;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.dto.recover.MetaRecoverLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * Created by zzj on 2020/3/3.
 */
public interface MetaRecoverLogService {
    public PageBean selectMetaRecoverLogList(PageForm<MetaRecoverLogDto> pageForm);

    public  MetaRecoverLogDto findMetaRecoverLogById(String metaRecoverLogId);

    public void deleteMetaRecoverLogByIds(String[] MetaRecoverLogIds);

    public void recover(MetaRecoverLogDto metaRecoverLogDto);

    public List<TreeVo> getFileList(String databaseId, String storageDirectory);

    public List<TreeVo> getFileChidren(String childrenPath);

    public Map<String,Object> parsingPath(String path);

}
