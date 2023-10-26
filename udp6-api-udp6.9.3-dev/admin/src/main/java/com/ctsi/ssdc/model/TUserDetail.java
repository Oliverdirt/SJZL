package com.ctsi.ssdc.model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * TUserDetail entity. @author MyEclipse Persistence Tools
 */
//@Deprecated
public class TUserDetail implements java.io.Serializable {

    // Fields

    /**
	 * 
	 */
	private static final long serialVersionUID = 3606970191524226871L;
	
	private Long id;
    private Long userId;
    private String familyName;
    private String name;
    private String mobile;
    private String email;
    private String imgPath;
    private ZonedDateTime lastLogin;
    private String discTitle;
    private String discDetail;
    private ZonedDateTime registerTime;

    // Constructors

    /** default constructor */
    public TUserDetail() {}

    /** full constructor */
    public TUserDetail(Long userId, String familyName, String name, String mobile, String email,
            String imgPath, ZonedDateTime lastLogin, String discTitle, String discDetail,
                       ZonedDateTime registerTime) {
        this.userId = userId;
        this.familyName = familyName;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.imgPath = imgPath;
        this.lastLogin = lastLogin;
        this.discTitle = discTitle;
        this.discDetail = discDetail;
        this.registerTime = registerTime;
    }

    // Property accessors
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public ZonedDateTime getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(ZonedDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getDiscTitle() {
        return this.discTitle;
    }

    public void setDiscTitle(String discTitle) {
        this.discTitle = discTitle;
    }

    public String getDiscDetail() {
        return this.discDetail;
    }

    public void setDiscDetail(String discDetail) {
        this.discDetail = discDetail;
    }

    public ZonedDateTime getRegisterTime() {
        return this.registerTime;
    }

    public void setRegisterTime(ZonedDateTime registerTime) {
        this.registerTime = registerTime;
    }

}
