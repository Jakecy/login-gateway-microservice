package com.jakecy.config.pwdEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jakecy.config.utils.MD5;


@Component("passwordEncoder")
public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence arg) {
		
		//进行加密操作
		//password1 + "_" + sal
		//account.setUserPwd(MD5.getMD5(password1 + "_" + salt));
		//MD5.getMD5(arg);
		String raw = arg.toString();
		String enCodedStr = MD5.getMD5(raw);
		return enCodedStr;
	}

	@Override
	public boolean matches(CharSequence arg1, String arg2) {
		if(arg1.toString().equals(arg2)){
			return true;
		}
		return false;
	}

}
