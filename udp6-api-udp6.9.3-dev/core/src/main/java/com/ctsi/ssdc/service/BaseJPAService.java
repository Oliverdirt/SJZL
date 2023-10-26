package com.ctsi.ssdc.service;

import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.example.BaseJPAExample;
import com.ctsi.ssdc.model.PageResult;

/**
 * base service interface
 * @author lym
 *
 * @param <E> the persisted entity
 * @param <PK> primary key type
 * @param <X> example,the query conditions are encapsulated
 */
public interface BaseJPAService<E, PK, X extends BaseJPAExample<E>> {
	
	/**
	 * insert
	 * @param e the entity to insert
	 * @return the persisted entity
	 */
    E insert(E e);
    
    /**
     * update
     * @param e the entity to update
     * @return
     */
    E update(E e); 
	
    /**
     * get all
     * @return the list of entities
     */
    PageResult<E> findAll();

    /**
     * get one by primary key 
     * @param p the primary key
     * @return
     */
    E findOne(PK p);

    /**
     * delete
     * @param p the primary key
     */
    void delete(PK p);

    /**
     * get by conditions
     * @param x
     * @param page
     * @return
     */
	PageResult<E> findByExample(X x, Pageable page);
	
	/**
     * get by conditions
     * @param x
     * @return
     */
	public PageResult<E> findByExample(X x);

}
