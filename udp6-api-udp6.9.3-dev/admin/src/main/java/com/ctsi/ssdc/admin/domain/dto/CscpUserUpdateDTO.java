package com.ctsi.ssdc.admin.domain.dto;/*
 *   @Description
 *   @Author dpin
 *   @version v1.0.0
 *   @Date 2019/8/7
 *   @Copyright

 */

import java.io.Serializable;

public class CscpUserUpdateDTO implements Serializable {
    private CscpUserDetailDTO cscpUserDetailDTO;
    private String currentUserPwd;
    private String newPassword;
    
    public CscpUserDetailDTO getCscpUserDetailDTO() {
        return cscpUserDetailDTO;
    }

    public void setCscpUserDetailDTO(CscpUserDetailDTO cscpUserDetailDTO) {
        this.cscpUserDetailDTO = cscpUserDetailDTO;
    }

    public String getCurrentUserPwd() {
        return currentUserPwd;
    }

    public void setCurrentUserPwd(String currentUserPwd) {
        this.currentUserPwd = currentUserPwd;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
