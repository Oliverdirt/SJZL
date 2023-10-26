package com.ctsi.ssdc.admin.constants;

/**
 * 租户状态
 */
public class TenantStatusConstant {

    // 0:无租户
    public final static int TENANT_NULL=0;

    // 1:租户存在
    public final static int TENANT_EXIST=1;

    // 2:过期
    public final static int TENANT_EXPIRED=2;

    // 3:该租户下，用户不存在
    public final static int USER_IN_TENANT_NOT_EXIST=3;

    // 租户未激活状态:"0"
    public final static String NOT_ACTIVE_FLAG = "0";

    // 租户激活状态:"1"
    public final static String ACTIVE_FLAG = "1";


}
