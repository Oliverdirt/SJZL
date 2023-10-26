package com.ctsi.ssdc.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    //public static final String ADMIN = "ROLE_ADMIN";

    //public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    
    public static final String AUTHORITY_ALL = "cscp.auth.all";
    
    public static final String USER_QUERY = "cscp.user.query";
    public static final String USER_ADD = "cscp.user.add";
    public static final String USER_DEL = "cscp.user.del";
    public static final String USER_EDIT = "cscp.user.edit";
    public static final String USER_DEBLOCKING = "cscp.user.deblocking";
    
    public static final String ROLE_QUERY = "cscp.role.query";
    public static final String ROLE_ADD = "cscp.role.add";
    public static final String ROLE_DEL = "cscp.role.del";
    public static final String ROLE_EDIT = "cscp.role.edit";
    
    public static final String WG_QUERY = "cscp.wg.query";
    public static final String WG_ADD = "cscp.wg.add";
    public static final String WG_DEL = "cscp.wg.del";
    public static final String WG_EDIT = "cscp.wg.edit";
    
    public static final String ORG_QUERY = "cscp.org.query";
    public static final String ORG_ADD = "cscp.org.add";
    public static final String ORG_DEL = "cscp.org.del";
    public static final String ORG_EDIT = "cscp.org.edit";

    public static final String SR = "cscp.sr";
    
    public static final String LOGGING_LOGIN = "cscp.logging.login";
    public static final String LOGGING_OPERATION = "cscp.logging.operation";

    private AuthoritiesConstants() {
    }
}
