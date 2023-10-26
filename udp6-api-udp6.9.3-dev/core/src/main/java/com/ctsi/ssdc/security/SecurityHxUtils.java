package com.ctsi.ssdc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityHxUtils {

    private SecurityHxUtils() {
    }

    /**
     * Get the username of the current user.
     *
     * @return the username of the current user
     */
    public static Optional<String> getOptionalCurrentUserName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof CscpHxUserDetail) {
                CscpHxUserDetail springSecurityUser = (CscpHxUserDetail) authentication.getPrincipal();
                return springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                return (String) authentication.getPrincipal();
            }
            return null;
        });
    }

    public static Optional<String> getOptionalCurrentTenantAccount() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof CscpHxUserDetail) {
                CscpHxUserDetail springSecurityUser = (CscpHxUserDetail) authentication.getPrincipal();
                return springSecurityUser.getTenantAccount();
            } else if (authentication.getPrincipal() instanceof String) {
                return (String) authentication.getPrincipal();
            }
            return null;
        });
    }

    public static String getCurrentUserName() {
        return getOptionalCurrentUserName().map(name -> {
            return name;
        }).orElse(null);
    }
    public static String getCurrentTenantAccount() {
        return getOptionalCurrentTenantAccount().map(name -> {
            return name;
        }).orElse(null);
    }
    public static Optional<Long> getOptionalCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof CscpHxUserDetail) {
                CscpHxUserDetail springSecurityUser = (CscpHxUserDetail) authentication.getPrincipal();
                return springSecurityUser.getId();
            }
            return null;
        });
    }

    public static Optional<Long> getOptionalCurrentTenantId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof CscpHxUserDetail) {
                CscpHxUserDetail springSecurityUser = (CscpHxUserDetail) authentication.getPrincipal();
                return springSecurityUser.getTenantId();
            }
            return null;
        });
    }

    public static Long getCurrentUserId() {
        return getOptionalCurrentUserId().map(id -> {
            return id;
        }).orElse(0L);

    }
    public static Long getCurrentTenantId() {
        return getOptionalCurrentTenantId().map(id -> {
            return id;
        }).orElse(0L);

    }

    public static Optional<CscpHxUserDetail> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof CscpHxUserDetail) {
                return (CscpHxUserDetail) authentication.getPrincipal();
            }
            return null;
        });
    }

    public static Optional<Authentication> getCurrentAuthentication() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication());

    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user
     */
    public static Optional<String> getCurrentUserJwt() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream().noneMatch(
                        grantedAuthority -> grantedAuthority.getAuthority()
                                .equals(AuthoritiesConstants.ANONYMOUS)))
                .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the
     * Servlet API
     *
     * @param authority
     *            the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities()
                        .stream().anyMatch(grantedAuthority ->
                                grantedAuthority.getAuthority().equals(authority)))
                .orElse(false);
    }
}
