package com.ctsi.ssdc.service.impl;

import com.ctsi.ssdc.example.BaseExample;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.repository.BaseRepository;
import com.ctsi.ssdc.service.StrengthenBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class StrengthenBaseServiceImpl<R extends BaseRepository<E, PK, X>, E, PK, X extends BaseExample> 
	implements StrengthenBaseService<E, PK, X> {
	
    @Autowired
    protected R r;
    	
    @Override
	public E insert(E e) {
        r.insert(e);
        return e;
    }

    @Override
    public E update(E e) {
        r.updateByPrimaryKeySelective(e);
        return e;
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResult<E> findAll() {
        List<E> data = r.selectByExample(null);
                
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = r.countByExample(null);
        }
        
        return new PageResult<>(data,count,count);        
        
    }

    @Override
    public E findOne(PK p) {
        return r.selectByPrimaryKey(p);
    }

    @Override
    public void delete(PK p) {
    	r.deleteByPrimaryKey(p);
    }

    @Override
    public PageResult<E> findByExample(X x, Pageable page) {
    	
    	x.buildCriteria();
        
    	long count = 0L;
    	
        if(page != null) {
        	x.setOrderByClause(x.buildOrderByClause(page));
        	PageHelper.startPage(page.getPageNumber()+1, page.getPageSize());
        }
        
        List<E> data = r.selectByExample(x);
        
        PageInfo<E> info = new PageInfo<>(data);
        count = info.getTotal();

        return new PageResult<>(data,count,count);    
        
    }

	@Override
	public PageResult<E> findByExample(X x) {
		x.buildCriteria();
        
        List<E> data = r.selectByExample(x);
        
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = data.size();
        }
        
        return new PageResult<>(data,count,count);    
	}
	
	

}
