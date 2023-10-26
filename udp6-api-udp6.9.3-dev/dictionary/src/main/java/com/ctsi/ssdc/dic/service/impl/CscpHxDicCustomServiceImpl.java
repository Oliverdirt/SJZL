package com.ctsi.ssdc.dic.service.impl;


import com.ctsi.ssdc.criteria.LongCriteria;
import com.ctsi.ssdc.dic.consts.CscpDicAttrType;
import com.ctsi.ssdc.dic.domain.*;
import com.ctsi.ssdc.dic.model.DicItemModel;
import com.ctsi.ssdc.dic.repository.CscpHxDicItemRepository;
import com.ctsi.ssdc.dic.repository.CscpHxDicRepository;
import com.ctsi.ssdc.exception.BadRequestAlertException;
import com.ctsi.ssdc.util.SpringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import com.ctsi.ssdc.dic.service.CscpHxDicCustomService;
import com.ctsi.ssdc.dic.repository.CscpHxDicCustomRepository;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import com.ctsi.ssdc.service.impl.StrengthenBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import com.ctsi.ssdc.model.PageResult;
import org.springframework.data.domain.Pageable;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CscpHxDicCustom.
 *
 * @author ctsi-biyi-generator
 *
 */
@Service
public class CscpHxDicCustomServiceImpl 
	extends StrengthenBaseServiceImpl<CscpHxDicCustomRepository, CscpHxDicCustom, Long, CscpHxDicCustomExample> 
	implements CscpHxDicCustomService {

    @Autowired
    CscpHxDicCustomRepository cscpHxDicCustomRepository;

    @Autowired
    CscpHxDicRepository cscpHxDicRepository;

    @Autowired
    CscpHxDicItemRepository cscpHxDicItemRepository;




    /**
    * GET  /cscpHxDicCustoms : get the cscpHxDicCustoms firstStringBaseColumn.
    */
    @Override
    public PageResult<CscpHxDicCustom> findFirstStringColumn(String dicName,Pageable pageable){
        String str = dicName;
        if (Objects.nonNull(pageable)) {
            PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());
        }
        CscpHxDicCustomExample cscpHxDicCustomExample = new CscpHxDicCustomExample();
        String orderBy = getPageOrderBy(pageable);
        if(StringUtils.isNotEmpty(orderBy)) {
            cscpHxDicCustomExample.setOrderByClause(orderBy);
        }
        if (StringUtils.isEmpty(str)){
            cscpHxDicCustomExample.createCriteria();
        } else{
            cscpHxDicCustomExample.createCriteria().andDicNameLike("%" + str +"%");
        }
        List<CscpHxDicCustom>  data = cscpHxDicCustomRepository.selectByExample(cscpHxDicCustomExample);

        long count = 0L;
        if (CollectionUtils.isNotEmpty(data))
        {
            count = cscpHxDicCustomRepository.countByExample(cscpHxDicCustomExample);
        }
        return new PageResult<CscpHxDicCustom>(data,count,count);
    }
    private String getPageOrderBy(Pageable page) {
        if(page!= null && page.getSort() != null) {
            StringBuilder sb = new StringBuilder();
            page.getSort().forEach(sort -> sb.append(sort.getProperty())
            .append(" ").append(sort.getDirection()).append(","));
            if(sb.length() > 1) {
                return (sb.substring(0,sb.length()-1));
             }
        }
        return null;
    }

    /**
     * 新增自定义字典
     * @param cscpHxDicCustom
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CscpHxDicCustom insert(CscpHxDicCustom cscpHxDicCustom) {
        CscpHxDic cscpHxDic = new CscpHxDic();

        cscpHxDic.setDicName(cscpHxDicCustom.getDicName());
        cscpHxDic.setDicCode(cscpHxDicCustom.getDicCode());
        cscpHxDic.setDicAttr(CscpDicAttrType.CUSTOM);
        // 查询是否已存在，若存在，提示错误
        int count1 = cscpHxDicRepository.checkCscpHxDicName(cscpHxDic);
        int count2 = cscpHxDicRepository.checkCscpHxDicCode(cscpHxDic);
        if(count1 >= 1 || count2 >=1){
            throw new BadRequestAlertException("新增自定义字典失败:字典已存在","CscpHxDicCustomServiceImpl.insert","");
        }
        // 判断bean,method是否存在
        String beanName = cscpHxDicCustom.getDicClass();
        String methodName = cscpHxDicCustom.getDicMethod();
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        boolean b = applicationContext.containsBean(beanName);
        if (!b){
            throw new BadRequestAlertException(beanName + " 类不存在","CscpHxDicCustomServiceImpl.insert","");
        }
        Object bean = SpringUtil.getBean(beanName);
        Method method = null;
        try {
            method = bean.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new BadRequestAlertException(methodName + " 方法不存在","CscpHxDicCustomServiceImpl.insert","");
        }

        try {
            // 插入自定义字典表
            cscpHxDicCustomRepository.insert(cscpHxDicCustom);
            // 插入字典类型表
            cscpHxDic.setDicId(cscpHxDicCustom.getDicId());
            cscpHxDic.setDicAttr(CscpDicAttrType.CUSTOM);
            cscpHxDic.setDicSort(cscpHxDicCustom.getDicSort());
            cscpHxDic.setDescription(cscpHxDicCustom.getDescription());
            cscpHxDicRepository.insert(cscpHxDic);
            // 执行自定义字典的方法，构建字典值。
            DicItemModel dicItemModel = (DicItemModel)  method.invoke(bean);
            List<CscpHxDicItem> cscpHxDicItems = dicItemModel.getCscpHxDicItems();
            // 插入字典项值表
            for (int i = 0; i < cscpHxDicItems.size(); i++) {
                CscpHxDicItem cscpHxDicItem = cscpHxDicItems.get(i);
                cscpHxDicItem.setDicId(cscpHxDicCustom.getDicId());
                cscpHxDicItem.setItemSort(i+1);
                cscpHxDicItemRepository.insert(cscpHxDicItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException("新增自定义字典失败","CscpHxDicCustomServiceImpl.insert","");
        }
        return cscpHxDicCustom;
    }

    /**
     * 修改自定义字典
     * @param cscpHxDicCustom
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CscpHxDicCustom update(CscpHxDicCustom cscpHxDicCustom) {

        // 判断方法或者类名是否有更改，执行自定义字典的方法，构建字典值。
        String beanName = cscpHxDicCustom.getDicClass();
        String methodName = cscpHxDicCustom.getDicMethod();
        CscpHxDicCustom selectDic = cscpHxDicCustomRepository.selectByPrimaryKey(cscpHxDicCustom.getDicId());
        Method method = null;
        Object bean = null;
        boolean checkResult = selectDic != null && (!beanName.equalsIgnoreCase(selectDic.getDicClass()) || !methodName.equals(selectDic.getDicMethod()));
        if (checkResult){
            // 判断bean是否存在
            ApplicationContext applicationContext = SpringUtil.getApplicationContext();
            boolean b = applicationContext.containsBean(beanName);
            if (!b){
                throw new BadRequestAlertException(beanName + " 类不存在","CscpHxDicCustomServiceImpl.insert","");
            }
            bean = SpringUtil.getBean(beanName);
            try {
                method = bean.getClass().getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                throw new BadRequestAlertException(methodName + " 方法不存在","CscpHxDicCustomServiceImpl.insert","");
            }
        }

        try {
            // 插入自定义字典表
            cscpHxDicCustomRepository.updateByPrimaryKeySelective(cscpHxDicCustom);
            // 插入字典类型表
            CscpHxDic cscpHxDic = new CscpHxDic();
            cscpHxDic.setDicName(cscpHxDicCustom.getDicName());
            cscpHxDic.setDicCode(cscpHxDicCustom.getDicCode());
            cscpHxDic.setDicId(cscpHxDicCustom.getDicId());
            cscpHxDic.setDicAttr(CscpDicAttrType.CUSTOM);
            cscpHxDic.setDicSort(cscpHxDicCustom.getDicSort());
            cscpHxDic.setDescription(cscpHxDicCustom.getDescription());
            cscpHxDicRepository.updateByPrimaryKeySelective(cscpHxDic);

            if (checkResult && method != null && bean != null){
                // 删除原先items
                CscpHxDicItemExample cscpHxDicItemExample = new CscpHxDicItemExample();
                LongCriteria longCriteria = new LongCriteria();
                longCriteria.setEquals(cscpHxDicCustom.getDicId());
                cscpHxDicItemExample.setDicId(longCriteria);
                cscpHxDicItemRepository.deleteByExample(cscpHxDicItemExample);
                DicItemModel dicItemModel = (DicItemModel)  method.invoke(bean);
                List<CscpHxDicItem> cscpHxDicItems = dicItemModel.getCscpHxDicItems();
                // 插入字典项值表
                for (int i = 0; i < cscpHxDicItems.size(); i++) {
                    CscpHxDicItem cscpHxDicItem = cscpHxDicItems.get(i);
                    cscpHxDicItem.setDicId(cscpHxDicCustom.getDicId());
                    cscpHxDicItem.setItemSort(i+1);
                    cscpHxDicItemRepository.insert(cscpHxDicItem);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException("修改自定义字典失败","CscpHxDicCustomServiceImpl.update","");
        }
        return cscpHxDicCustom;
    }

    /**
     * 删除自定义字典
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        try {
            // 删除自定义字典表
            cscpHxDicCustomRepository.deleteByPrimaryKey(id);
            // 删除字典类型表
            cscpHxDicRepository.deleteByPrimaryKey(id);
            // 删除字典项值表

        } catch (Exception e) {
            throw new BadRequestAlertException("删除自定义字典失败","CscpHxDicCustomServiceImpl.delete","");
        }

    }
}
