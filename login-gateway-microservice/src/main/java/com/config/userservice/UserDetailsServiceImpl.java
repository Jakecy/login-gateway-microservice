package com.config.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.config.dto.AccountDetail;
import com.config.excuter.AuthenticationCommonExcuter;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LoginService    loginServiceImpl;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//加载用户信息	
		AccountDetail accountDetail = loginServiceImpl.getAccountDetail(username);
		return new AccountDetail(username, accountDetail.getPassword(), accountDetail.getSalt(), accountDetail.getAuthorities());
	}

}
