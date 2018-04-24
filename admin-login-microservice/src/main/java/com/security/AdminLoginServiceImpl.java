package com.security;

import org.springframework.stereotype.Service;

import com.config.dto.AccountDetail;
import com.config.userservice.LoginService;

@Service("loginService")
public class AdminLoginServiceImpl implements LoginService {

	//在这里调用accountService完成从数据库中获取用户信息
	
	
	@Override
	public AccountDetail getAccountDetail(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
