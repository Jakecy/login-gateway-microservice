package com.config.excuter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.config.dto.AccountDetail;

public interface AuthenticationCommonExcuter {
	public void verify(AccountDetail accountDetail, UsernamePasswordAuthenticationToken token) ;
}
