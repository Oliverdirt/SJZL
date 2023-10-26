package com.ctsi.ssdc.service.impl;

import com.ctsi.ssdc.example.BaseJPAExample;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.service.BaseJPAService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public class BaseJPAServiceImpl<E, PK, X extends BaseJPAExample<E>> implements BaseJPAService<E, PK, X> {
	
    @Autowired
    protected JpaRepository<E, PK> jpaRepository;
    
    @Autowired
    protected JpaSpecificationExecutor<E> jpaSpecificationExecutor;
    
    	
    @Override
	public E insert(E e) {
    	jpaRepository.save(e);
        return e;
    }

    @Override
    public E update(E e) {
    	jpaRepository.save(e);
        return e;
    }
    
    @Override
    public PageResult<E> findAll() {
        List<E> data = jpaRepository.findAll();
                
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = jpaRepository.count();
        }
        
        return new PageResult<>(data,count,count);        
        
    }

    @Override
    public E findOne(PK p) {
        return jpaRepository.getOne(p);
    }

    @Override
    public void delete(PK p) {
    	jpaRepository.deleteById(p);
    }
    @Override
    public PageResult<E> findByExample(X x, Pageable page) {
    	
    	Specification<E> spec = x.createSpecification();
    	
    	Page<E> data = jpaSpecificationExecutor.findAll(spec, page);
        
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data.getContent())) {
            count = jpaSpecificationExecutor.count(spec);
        }
        
        return new PageResult<>(data.getContent(),count,count);    
        
    }
    @Override
    public PageResult<E> findByExample(X x) {
    	
    	Specification<E> spec = x.createSpecification();
    	
    	List<E> data = jpaSpecificationExecutor.findAll(spec);
        
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = jpaSpecificationExecutor.count(spec);
        }
        
        return new PageResult<>(data,count,count);    
        
    }

}
