package com.ctsi.ssdc.admin.domain.dto;

public class CscpOrgParamDTO {
    private CscpOrgDTO[] cscpOrgDtos;
    private CscpUserOrgDTO[] cscpUserOrgDtos;
    private CscpWorkGroupOrgDTO[] cscpWorkGroupOrgDtos;
    
    private CscpUserDetailDTO[] cscpUserDetailDtos;
    private CscpWorkGroupDTO[] cscpWorkGroupDtos;

    
    public CscpOrgDTO[] getcscpOrgDtos() {
        CscpOrgDTO[] temp = cscpOrgDtos;
        return temp;
    }
    public void setcscpOrgDtos(CscpOrgDTO[] cscpOrgDtos) {
        CscpOrgDTO[] temp = cscpOrgDtos;
        this.cscpOrgDtos = temp.clone();
    }
    public CscpUserOrgDTO[] getcscpUserOrgDtos() {
        CscpUserOrgDTO[] temp = cscpUserOrgDtos;
        return temp;
    }
    public void setcscpUserOrgDtos(CscpUserOrgDTO[] cscpUserOrgDtos) {
        CscpUserOrgDTO[] temp = cscpUserOrgDtos;
        this.cscpUserOrgDtos = temp.clone();
    }
    public CscpWorkGroupOrgDTO[] getcscpWorkGroupOrgDtos() {
        CscpWorkGroupOrgDTO[] temp = cscpWorkGroupOrgDtos;
        return temp;
    }
    public void setcscpWorkGroupOrgDtos(CscpWorkGroupOrgDTO[] cscpWorkGroupOrgDtos) {
        CscpWorkGroupOrgDTO[] temp = cscpWorkGroupOrgDtos;
        this.cscpWorkGroupOrgDtos = temp.clone();
    }
    public CscpUserDetailDTO[] getcscpUserDetailDtos() {
        CscpUserDetailDTO[] temp = cscpUserDetailDtos;
        return temp;
    }
    public void setcscpUserDetailDtos(CscpUserDetailDTO[] cscpUserDetailDtos) {
        CscpUserDetailDTO[] temp = cscpUserDetailDtos;
        this.cscpUserDetailDtos = temp.clone();
    }
    public CscpWorkGroupDTO[] getcscpWorkGroupDtos() {
        CscpWorkGroupDTO[] temp = cscpWorkGroupDtos;
        return temp;
    }
    public void setcscpWorkGroupDtos(CscpWorkGroupDTO[] cscpWorkGroupDtos) {
        CscpWorkGroupDTO[] temp = cscpWorkGroupDtos;
        this.cscpWorkGroupDtos = temp.clone();
    }
    
    
}
