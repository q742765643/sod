package com.piesat.portal.rpc.service;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.IdUtils;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.DepartManageDao;
import com.piesat.portal.entity.DepartManageEntity;
import com.piesat.portal.rpc.api.DepartManageService;
import com.piesat.portal.rpc.dto.DepartManageDto;
import com.piesat.portal.rpc.mapstruct.DepartManageMapstruct;
import com.piesat.portal.rpc.util.PortalTreeSelect;
import com.piesat.portal.rpc.util.PortalTreeSelectP;
import com.piesat.ucenter.rpc.util.TreeSelect;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("departManageService")
public class DepartManageServiceImpl extends BaseService<DepartManageEntity> implements DepartManageService {

    @Autowired
    private DepartManageDao departManageDao;

    @Autowired
    private DepartManageMapstruct departManageMapstruct;

    @Override
    public BaseDao<DepartManageEntity> getBaseDao() {
        return departManageDao;
    }




    @Override
    public PageBean selectPageList(PageForm<DepartManageDto> pageForm) {
        DepartManageDto departManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(departManageDto.getDeptcode())){
            specificationBuilder.add("deptcode", SpecificationOperator.Operator.eq.name(),departManageDto.getDeptcode());
        }
        if(StringUtils.isNotEmpty(departManageDto.getDeptname())){
            specificationBuilder.add("deptname", SpecificationOperator.Operator.likeAll.name(),departManageDto.getDeptname());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,Sort.by(Sort.Direction.ASC, "deptcode"));
        List<DepartManageEntity> departManageEntities = (List<DepartManageEntity>) pageBean.getPageData();
        List<DepartManageDto> departManageDtos = departManageMapstruct.toDto(departManageEntities);
        //查父级  但是省级和国家级的不一样

        if(departManageDtos != null && departManageDtos.size()>0){
            for(DepartManageDto departManage : departManageDtos){
                List<DepartManageDto> parentDepartManage = null;
                if("C".equals(this.warLevel())){
                    parentDepartManage = findByDeptunicode(departManage.getParentCode());
                }else{
                    parentDepartManage = findByDeptcode(departManage.getParentCode());
                }
                if(parentDepartManage != null && parentDepartManage.size()>0){
                    departManage.setParentName(parentDepartManage.get(0).getDeptname());
                }
            }
        }
        pageBean.setPageData(departManageDtos);
        return pageBean;
    }

    //判断是省级还是国家局
    public String warLevel(){
        String warLevel = "C";
        List<DepartManageDto> allByDeptCodeAsc = findAllByDeptCodeAsc();
        if(allByDeptCodeAsc != null && allByDeptCodeAsc.size()>0 && "001".equals(allByDeptCodeAsc.get(0).getParentCode())){//省级
            warLevel = "P";//省级
        }
        return warLevel;
    }

    @Override
    public List<DepartManageDto> findAllByDeptCodeAsc() {
        Sort sort = Sort.by(Sort.Direction.ASC, "deptcode");
        List<DepartManageEntity> departManageEntities = departManageDao.findAll(sort);
        return departManageMapstruct.toDto(departManageEntities);
    }

    @Override
    public List<DepartManageDto> findByDeptunicode(String deptunicode) {
        List<DepartManageEntity> departManageEntities = departManageDao.findByDeptunicode(deptunicode);
       return departManageMapstruct.toDto(departManageEntities);
    }

    @Override
    public List<DepartManageDto> findByDeptcode(String deptcode) {
        List<DepartManageEntity> departManageEntities = departManageDao.findByDeptcode(deptcode);
        return departManageMapstruct.toDto(departManageEntities);
    }

    @Override
    public DepartManageDto getDotById(String id) {
        DepartManageEntity departManageEntity = this.getById(id);
        return this.departManageMapstruct.toDto(departManageEntity);
    }

    @Override
    public List getTreeSelectDept(DepartManageDto departManageDto) {
        List<DepartManageEntity> departManageEntities = this.getAll();
        List<PortalTreeSelect> treeSelects = this.buildDeptTreeSelect(departManageMapstruct.toDto(departManageEntities));
        return treeSelects;
    }

    @Override
    public DepartManageDto saveDto(DepartManageDto departManageDto) {
        DepartManageEntity departManageEntity = departManageMapstruct.toEntity(departManageDto);
        if(StringUtils.isEmpty(departManageEntity.getId())){
            departManageEntity.setId(IdUtils.simpleUUID());
        }
        departManageEntity.setUpdateTime(new Date());
        departManageEntity = this.saveNotNull(departManageEntity);
        return this.departManageMapstruct.toDto(departManageEntity);
    }

    @Override
    public DepartManageDto updateDto(DepartManageDto departManageDto) {
        DepartManageEntity departManageEntity = departManageMapstruct.toEntity(departManageDto);
        departManageEntity = this.saveNotNull(departManageEntity);
        return this.departManageMapstruct.toDto(departManageEntity);
    }

    @Override
    public List<DepartManageDto> findAllDept() {
        List<DepartManageEntity> departManageEntities = this.getAll();
        return this.departManageMapstruct.toDto(departManageEntities);
    }

    public List buildDeptTreeSelect(List<DepartManageDto> depts)
    {
        String warLevelStr = warLevel();
        List<DepartManageDto> deptTrees = buildDeptTree(depts,warLevelStr);
        if("C".equals(warLevelStr)){
            return deptTrees.stream().map(PortalTreeSelect::new).collect(Collectors.toList());
        }else{
            return deptTrees.stream().map(PortalTreeSelectP::new).collect(Collectors.toList());
        }

    }

    public List<DepartManageDto> buildDeptTree(List<DepartManageDto> depts,String warLevelStr)
    {
        List<DepartManageDto> returnList = new ArrayList<DepartManageDto>();

        for (Iterator<DepartManageDto> iterator = depts.iterator(); iterator.hasNext();)
        {
            DepartManageDto t = (DepartManageDto) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if ("C".equals(warLevelStr) && t.getParentCode().equals("0"))
            {
                recursionFn(depts, t, warLevelStr);
                returnList.add(t);
            }else if("P".equals(warLevelStr) && t.getParentCode().equals("001")){
                recursionFn(depts, t, warLevelStr);

                returnList.add(t);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    private void recursionFn(List<DepartManageDto> list, DepartManageDto t,String warLevelStr) {
        // 得到子节点列表
        List<DepartManageDto> childList = getChildList(list, t, warLevelStr);
        t.setChildren(childList);
        for (DepartManageDto tChild : childList) {
            recursionFn(list, tChild, warLevelStr);
        }
    }

    private boolean hasChild(List<DepartManageDto> list, DepartManageDto t,String warLevelStr)
    {
        return getChildList(list, t, warLevelStr).size() > 0 ? true : false;
    }

    private List<DepartManageDto> getChildList(List<DepartManageDto> list, DepartManageDto t,String warLevelStr)
    {
        List<DepartManageDto> tlist = new ArrayList<DepartManageDto>();
        Iterator<DepartManageDto> it = list.iterator();
        while (it.hasNext())
        {
            DepartManageDto n = (DepartManageDto) it.next();
            if("C".equals(warLevelStr)){
                if (n.getParentCode().equals( t.getDeptunicode()))
                {
                    tlist.add(n);
                }
            }else{
                if (n.getParentCode().equals( t.getDeptcode()))
                {
                    tlist.add(n);
                }
            }

        }
        return tlist;
    }


}
