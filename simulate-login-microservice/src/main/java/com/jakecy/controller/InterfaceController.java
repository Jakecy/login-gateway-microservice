package com.jakecy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jakecy.model.Account;
import com.jakecy.service.AccountService;

@RestController
@RequestMapping("/customController")
public class InterfaceController {

	@Autowired
	private  AccountService     	accountService;
	
    /**
	 * 查询单个数据
	 *
	 * @param id
	 * @return
	 */

	@RequestMapping(value = "/queryAccount/{username}", method = RequestMethod.GET)
	public Account queryById(@PathVariable("username") String  username) {
		return accountService.getAccount(username);
	}
}
