package com.piesat.common.jpa.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-11-17 18:43
 **/
@NoRepositoryBean
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

    public EntityManager getEm() {
        return this.em;
    }

    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    public String getEntityName() {
        return this.entityName;
    }
}

