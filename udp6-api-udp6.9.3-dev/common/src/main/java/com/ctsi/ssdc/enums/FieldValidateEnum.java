package com.ctsi.ssdc.enums;

public enum FieldValidateEnum {
    /**
     * 手机号
     */
    CELL_PHONE_NUMBER("cell_phone_number","^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\\\d{8}$","手机号校验出错"),
    /**
     * 国内电话
     */
    DOMESTIC_TELEPHONE_NUMBER("domestic_telephone_number","^((\\\\d{3,4}-)|\\\\d{3.4}-)?\\\\d{7,8}$ ","国内电话号校验出错"),
    /**
     * 邮箱
     */
    MAIL("mail","^\\\\w+([-+.]\\\\w+)*@\\\\w+([-.]\\\\w+)*\\\\.\\\\w+([-.]\\\\w+)*$","邮件校验出错"),
    /**
     * 网站
     */
    WEBSITE("website","[a-zA-z]+://[^\\s]* 或 ^http://([\\\\w-]+\\\\.)+[\\\\w-]+(/[\\\\w-./?%&=]*)?$","网址号校验出错"),
    /**
     * MAC地址
     */
    MAC_ADDRESS("mac_address","^([0-9a-fA-F]{2})(([/\\\\s:-][0-9a-fA-F]{2}){5})","MAC地址校验出错"),
    /**
     * 纯数字
     */
    PURE_NUMBERS("pure_numbers","^[0-9]*$","纯数字校验出错"),
    /**
     * 纯字母
     */
    PLAIN_LETTERS("plain_letters","^[a-zA-Z]*$","纯字母校验出错"),
    /**
     * 英文数字
     */
    ENGLISH_AND_NUMBER("english_and_number","^[A-Za-z0-9]+$","英文、数字"),
    /**
     * 英文、数字、或者下划线
     */
    ENGLISH_NUMBER_UNDERLINE("english_number_underline","^\\\\w+$","英文、数字、或者下划线")
    ;
    private String ruleCode;
    private String rulevalue;
    private String ruleinfo;

    FieldValidateEnum(String ruleCode, String rulevalue, String ruleinfo) {
        this.ruleCode = ruleCode;
        this.rulevalue = rulevalue;
        this.ruleinfo = ruleinfo;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public String getRulevalue() {
        return rulevalue;
    }

    public String getRuleinfo() {
        return ruleinfo;
    }

    public static FieldValidateEnum getByRuleCode(String ruleCode){
        for (FieldValidateEnum fieldValidateEnum : FieldValidateEnum.values()){
            if (fieldValidateEnum.ruleCode.equals(ruleCode)){
                return fieldValidateEnum;
            }
        }
        return null;
    }
}


