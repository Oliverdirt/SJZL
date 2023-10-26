package com.ctsi.ssdc.service.impl;

import com.ctsi.ssdc.example.BasePageableExample;
import com.ctsi.ssdc.model.PageResult;
import com.ctsi.ssdc.repository.BaseRepository;
import com.ctsi.ssdc.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BaseServiceImpl<E, PK, X extends BasePageableExample> implements BaseService<E, PK, X> {
	
    @Autowired
    private BaseRepository<E, PK, X> baseRepository;
    	
    @Override
	public E insert(E e) {
        baseRepository.insert(e);
        return e;
    }

    @Override
    public E update(E e) {
        baseRepository.updateByPrimaryKeySelective(e);
        return e;
    }
    
    @Override
    public PageResult<E> findAll() {
        List<E> data = baseRepository.selectByExample(null);
                
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = baseRepository.countByExample(null);
        }
        
        return new PageResult<>(data,count,count);        
        
    }

    @Override
    public E findOne(PK p) {
        return baseRepository.selectByPrimaryKey(p);
    }

    @Override
    public void delete(PK p) {
    	baseRepository.deleteByPrimaryKey(p);
    }

    @Override
    public PageResult<E> findByExample(X x, Pageable page) {
    	
    	x.buildCriteria();
        
    	long count = 0L;
    	
        if(page != null) {
        	 x.setPageable(page);
        	 PageHelper.startPage(page.getPageNumber()+1, page.getPageSize());
        	 
        }
        
        List<E> data = baseRepository.selectByExample(x);
        
        PageInfo<E> info = new PageInfo<>(data);
        count = info.getTotal();

        return new PageResult<>(data,count,count);    
        
    }

	@Override
	public PageResult<E> findByExample(X x) {
		x.buildCriteria();
        
        List<E> data = baseRepository.selectByExample(x);
        
        long count = 0L;
        
        if(CollectionUtils.isNotEmpty(data)) {
            count = data.size();
        }
        
        return new PageResult<>(data,count,count);    
	}

}
