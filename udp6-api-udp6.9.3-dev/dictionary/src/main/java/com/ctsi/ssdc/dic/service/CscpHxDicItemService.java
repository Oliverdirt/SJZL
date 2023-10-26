package com.ctsi.ssdc.dic.service;

import com.ctsi.ssdc.dic.domain.CscpHxDicItem;
import com.ctsi.ssdc.dic.domain.CscpHxDicItemExample;
import com.ctsi.ssdc.dic.model.TreeDicItemSelect;
import com.ctsi.ssdc.service.StrengthenBaseService;
import java.util.List;

/**
 * Service Interface for managing CscpHxDicItem.
 *
 * @author ctsi-biyi-generator
 *
 */
public interface CscpHxDicItemService
	extends StrengthenBaseService<CscpHxDicItem, Long, CscpHxDicItemExample>{


    List<TreeDicItemSelect> buildDicItemTreeSelect(List<CscpHxDicItem> items);

    /**
     * 构建前端所需要树结构
     *
     * @param items 列表
     * @return 树结构列表
     */
    List<CscpHxDicItem> buildDicItemTree(List<CscpHxDicItem> items);

    List<CscpHxDicItem> selectByDicId(Long dicId);
    /**
     * 批量删除
     * @param itemIds
     */
    void deleteByIds(Long[] itemIds);


    /**
     * 字典项编码重复校验
     * @param item
     * @return
     */
    int checkCscpHxItemCode(CscpHxDicItem item);
    /**
     * 树形字典是否删除
     * @param itemId
     * @return
     */
    boolean checkIsTreeItemDel(Long itemId);
}
