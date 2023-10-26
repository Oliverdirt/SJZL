package com.ctsi.ssdc.util;


import com.ctsi.ssdc.model.PageResult;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class ListPageUtil {

    /**
     *
     * @param pageSize  当前页面大小
     * @param pageIndex  当前页码
     * @param list  需要分页的集合
     * @return
     */
    public static PageResult pager(int pageSize, int pageIndex, List list){
        //使用list 中的sublist方法分页
        List<T> dataList=null;
        // 每页显示多少条记录

        //当前第几页数据
        int currentPage;

        // 一共多少条记录
        int totalRecord = list.size();

        // 一共多少页
        int totalPage = totalRecord % pageSize;
        if (totalPage > 0) {
            totalPage = totalRecord / pageSize + 1;
        } else {
            totalPage = totalRecord / pageSize;
        }

        // 当前第几页数据
        currentPage = totalPage < pageIndex+1 ? totalPage : pageIndex+1;

        // 起始索引
        int fromIndex = pageSize * (currentPage - 1);

        // 结束索引
        int toIndex = pageSize * currentPage > totalRecord ? totalRecord : pageSize * currentPage;

        dataList = list.subList(fromIndex, toIndex);

        PageResult pageResult=new PageResult();
        pageResult.setRecordsTotal(totalRecord);
        pageResult.setData(dataList);
        return pageResult;
    }


}
