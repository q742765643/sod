package com.piesat.ucenter.rpc.service.system;

import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.ucenter.dao.system.DeptDao;
import com.piesat.ucenter.entity.system.DeptEntity;
import com.piesat.ucenter.mapper.system.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/27 19:40
 */
@Service
public class DeptServiceImpl extends BaseService<DeptEntity> {
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private DeptMapper deptMapper;
    @Override
    public BaseDao<DeptEntity> getBaseDao() {
        return deptDao;
    }
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<DeptEntity> selectDeptList(DeptEntity dept)
    {
        return deptMapper.selectDeptList(dept);
    }
    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<DeptEntity> buildDeptTree(List<DeptEntity> depts)
    {
        List<DeptEntity> returnList = new ArrayList<DeptEntity>();
        for (Iterator<DeptEntity> iterator = depts.iterator(); iterator.hasNext();)
        {
            DeptEntity t = (DeptEntity) iterator.next();
            // 根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals("0"))
            {
                recursionFn(depts, t);
                returnList.add(t);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }
    /**
     * 递归列表
     */
    private void recursionFn(List<DeptEntity> list, DeptEntity t) {
        // 得到子节点列表
        List<DeptEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DeptEntity tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<DeptEntity> it = childList.iterator();
                while (it.hasNext()) {
                    DeptEntity n = (DeptEntity) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }
    /**
     * 得到子节点列表
     */
    private List<DeptEntity> getChildList(List<DeptEntity> list, DeptEntity t)
    {
        List<DeptEntity> tlist = new ArrayList<DeptEntity>();
        Iterator<DeptEntity> it = list.iterator();
        while (it.hasNext())
        {
            DeptEntity n = (DeptEntity) it.next();
            if (n.getParentId().equals( t.getId()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildDeptTreeSelect(List<DeptEntity> depts)
    {
        List<DeptEntity> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    private boolean hasChild(List<DeptEntity> list, DeptEntity t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */

    public List<String> selectDeptListByRoleId(String roleId)
    {
        return deptMapper.selectDeptListByRoleId(roleId);
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public DeptEntity selectDeptById(String deptId)
    {
        return deptDao.getOne(deptId);
    }
    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public DeptEntity insertDept(DeptEntity dept)
    {
        return deptDao.save(dept);
    }
}
