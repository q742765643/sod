package com.piesat.dm.rpc.service.dataapply;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.dao.dataapply.NewdataTableColumnDao;
import com.piesat.dm.entity.dataapply.NewdataTableColumnEntity;
import com.piesat.dm.rpc.api.dataapply.NewdataTableColumnService;
import com.piesat.dm.rpc.dto.dataapply.NewdataTableColumnDto;
import com.piesat.dm.rpc.mapper.dataapply.NewdataTableColumnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/18 21:33
 */
@Service
public class NewdataTableColumnServiceImpl extends BaseService<NewdataTableColumnEntity> implements NewdataTableColumnService{

    @Autowired
    private NewdataTableColumnDao newdataTableColumnDao;
    @Autowired
    private NewdataTableColumnMapper newdataTableColumnMapper;
    @Override
    public BaseDao<NewdataTableColumnEntity> getBaseDao() {
        return this.newdataTableColumnDao;
    }

    @Override
    public NewdataTableColumnDto saveDto(NewdataTableColumnDto newdataTableColumnDto) {
        NewdataTableColumnEntity newdataTableColumnEntity = this.newdataTableColumnMapper.toEntity(newdataTableColumnDto);
        newdataTableColumnEntity = this.saveNotNull(newdataTableColumnEntity);
        return this.newdataTableColumnMapper.toDto(newdataTableColumnEntity);
    }

    @Transactional
    @Override
    public void deleteByApplyId(String applyId) {
        newdataTableColumnDao.deleteByApplyId(applyId);
    }

    @Override
    public List<NewdataTableColumnDto> findByApplyId(String applyId) {
        return this.newdataTableColumnMapper.toDto(this.newdataTableColumnDao.findByApplyId(applyId));
    }


}
