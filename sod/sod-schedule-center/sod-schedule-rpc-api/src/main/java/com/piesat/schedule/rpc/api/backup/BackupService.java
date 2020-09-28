package com.piesat.schedule.rpc.api.backup;

import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * Created by zzj on 2019/12/23.
 */
public interface BackupService {
    public PageBean selectBackupList(PageForm<BackUpDto> pageForm);
    public BackUpDto selectBackupByParam(String databaseId,String dataClassId);
    public BackUpDto findBackupById(String backupId);
    public void saveBackup(BackUpDto backUpDto);
    public void updateBackup(BackUpDto backUpDto);
    public void deleteBackupByIds(String[] backupIds);
    public List<Map<String,Object>> findDatabase();
    public List<Map<String,Object>> findDataClassId(String dataBaseId,String dataClassId);
    public void exportExcel(BackUpDto backUpDto);
    public ResultT<String> execute(String id);
}
