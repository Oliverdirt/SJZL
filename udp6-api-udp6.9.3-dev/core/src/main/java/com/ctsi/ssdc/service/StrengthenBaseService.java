package com.ctsi.ssdc.service;

import org.springframework.data.domain.Pageable;

import com.ctsi.ssdc.example.BaseExample;
import com.ctsi.ssdc.model.PageResult;

/**
 * base service interface
 * @author lym
 *
 * @param <E> the persisted entity
 * @param <PK> primary key type
 * @param <X> example,the query conditions are encapsulated
 */
public interface StrengthenBaseService<E, PK, X extends BaseExample> {
	
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
	
	PageResult<E> findByExample(X x);

}
