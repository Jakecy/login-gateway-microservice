package com.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.config.dto.AccountDetail;
import com.config.userservice.LoginService;
import com.dao.AccountMapper;
import com.model.Account;
import com.service.AccountService;

@Service("loginService")
public class AdminLoginServiceImpl implements LoginService {

	//在这里调用accountService完成从数据库中获取用户信息
	@Autowired
	private AccountService   accountService;

	
	@Override
	public AccountDetail getAccountDetail(String username) {
		
		Account  account=accountService.getAccount(username);//获取用户信息
		if(account==null){
			throw new UsernameNotFoundException("user not exists");
		}
		//创建AccountDetail
		//创建authorities
		List<GrantedAuthority> authorities = new ArrayList<>();
		//new SimpleGrantedAuthority("ROLE_USER")
		authorities.add(new SimpleGrantedAuthority("ROLE_USER,ROLE_ADMIN"));
		AccountDetail ad=new  AccountDetail(username, account.getUserPwd(), account.getPwdSalt(), authorities);
		return ad;
	}

}
