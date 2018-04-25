package com.jakecy.config.excuter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.jakecy.config.dto.AccountDetail;


public interface AuthenticationCommonExcuter {
	public void verify(AccountDetail accountDetail, UsernamePasswordAuthenticationToken token) ;
}
