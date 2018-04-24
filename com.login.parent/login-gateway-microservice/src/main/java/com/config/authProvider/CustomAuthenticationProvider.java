package com.config.authProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.config.dto.AccountDetail;
import com.config.excuter.AuthenticationCommonExcuter;


@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	/**
	 * 我们要在CustomAuthenticationProvider中完成下面几项工作：
	 * 
	 * 1.根据用户提供的用户名从数据库中获取该用户名所对应的账户信息
	 * 
	 * 2.userDetail和前端传来的token
	 */
	
	//注入UserService
	@Autowired
	private  UserDetailsService    userDetailsServiceImpl;
	@Autowired
	private  AuthenticationCommonExcuter   authExcuter;
	
	
	
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token)
			throws AuthenticationException {
		//just do nothing
         return ;
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token)
			throws AuthenticationException {
		// 获取用户信息
		AccountDetail details = (AccountDetail) userDetailsServiceImpl.loadUserByUsername(username);
		//调用verifyExcuter完成用户名和密码的校验
		authExcuter.verify(details, token);
		return details;
	}

}
