package com.piesat.schedule.client.service;

import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.entity.DatabaseOperationVo;
import com.piesat.schedule.entity.IndexVo;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.schedule.entity.move.MoveLogEntity;
import com.piesat.schedule.mapper.DatabaseOperationMapper;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-14 15:15
 **/
@Service
public class DatabaseOperationService {
    @Autowired
    private DatabaseOperationMapper databaseOperationMapper;

    public long selectTableCount(String parentId,String ktable, String conditions, ResultT<String> resultT){
        long count=0;
        DataSourceContextHolder.setDataSource(parentId);
        try {

            DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
            databaseOperationVo.setKtable(ktable);
            databaseOperationVo.setConditions(conditions);
            count= databaseOperationMapper.selectTableCount(databaseOperationVo);
        } catch (Exception e) {
            resultT.setErrorMessage("表{}查询应删除条数失败,错误{}",ktable, OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);

        }finally {
            DataSourceContextHolder.clearDataSource();
        }
        return count;
    }

    public Date selectKtableMaxTime(String ktable, String conditions){
        DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
        databaseOperationVo.setKtable(ktable);
        databaseOperationVo.setConditions(conditions);
       return databaseOperationMapper.selectKtableMaxTime( databaseOperationVo);
    }

    public int delteKtable(ClearLogEntity clearLogEntity, String conditions){
        DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
        databaseOperationVo.setKtable(clearLogEntity.getTableName());
        databaseOperationVo.setConditions(conditions);
        return databaseOperationMapper.delteKtable(databaseOperationVo);
    }

    public int deleteVtable(ClearLogEntity clearLogEntity, String conditions){
        DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
        databaseOperationVo.setKtable(clearLogEntity.getTableName());
        databaseOperationVo.setConditions(conditions);
        databaseOperationVo.setVtable(clearLogEntity.getVTableName());
        databaseOperationVo.setFk(clearLogEntity.getForeignKey());
        return databaseOperationMapper.deleteVtable(databaseOperationVo);
    }

    public List<Map<String,Object>> selectXuguPartition(String schemaName, String tableName){
        return databaseOperationMapper.selectXuguPartition(schemaName,tableName);
    }

    public int deletePartition(String tableName,String partName){
        return databaseOperationMapper.deletePartition(tableName,partName);
    }

    public List<Map<String,Object>> selectByKCondition(String parentId,String table,String conditions,ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(parentId);
        List<Map<String,Object>> mapList=new ArrayList<>();
        try {
            DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
            databaseOperationVo.setKtable(table);
            databaseOperationVo.setConditions(conditions);
            mapList= databaseOperationMapper.selectByKCondition(databaseOperationVo);
        } catch (Exception e) {
            resultT.setErrorMessage("表{}查询失败,错误{}",table, OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);

        }finally {
            DataSourceContextHolder.clearDataSource();
        }
        return mapList;
    }

    public List<Map<String,Object>> selectByVCondition(MoveLogEntity moveLogEntity,String conditions,ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(moveLogEntity.getParentId());
        List<Map<String,Object>> mapList=new ArrayList<>();

        try {
            DatabaseOperationVo databaseOperationVo=new DatabaseOperationVo();
            databaseOperationVo.setKtable(moveLogEntity.getTableName());
            databaseOperationVo.setConditions(conditions);
            databaseOperationVo.setVtable(moveLogEntity.getVTableName());
            databaseOperationVo.setFk(moveLogEntity.getForeignKey());
            return  databaseOperationMapper.selectByVCondition(databaseOperationVo);
        } catch (Exception e) {
            resultT.setErrorMessage("表{}查询失败,错误{}",moveLogEntity.getVTableName(), OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);

        }finally {
            DataSourceContextHolder.clearDataSource();
        }
        return mapList;
    }

    public long updateIndex(String parentId,IndexVo indexVo,ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(parentId);
        long count=0;
        try {
            count= databaseOperationMapper.updateIndex(indexVo);
        } catch (Exception e) {
            resultT.setErrorMessage("表{}更新失败,错误{}",indexVo.getTable(), OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);

        }finally {
            DataSourceContextHolder.clearDataSource();
        }
        return count;
    }

    public long deleteIndex(String parentId,IndexVo indexVo,ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(parentId);
        long count=0;
        try {
            count= databaseOperationMapper.deleteIndex(indexVo);
        } catch (Exception e) {
            resultT.setErrorMessage("表{}更新失败,错误{}",indexVo.getTable(), OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);

        }finally {
            DataSourceContextHolder.clearDataSource();
        }
        return count;
    }
}

