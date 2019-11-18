package com.piesat.common.jpa.service;

import com.piesat.common.jpa.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-11-17 18:46
 **/
//@Transactional(readOnly = true)
public abstract class GenericServiceImpl<T, ID extends Serializable> implements GenericService<T, ID>
{
    protected Logger logger;

    public GenericServiceImpl() {
        this.logger = LoggerFactory.getLogger((Class)this.getClass());
    }

    public abstract GenericDao<T, ID> getGenericDao();

    @Override
    public Class<T> getEntityClass() {
        final Type genType = this.getClass().getGenericSuperclass();
        final Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<T>)params[0];
    }

    @Override
    public Page<T> getPage(final Specification<T> specification, final PageRequest pageRequest, final T entity) {
        return (Page<T>)this.getGenericDao().findAll(specification,pageRequest);
    }

    @Override
    public Page<T> getPage(final Specification<T> specification, final PageRequest pageRequest) {
        return this.getGenericDao().findAll(specification,pageRequest);
    }

    @Override
    public T getById(final ID id) {
        return (T)this.getGenericDao().findById(id);
    }

    @Override
    public T getBySpecification(final Specification<T> specification) {
        return (T)this.getGenericDao().findOne(specification);
    }

    @Override
    public boolean exists(final ID id) {
        return this.getGenericDao().existsById(id);
    }

    @Override
    public long count() {
        return this.getGenericDao().count();
    }

    @Override
    public long count(final Specification<T> specification) {
        return this.getGenericDao().count(specification);
    }

    @Override
    public List<T> getAll() {
        return (List<T>)this.getGenericDao().findAll();
    }

    @Override
    public List<T> getAll(final Specification<T> specification) {
        return (List<T>)this.getGenericDao().findAll(specification);
    }

    @Override
    public Page<T> getAll(final Pageable pageable) {
        return this.getGenericDao().findAll(pageable);
    }

    @Override
    public List<T> getAll(final Sort sort) {
        return this.getGenericDao().findAll(sort);
    }

    @Override
    public List<T> getAll(final Specification<T> specification, final Sort sort) {
        return this.getGenericDao().findAll(specification, sort);
    }

    @Transactional(readOnly = false)
    @Override
    public T save(final T entity) {
        return this.getGenericDao().save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public List<T> save(final Iterable<T> entities) {
        return this.getGenericDao().saveAll(entities);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(final ID id) {
        this.getGenericDao().delete((T) id);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(final T entity) {
        this.getGenericDao().delete(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(final Iterable<T> entities) {
        this.getGenericDao().delete((T) entities);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteByIds(final Iterable<ID> ids) {
        for (final ID id : ids) {
            this.delete(id);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteInBatch(final Iterable<T> entities) {
        this.getGenericDao().deleteInBatch(entities);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteAll() {
        this.getGenericDao().deleteAll();
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteAllInBatch() {
        this.getGenericDao().deleteAllInBatch();
    }
}


