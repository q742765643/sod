package com.piesat.common.jpa.dao;

import com.piesat.common.jpa.page.PageBean;
import com.piesat.common.jpa.page.PageForm;
import lombok.Data;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
@Data
public class GenericDaoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericDao<T, ID>
{
    protected static final Logger logger;
    private final EntityManager em;
    private final Class<T> entityClass;
    private final String entityName;

    static {
        logger = LoggerFactory.getLogger((Class)GenericDaoImpl.class);
    }

    public GenericDaoImpl(final JpaEntityInformation<T, ID> entityInformation, final EntityManager entityManager) {
        super((JpaEntityInformation)entityInformation, entityManager);
        this.em = entityManager;
        this.entityClass = (Class<T>)entityInformation.getJavaType();
        this.entityName = entityInformation.getEntityName();
    }
    @Override
    public EntityManager getEm() {
        return this.em;
    }

    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    public String getEntityName() {
        return this.entityName;
    }

    /**
     * 执行原生SQL查询
     * @param sql
     * @param params sql参数
     * @return 结果集并影射成Map
     */
    @Override
    public List<Map<String,Object>> queryByNativeSQL(String sql, Map<String,Object> params){
        Query query=em.createNativeQuery(sql);
        if(params!=null&&params.size()>0){
            for(String param:params.keySet() ){
                query.setParameter(param,params.get(param));
            }
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
    /**
     * 执行原生SQL查询
     * @param sql
     * @param params sql参数
     * @return 结果集并影射成Map
     */
    @Override
    public List<Map<String,Object>> queryByNativeSQL(String sql, Object... params){
        Query query=em.createNativeQuery(sql);
        if(params!=null&&params.length>0){
            for(int i=0;i< params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
    /**
     * 执行原生SQL查询
     * @param sql
     * @param entityClass 结果转换实体类
     * @param params sql参数
     * @return 结果集并影射成entityClass
     */
    @Override
    public List<?> queryByNativeSQL(String sql, Class entityClass, Map<String,Object> params){
        Query query=em.createNativeQuery(sql,entityClass);
        if(params!=null&&params.size()>0){
            for(String param:params.keySet() ){
                query.setParameter(param,params.get(param));
            }
        }
        return query.getResultList();
    }

    @Override
    public PageBean queryByNativeSQLPageList(String sql, Class entityClass, Map<String,Object> params, PageForm pageForm){
        String newSql="select * from ("+sql+") limit "+pageForm.getPageSize()+","+pageForm.getCurrentPage();
        Query query=em.createNativeQuery(newSql,entityClass);
        if(params!=null&&params.size()>0){
            for(String param:params.keySet() ){
                query.setParameter(param,params.get(param));
            }
        }
        List<T> list=query.getResultList();
        long total=this.queryByNativeSQLCount(sql,params);
        PageBean pageBean=new PageBean();
        pageBean.setTotalCount(total);
        pageBean.setPageData(list);
        return pageBean;
    }
    @Override
    public PageBean queryByNativeSQLPageMap(String sql, Map<String,Object> params, PageForm pageForm){
        String newSql="select * from ("+sql+") limit "+pageForm.getPageSize()+","+pageForm.getCurrentPage();
        Query query=em.createNativeQuery(newSql);
        if(params!=null&&params.size()>0){
            for(String param:params.keySet() ){
                query.setParameter(param,params.get(param));
            }
        }
        List<Map<String,Object>> list=query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        long total=this.queryByNativeSQLCount(sql,params);
        PageBean pageBean=new PageBean();
        pageBean.setTotalCount(total);
        pageBean.setPageData(list);
        return pageBean;
    }

    public long queryByNativeSQLCount(String sql, Map<String,Object> params){
        String newSql="select count(*) from ("+sql+")";
        Query query=em.createNativeQuery(newSql);
        if(params!=null&&params.size()>0){
            for(String param:params.keySet() ){
                query.setParameter(param,params.get(param));
            }
        }
        return (long) query.getSingleResult();
    }

}

