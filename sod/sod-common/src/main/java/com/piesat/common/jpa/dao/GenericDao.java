package com.piesat.common.jpa.dao;

import com.piesat.common.jpa.page.PageBean;
import com.piesat.common.jpa.page.PageForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-11-17 18:43
 **/
@NoRepositoryBean
public interface GenericDao<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>
{
    public EntityManager getEm();
    public List<Map<String,Object>> queryByNativeSQL(String sql, Map<String,Object> params);
    public List<Map<String,Object>> queryByNativeSQL(String sql, Object... params);
    public List<?> queryByNativeSQL(String sql,Class entityClass,Map<String,Object> params);
    public PageBean queryByNativeSQLPageMap(String sql, Map<String,Object> params, PageForm pageForm);
    public PageBean queryByNativeSQLPageList(String sql, Class entityClass, Map<String,Object> params, PageForm pageForm);

}

