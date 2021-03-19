package com.piesat.dm.rpc.service.datatable;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.common.enums.StatusEnum;
import com.piesat.dm.dao.datatable.DataTableApplyDao;
import com.piesat.dm.entity.datatable.DataTableApplyEntity;
import com.piesat.dm.mapper.MybatisPageMapper;
import com.piesat.dm.rpc.api.ReviewLogService;
import com.piesat.dm.rpc.api.datatable.DataTableApplyService;
import com.piesat.dm.rpc.api.datatable.DataTableService;
import com.piesat.dm.rpc.dto.ReviewLogDto;
import com.piesat.dm.rpc.dto.datatable.DataTableApplyDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.mapper.datatable.DataTableApplyMapper;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 表申请
 *
 * @author cwh
 * @date 2020年 07月31日 16:48:06
 */
@Service
public class DataTableApplyServiceImpl extends BaseService<DataTableApplyEntity> implements DataTableApplyService {
    @Autowired
    private DataTableApplyDao dataTableApplyDao;
    @Autowired
    private DataTableApplyMapper dataTableApplyMapper;
    @Autowired
    private MybatisPageMapper mybatisPageMapper;
    @Autowired
    private DataTableService dataTableService;
    @Autowired
    private ReviewLogService reviewLogService;

    @Override
    public BaseDao<DataTableApplyEntity> getBaseDao() {
        return this.dataTableApplyDao;
    }

    @Override
    public DataTableApplyDto saveDto(DataTableApplyDto dataTableApplyDto) {
        String id = dataTableApplyDto.getId();
        DataTableApplyDto dotById = this.getDotById(id);
        dotById.setStatus(dataTableApplyDto.getStatus());
        dotById.setReview(dataTableApplyDto.getReview());
        DataTableApplyEntity dataTableApplyEntity = this.dataTableApplyMapper.toEntity(dotById);
        DataTableApplyEntity save = this.save(dataTableApplyEntity);
        UserDto userDto= (UserDto) SecurityUtils.getSubject().getPrincipal();
        ReviewLogDto rl = new ReviewLogDto();
        rl.setBindId(dataTableApplyDto.getId());
        rl.setUserId(userDto.getUserName());
        rl.setStatusInfo("数据表申请");
        rl.setRemark(dataTableApplyDto.getRemark());
        return this.dataTableApplyMapper.toDto(save);
    }

    @Override
    public DataTableApplyDto getDotById(String id) {
        DataTableApplyEntity byId = this.getById(id);
        return this.dataTableApplyMapper.toDto(byId);
    }

    @Override
    public List<DataTableApplyDto> all() {
        List<DataTableApplyEntity> all = this.getAll();
        return this.dataTableApplyMapper.toDto(all);
    }

    @Override
    public PageBean selectPageList(PageForm<Map<String, Object>> pageForm) {
        PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
        List<Map<String, Object>> lists = mybatisPageMapper.pageApplyData(pageForm.getT());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(lists);
        //获取当前页数据
        PageBean pageBean = new PageBean(pageInfo.getTotal(), pageInfo.getPages(), lists);
        return pageBean;
    }

    @Override
    public ResultT review(DataTableApplyDto dataTableApplyDto) {
        try {
            UserDto userDto= (UserDto) SecurityUtils.getSubject().getPrincipal();
            ReviewLogDto rl = new ReviewLogDto();
            rl.setBindId(dataTableApplyDto.getId());
            rl.setUserId(userDto.getUserName());
            if (StatusEnum.match(dataTableApplyDto.getStatus())==StatusEnum.审核通过){
                DataTableInfoDto dti = new DataTableInfoDto();
                BeanUtils.copyProperties(dataTableApplyDto, dti);
                DataTableInfoDto dataTableInfoDto = dataTableService.saveDto(dti);
                rl.setStatusInfo(StatusEnum.matchStatus(2));
            }else if (StatusEnum.match(dataTableApplyDto.getStatus())==StatusEnum.审核未通过){
                rl.setRemark(dataTableApplyDto.getReview());
                rl.setStatusInfo(StatusEnum.matchStatus(3));
            }
            this.saveNotNull(this.dataTableApplyMapper.toEntity(dataTableApplyDto));
            this.reviewLogService.saveDto(rl);
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
