package com.ctsi.ssdc.admin.domain;/*
 *   @Description
 *   @Author dpin
 *   @version v1.0.0
 *   @Date 2020/1/14
 *   @Copyright

 */

public class CscpOrgParam {
    private CscpOrg[] cscpOrg;
    private CscpUserOrg[] cscpUserOrg;
    private CscpWorkGroup[] cscpWorkGroup;
    private CscpWorkGroupOrg[] cscpWorkGroupOrg;
    private CscpUserDetail[] cscpUserDetail;

    public CscpOrg[] getCscpOrg() {
        CscpOrg[] temp = cscpOrg;
        return temp;
    }

    public void setCscpOrg(CscpOrg[] cscpOrg) {
        CscpOrg[] temp = cscpOrg;
        this.cscpOrg = temp.clone();
    }

    public CscpUserOrg[] getCscpUserOrg() {
        CscpUserOrg[] temp = cscpUserOrg;
        return temp;
    }

    public void setCscpUserOrg(CscpUserOrg[] cscpUserOrg) {
        CscpUserOrg[] temp = cscpUserOrg;
        this.cscpUserOrg = temp.clone();
    }

    public CscpWorkGroup[] getCscpWorkGroup() {
        CscpWorkGroup[] temp = cscpWorkGroup;
        return temp;
    }

    public void setCscpWorkGroup(CscpWorkGroup[] cscpWorkGroup) {
        CscpWorkGroup[] temp = cscpWorkGroup;
        this.cscpWorkGroup = temp.clone();
    }

    public CscpWorkGroupOrg[] getCscpWorkGroupOrg() {
        CscpWorkGroupOrg[] temp = cscpWorkGroupOrg;
        return temp;
    }

    public void setCscpWorkGroupOrg(CscpWorkGroupOrg[] cscpWorkGroupOrg) {
        CscpWorkGroupOrg[] temp = cscpWorkGroupOrg;
        this.cscpWorkGroupOrg = temp.clone();
    }

    public CscpUserDetail[] getCscpUserDetail() {
        CscpUserDetail[] temp = cscpUserDetail;
        return temp;
    }

    public void setCscpUserDetail(CscpUserDetail[] cscpUserDetail) {
        CscpUserDetail[] temp = cscpUserDetail;
        this.cscpUserDetail = temp.clone();
    }
}
