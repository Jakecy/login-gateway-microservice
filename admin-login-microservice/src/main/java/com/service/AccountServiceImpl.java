package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AccountMapper;
import com.model.Account;

@Service("accountService")
public class AccountServiceImpl  implements  AccountService {

	//调用，查询
	@Autowired
	private  AccountMapper    accountMapper;
	
	/**
	 * 根据用户名获取用户信息
	 */
	@Override
	public Account getAccount(String username) {
		//根据id获取用户信息
	    Account account = accountMapper.selectByUsername(username);
	    return account;
	}
	
}
