package com.ctsi.ssdc.dic.service.impl;

import com.ctsi.ssdc.dic.domain.CscpHxDicItem;
import com.ctsi.ssdc.dic.domain.CscpHxDicItemExample;
import com.ctsi.ssdc.dic.model.TreeDicItemSelect;
import com.ctsi.ssdc.dic.repository.CscpHxDicItemRepository;
import com.ctsi.ssdc.dic.service.CscpHxDicItemService;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CscpHxDicItem.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpHxDicItemServiceImpl
	extends StrengthenBaseServiceImpl<CscpHxDicItemRepository, CscpHxDicItem, Long, CscpHxDicItemExample>
	implements CscpHxDicItemService {

    @Autowired
    CscpHxDicItemRepository cscpHxDicItemRepository;


    @Override
    public List<TreeDicItemSelect> buildDicItemTreeSelect(List<CscpHxDicItem> items) {
        List<CscpHxDicItem> cscpHxDicItems = buildDicItemTree(items);
        return cscpHxDicItems.stream().map(TreeDicItemSelect::new).collect(Collectors.toList());
    }

    @Override
    public List<CscpHxDicItem> buildDicItemTree(List<CscpHxDicItem> items) {
        List<CscpHxDicItem> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<Long>();
        for (CscpHxDicItem item : items)
        {
            tempList.add(item.getItemId());
        }
        for (Iterator<CscpHxDicItem> iterator = items.iterator(); iterator.hasNext();)
        {
            CscpHxDicItem item = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(item.getItemParentId()))
            {
                recursionFn(items, item);
                returnList.add(item);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = items;
        }
        return returnList;
    }

    @Override
    public List<CscpHxDicItem> selectByDicId(Long dicId) {
        return cscpHxDicItemRepository.selectByDicId(dicId);
    }

    @Override
    public void deleteByIds(Long[] itemIds) {
        List<Long> delList = new ArrayList<>(Arrays.asList(itemIds));
        // 批量删除租户
        cscpHxDicItemRepository.deleteByIds(delList);
    }

    @Override
    public int checkCscpHxItemCode(CscpHxDicItem item) {
        if(item.getItemId() == null){
            return cscpHxDicItemRepository.checkCscpHxItemCode(item);
        }else{
            CscpHxDicItem cscpHxDicItem = cscpHxDicItemRepository.selectByPrimaryKey(item.getItemId());
            if(cscpHxDicItem.getItemCode().equals(item.getItemCode())){
                return 0;
            }else{
                return cscpHxDicItemRepository.checkCscpHxItemCode(item);
            }
        }
    }

    @Override
    public boolean checkIsTreeItemDel(Long itemId) {
        List<CscpHxDicItem> list = cscpHxDicItemRepository.checkIsTreeItemDel(itemId);
        return list.size()>0 ? true : false;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<CscpHxDicItem> list, CscpHxDicItem t)
    {
        // 得到子节点列表
        List<CscpHxDicItem> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CscpHxDicItem tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<CscpHxDicItem> list, CscpHxDicItem t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 得到子节点列表
     */
    private List<CscpHxDicItem> getChildList(List<CscpHxDicItem> list, CscpHxDicItem t)
    {
        List<CscpHxDicItem> tlist = new ArrayList<>();
        Iterator<CscpHxDicItem> it = list.iterator();
        while (it.hasNext())
        {
            CscpHxDicItem n =  it.next();
            if ((n.getItemParentId()!=null) && n.getItemParentId().longValue() == t.getItemId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }
}
