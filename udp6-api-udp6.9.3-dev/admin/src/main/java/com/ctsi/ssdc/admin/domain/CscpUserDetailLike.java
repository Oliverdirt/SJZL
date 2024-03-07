package com.ctsi.ssdc.admin.domain;

import java.io.Serializable;

/**
 * 模糊查询
*/
public class CscpUserDetailLike implements Serializable {

    /**
	 * default
	 */
	private static final long serialVersionUID = 1L;

	private String usernameLike;

    private String familyNameLike;

    private String nameLike;

    private String mobileLike;
    
    private String emailLike;

    private String discTitleLike;

    private String discDetailLike;

    private String deptIdEquals;

    private String roleIdEquals;

    private String dataStatusEquals;

    private String accountTypeEquals;

	private String systemIdEquals;

	public String getDeptIdEquals() {
		return deptIdEquals;
	}

	public void setDeptIdEquals(String deptIdEquals) {
		this.deptIdEquals = deptIdEquals;
	}

	public String getUsernameLike() {
		return usernameLike;
	}

	public void setUsernameLike(String usernameLike) {
		this.usernameLike = usernameLike;
	}

	public String getFamilyNameLike() {
		return familyNameLike;
	}

	public void setFamilyNameLike(String familyNameLike) {
		this.familyNameLike = familyNameLike;
	}

	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public String getMobileLike() {
		return mobileLike;
	}

	public void setMobileLike(String mobileLike) {
		this.mobileLike = mobileLike;
	}

	public String getEmailLike() {
		return emailLike;
	}

	public void setEmailLike(String emailLike) {
		this.emailLike = emailLike;
	}

	public String getDiscTitleLike() {
		return discTitleLike;
	}

	public void setDiscTitleLike(String discTitleLike) {
		this.discTitleLike = discTitleLike;
	}

	public String getDiscDetailLike() {
		return discDetailLike;
	}

	public void setDiscDetailLike(String discDetailLike) {
		this.discDetailLike = discDetailLike;
	}

	public String getRoleIdEquals() {
		return roleIdEquals;
	}

	public void setRoleIdEquals(String roleIdEquals) {
		this.roleIdEquals = roleIdEquals;
	}

	public String getDataStatusEquals() {
		return dataStatusEquals;
	}

	public void setDataStatusEquals(String dataStatusEquals) {
		this.dataStatusEquals = dataStatusEquals;
	}

	public String getAccountTypeEquals() {
		return accountTypeEquals;
	}

	public void setAccountTypeEquals(String accountTypeEquals) {
		this.accountTypeEquals = accountTypeEquals;
	}

	public String getSystemIdEquals() {
		return systemIdEquals;
	}

	public void setSystemIdEquals(String systemIdEquals) {
		this.systemIdEquals = systemIdEquals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discDetailLike == null) ? 0 : discDetailLike.hashCode());
		result = prime * result + ((discTitleLike == null) ? 0 : discTitleLike.hashCode());
		result = prime * result + ((emailLike == null) ? 0 : emailLike.hashCode());
		result = prime * result + ((familyNameLike == null) ? 0 : familyNameLike.hashCode());
		result = prime * result + ((mobileLike == null) ? 0 : mobileLike.hashCode());
		result = prime * result + ((nameLike == null) ? 0 : nameLike.hashCode());
		result = prime * result + ((usernameLike == null) ? 0 : usernameLike.hashCode());
		result = prime * result + ((deptIdEquals == null) ? 0 : deptIdEquals.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CscpUserDetailLike other = (CscpUserDetailLike) obj;
		if (discDetailLike == null) {
			if (other.discDetailLike != null) {
				return false;
			}
		} else if (!discDetailLike.equals(other.discDetailLike)) {
			return false;
		}
		if (discTitleLike == null) {
			if (other.discTitleLike != null) {
				return false;
			}
		} else if (!discTitleLike.equals(other.discTitleLike)) {
			return false;
		}
		if (emailLike == null) {
			if (other.emailLike != null) {
				return false;
			}
		} else if (!emailLike.equals(other.emailLike)) {
			return false;
		}
		if (familyNameLike == null) {
			if (other.familyNameLike != null) {
				return false;
			}
		} else if (!familyNameLike.equals(other.familyNameLike)) {
			return false;
		}
		if (mobileLike == null) {
			if (other.mobileLike != null) {
				return false;
			}
		} else if (!mobileLike.equals(other.mobileLike)) {
			return false;
		}
		if (nameLike == null) {
			if (other.nameLike != null) {
				return false;
			}
		} else if (!nameLike.equals(other.nameLike)) {
			return false;
		}
		if (usernameLike == null) {
			if (other.usernameLike != null) {
				return false;
			}
		} else if (!usernameLike.equals(other.usernameLike)) {
			return false;
		}
		if (deptIdEquals == null) {
			if (other.deptIdEquals != null) {
				return false;
			}
		} else if (!deptIdEquals.equals(other.deptIdEquals)) {
			return false;
		}
		return true;
	}
	
}