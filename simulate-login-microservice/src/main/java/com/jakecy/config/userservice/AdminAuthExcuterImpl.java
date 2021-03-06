package com.jakecy.config.userservice;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.jakecy.config.dto.AccountDetail;
import com.jakecy.config.exception.PasswordWrongException;
import com.jakecy.config.excuter.AuthenticationCommonExcuter;
import com.jakecy.config.utils.MD5;

@Service("authenticationCommonExcuter")
public class AdminAuthExcuterImpl implements AuthenticationCommonExcuter {

	@Override
	public void verify(AccountDetail accountDetail, UsernamePasswordAuthenticationToken token) {
		// 在这里我们做用户名和密码的校验工作
		// 判断用户名和密码是否正确
		// 把前台传来的密码进行加密，并把加密后的和库中的进行比对
		String passwdToken = (String) token.getCredentials();// 获取密码
		// 进行MD5加密
		//由于FrontEnd传来的是md5加密后的密码，所以
		//这里要手动地进行Md5加密进行immulate
		passwdToken  = MD5.getMD5(passwdToken);
		//MD5.getMD5(cAccount.getUserPwd() + "_" + salt)
		String encodedPresentedPwd = MD5.getMD5(passwdToken + "_" + accountDetail.getSalt());
		//把加密后的密码打印出来
		System.out.println("加密后的密码是："+encodedPresentedPwd);
		// 进行比对
		if (!accountDetail.getPassword().equals(encodedPresentedPwd)) {
			// 如果密码不对
			// 就抛出passwordWrongException
			throw new PasswordWrongException("密码错误!");
		}
		return;
	}
}
