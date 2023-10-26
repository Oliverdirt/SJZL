package com.ctsi.ssdc.dic.validator;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ：guoyanpei
 * date ：Created in 2022-02-14 9:44
 * description：校验结果枚举类
 * modified By：
 * version: version1.0.0
 */

@Getter
public enum ValidatorDicEnums {


    /**
     * 字典名称重复
     */
    DIC_NAME_REP("字典名称重复"),

    /**
     * 字典编码重复
     */
    DIC_CODE_REP("字典编码重复"),

    /**
     * 字典项编码重复
     */
    DIC_ITEM_CODE_REP("字典项编码重复") ;

    @Getter
    private String msg;

    ValidatorDicEnums(String msg) {
        this.msg = msg;
    }


}
