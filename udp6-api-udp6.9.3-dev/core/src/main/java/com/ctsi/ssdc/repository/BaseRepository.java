package com.ctsi.ssdc.repository;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public abstract interface BaseRepository<E, P, X> {
	 int insert(E e);
	 
	 int insertSelective(E e);

	 int updateByPrimaryKeySelective(E e);
	    
	 int updateByPrimaryKey(E e);
	 
	 long countByExample(X x);

	 int deleteByExample(X x);

	 int deleteByPrimaryKey(P p);

	 List<E> selectByExample(X x);

	 E selectByPrimaryKey(P p);

	 int updateByExampleSelective(@Param("record") E record, @Param("example") X example);

	 int updateByExample(@Param("record") E record, @Param("example") X example);
}
