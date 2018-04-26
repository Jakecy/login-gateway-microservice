package com.jakecy.config.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakecy.config.utils.RandomUtil;
import com.jakecy.utils.RedisUtil;

@Component("customLoginSuccessHandler")
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	
	@Autowired
    private  ObjectMapper objectMapper;
	@Autowired
	private  RedisUtil    redisUtil;  //注入redis,以便于进行token处理
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		  //当流程完整地从AuthenticationFilter中出来之后，我们会得到一个Authentication对象
	   	   //我现在要看的是这个authentication对象的isAuthenticated属性
	   	   System.out.println("-----------------Authentication对象-------");
	   	   System.out.println("------------我们现在得到的authentication对象的isAuthenticated属性值是:"+authentication.isAuthenticated());
		        
	   	   //设置响应格式
	   	   response.setHeader("Content-type", "text/html;charset=UTF-8");
		   response.setCharacterEncoding("UTF-8");
	   	   response.setStatus(HttpStatus.OK.value());
	   	   String token=RandomUtil.randomUUID();
	   	   response.setHeader("token",token );//返回一个用UUID作为token
	  /* 	   PrintWriter out = response.getWriter();
	   	   out.write(token);*/
	   	byte [] buffer=new byte[1024];
	   	OutputStream out=response.getOutputStream();
	   	out.write(token.getBytes());
	   	   //把用户登录信息以token的形式保存到redis中
	   	    redisUtil.set("login_token"+token, authentication.getName(), 100000l);
	   	   //登录成功之后，把与该账户相关的信息，返回给客户端浏览器，
	   	   //以便于让其保存到cookie中
		       
		   //objectMapper.writeValue(response.getWriter(), "Yayy you logged in!");
		  // objectMapper.writeValue(response.getWriter(), "恭喜您"+authentication.getPrincipal()+"登录成功！！！");
	}

}
