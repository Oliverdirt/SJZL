package com.ctsi.ssdc.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * 低代码接口权限控制map
 * @author wbb
 */
public enum LowCodeApiPermisionEnum {
    //新增
    LOWCODE_ADD("cscp.lowcode.add","新增","/api/lowcode/customize/template/add","POST","button",1),
    //删除
    LOWCOE_DELETE("cscp.lowcode.delete","删除","/api/lowcode/customize/template/delByPk","DELETE","button",2),
    //批量删除
    LOWCODE_DELETE_BATCH("cscp.lowcode.batchdelete","批量删除","/api/lowcode/customize/template/delByPks","DELETE","button",3),
    //修改
    LOWCODE_UPDATE("cscp.lowcode.update","编辑","/api/lowcode/customize/template/update","PUT","button",4),
    //查询
    LOWCODE_QUERY("cscp.lowcode.query","列表查询","/api/lowcode/customize/template/query","GET","button",5),
    //根据id查询
    LOWCODE_QUERY_BYID("cscp.lowcode.query","详细查询","/api/lowcode/customize/template/queryByPk","GET","button",6);

    private String permisionCode;
    private String name;
    private String url;
    private String type;
    private Integer order;
    private String httpMethod;

    LowCodeApiPermisionEnum(String permisionCode,
                            String name,
                            String url,
                            String httpMethod,
                            String type,
                            Integer order){
        this.permisionCode = permisionCode;
        this.name = name;
        this.url = url;
        this.httpMethod = httpMethod;
        this.type = type;
        this.order = order;
    }

    public String getPermisionCode() {
        return permisionCode;
    }

    public void setPermisionCode(String permisionCode) {
        this.permisionCode = permisionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * 权限码和url map
     * @return 权限码和url map
     */
    public static Map<String,String> toUrlMap(){
        Map<String,String> urlMap = new HashMap<>();
        for(LowCodeApiPermisionEnum l : LowCodeApiPermisionEnum.values()){
            urlMap.put(l.getPermisionCode(),l.getUrl());
        }
        return urlMap;
    }

    /**
     * 权限码和排序map
     * @return 权限码和排序map
     */
    public static Map<String,Integer> toOrderMap(){
        Map<String,Integer> orderMap = new HashMap<>();
        for(LowCodeApiPermisionEnum l : LowCodeApiPermisionEnum.values()){
            orderMap.put(l.getPermisionCode(),l.getOrder());
        }
        return orderMap;
    }

    /**
     * 权限码和url map
     * @return 权限码和url map
     */
    public static Map<String,String> toNameMap(){
        Map<String,String> nameMap = new HashMap<>();
        for(LowCodeApiPermisionEnum l : LowCodeApiPermisionEnum.values()){
            nameMap.put(l.getPermisionCode(),l.getName());
        }
        return nameMap;
    }

    /**
     * 获取枚举属性值
     * @return 属性列表
     */
    public static LowCodeApiPermisionEnum[] getLowCodeApiPermisionEnumArr(){
        return LowCodeApiPermisionEnum.values();
    }
}
