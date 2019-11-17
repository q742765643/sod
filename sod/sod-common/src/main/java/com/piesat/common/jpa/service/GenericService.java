package com.piesat.common.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzj on 2019/11/17.
 */
public interface GenericService<T, ID extends Serializable>{
    public Class<T> getEntityClass();
    public Page<T> getPage(final Specification<T> specification, final PageRequest pageRequest, final T entity);
    public Page<T> getPage(final Specification<T> specification, final PageRequest pageRequest);
    public T getById(final ID id);
    public T getBySpecification(final Specification<T> specification);
    public boolean exists(final ID id);
    public long count();
    public long count(final Specification<T> specification);
    public List<T> getAll();
    public List<T> getAll(final Specification<T> specification);
    public Page<T> getAll(final Pageable pageable);
    public List<T> getAll(final Sort sort);
    public List<T> getAll(final Specification<T> specification, final Sort sort);
    public T save(final T entity);
    public List<T> save(final Iterable<T> entities);
    public void delete(final ID id);
    public void delete(final T entity);
    public void delete(final Iterable<T> entities);
    public void deleteByIds(final Iterable<ID> ids);
    public void deleteInBatch(final Iterable<T> entities);
    public void deleteAll();
    public void deleteAllInBatch();
}
