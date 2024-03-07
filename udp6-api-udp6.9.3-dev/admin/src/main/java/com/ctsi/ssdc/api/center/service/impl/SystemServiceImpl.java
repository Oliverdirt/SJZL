package com.ctsi.ssdc.api.center.service.impl;

import com.ctsi.ssdc.api.center.domain.dto.DeptDTO;
import com.ctsi.ssdc.api.center.domain.dto.DicInfoDTO;
import com.ctsi.ssdc.api.center.domain.dto.SystemDictDTO;
import com.ctsi.ssdc.api.center.mapper.SystemDao;
import com.ctsi.ssdc.api.center.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author Len
 * @Date 2023/6/6 15:07
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemDao systemDao;

    @Override
    public List<SystemDictDTO> getSystemDictInfo() {
        return systemDao.getSystemDictInfo();
    }

    @Override
    public List<DeptDTO> getDeptInfo() {
        List<DeptDTO> dtoList = systemDao.getDeptInfo();
        List<DeptDTO> deptTree = buildMenuTree(dtoList);
        return deptTree;
    }

    @Override
    public List<DicInfoDTO> getDicInfoByDicType(String dicType) {
        return systemDao.getDicInfoByDicType(dicType);
    }

    public List<DeptDTO> buildMenuTree(List<DeptDTO> deptInfos) {
        List<DeptDTO> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (DeptDTO deptInfo : deptInfos) {
            tempList.add(deptInfo.getId());
        }
        for (Iterator<DeptDTO> iterator = deptInfos.iterator(); iterator.hasNext(); ) {
            DeptDTO deptInfo = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(deptInfo.getParentId())) {
                recursionFn(deptInfos, deptInfo);
                returnList.add(deptInfo);
            }
        }
        if (returnList.isEmpty()) {
            returnList = deptInfos;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<DeptDTO> list, DeptDTO t) {
        // 得到子节点列表
        List<DeptDTO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DeptDTO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<DeptDTO> list, DeptDTO t) {
        return !getChildList(list, t).isEmpty();
    }

    /**
     * 得到子节点列表
     */
    private List<DeptDTO> getChildList(List<DeptDTO> list, DeptDTO t) {
        List<DeptDTO> tlist = new ArrayList<>();
        Iterator<DeptDTO> it = list.iterator();
        while (it.hasNext()) {
            DeptDTO n = it.next();
            if ((n.getParentId() != null) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

}
