package com.piesat.schedule.rpc.api.backup;

import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 16:46
 **/
public interface MetaBackupService {
    public PageBean selectBackupList(PageForm<MetaBackupDto> pageForm);

    public MetaBackupDto findBackupById(String metaBackupId);

    public void saveBackup(MetaBackupDto metaBackupDto);

    public void updateBackup(MetaBackupDto metaBackupDto);

    public void deleteBackupByIds(String[] metaBackupIds);

    public List<TreeVo> findMeta(String databaseId);

    public List<Map<String,String>> findDataBase();

    public void handExecute(MetaBackupDto metaBackupDto);

    public void exportExcel(MetaBackupDto metaBackupDto);
}

