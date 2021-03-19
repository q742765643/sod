package com.piesat.dm.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.ReviewLogDao;
import com.piesat.dm.dao.datatable.TableSqlDao;
import com.piesat.dm.entity.ReviewLogEntity;
import com.piesat.dm.entity.datatable.TableSqlEntity;
import com.piesat.dm.rpc.api.ReviewLogService;
import com.piesat.dm.rpc.api.datatable.TableSqlService;
import com.piesat.dm.rpc.dto.ReviewLogDto;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.dm.rpc.mapper.ReviewLogMapper;
import com.piesat.dm.rpc.mapper.datatable.TableSqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:08:01
 */
@Service
public class ReviewLogServiceImpl extends BaseService<ReviewLogEntity> implements ReviewLogService {
    @Autowired
    private ReviewLogDao reviewLogDao;
    @Autowired
    private ReviewLogMapper reviewLogMapper;


    @Override
    public BaseDao<ReviewLogEntity> getBaseDao() {
        return this.reviewLogDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReviewLogDto saveDto(ReviewLogDto reviewLogDto) {
        ReviewLogEntity reviewLogEntity = this.reviewLogMapper.toEntity(reviewLogDto);
        reviewLogEntity = this.saveNotNull(reviewLogEntity);
        return this.reviewLogMapper.toDto(reviewLogEntity);
    }

    @Override
    public ReviewLogDto getDotById(String id) {
        ReviewLogEntity reviewLogEntity = this.getById(id);
        return this.reviewLogMapper.toDto(reviewLogEntity);
    }

    @Override
    public List<ReviewLogDto> all() {
        List<ReviewLogEntity> all = this.getAll();
        return this.reviewLogMapper.toDto(all);
    }

    @Override
    public List<ReviewLogEntity> findByBindId(String bindId) {
        return this.reviewLogDao.findByBindIdOrderByUpdateTimeAsc(bindId);
    }
}
