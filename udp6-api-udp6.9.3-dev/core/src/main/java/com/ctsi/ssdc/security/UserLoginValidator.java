package com.ctsi.ssdc.security;

import com.ctsi.ssdc.model.UserForm;

public interface UserLoginValidator {

	void validate(UserForm user);
	
}
