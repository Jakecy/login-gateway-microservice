package com.jakecy.config;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakecy.config.authProvider.CustomAuthenticationProvider;
import com.jakecy.config.dto.AccountDetail;
import com.jakecy.config.filter.RequestBodyReaderAuthenticationFilter;
import com.jakecy.config.handler.CustomLoginSuccessHandler;
import com.jakecy.config.handler.CustomloginFailureHandler;
import com.jakecy.config.utils.RandomUtil;
import com.jakecy.utils.IRedisUtil;
import com.jakecy.utils.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	/**
	 * 把自定义的对象放入config中，
	 * 以便于config使用
	 */
	
	@Autowired
    private  UserDetailsService userService;
	@Autowired
    private  ObjectMapper objectMapper;
	
   @Autowired
   private  PasswordEncoder passwordEncoder;
   
   //注入自定义的AuthenticationProvider
   @Autowired
   private CustomAuthenticationProvider  customAuthenticationProvider;
   
   //注入与登录相关的处理器
   @Autowired
   private CustomLoginSuccessHandler  successHandler;
   @Autowired
   private CustomloginFailureHandler  failureHandler;
   @Autowired
   private RedisUtil				redisUtil;
   
   @Autowired
   private IRedisUtil			iRedisUtil;
   
   private Integer mobileTimeout;
   private static final Integer LOGIN_TIMEOUT = 86400; // 单位秒 60*60*24  86400
	
	
   @Bean
   public RequestBodyReaderAuthenticationFilter authenticationFilter() throws Exception {
       RequestBodyReaderAuthenticationFilter authenticationFilter
           = new RequestBodyReaderAuthenticationFilter();
       authenticationFilter.setAuthenticationSuccessHandler(successHandler);
       authenticationFilter.setAuthenticationFailureHandler(failureHandler);
       authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "GET"));
       authenticationFilter.setAuthenticationManager(authenticationManagerBean());
       return authenticationFilter;
   }
   /**
    * 以方法的方式注入一个authenticationProvider对象
    * @param request
    * @param response
    * @param authentication
    * @throws IOException
    */
   @Bean
   public DaoAuthenticationProvider authProvider() {
       DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       authProvider.setUserDetailsService(userService);
       authProvider.setPasswordEncoder(passwordEncoder);
       return authProvider;
   }
   /**
    * 在这里增加一个全局配置：向authenticationManager中放入我们自定义的authenticationProvider
    */
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      //这里使用的是方法注入的方式来注入对象
	  //authProvider()方法
	   auth.authenticationProvider(this.customAuthenticationProvider);
   }

   /**
    * 把配置代码写到configure中
    * security会自动应用configure()中的配置
    */
   @Override
   protected void configure(HttpSecurity http) throws Exception {
       http
        
           .csrf().disable()
           .authorizeRequests()
           .anyRequest().authenticated()
           .and()  //这里的and()就是起到一个结束符的作用
           .authorizeRequests()
		   .antMatchers("/login**").permitAll()

           .and()
           .addFilterBefore(
               authenticationFilter(),
               UsernamePasswordAuthenticationFilter.class)
           .logout()
           .logoutUrl("/logout")
           .logoutSuccessHandler(this::logoutSuccessHandler)

           .and()
           .exceptionHandling()
           .authenticationEntryPoint(new Http401AuthenticationEntryPoint("401"));
       
          //在这里增加配置，让其拦截所有的接口请求
         //校验每一个接口是否携带token,如果没有携带token就返回403
       http.securityContext().securityContextRepository(new SecurityContextRepository() {
			@Override
			public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				String tokenStr = requestResponseHolder.getRequest().getHeader("token");
				if (tokenStr == null) {
					tokenStr = requestResponseHolder.getRequest().getParameter("_token");
				}
				if (tokenStr != null && !tokenStr.trim().equals("undefined") && !tokenStr.trim().equals("")) {
					Object obj = iRedisUtil.getObject("custom_login_context_" + tokenStr);
					if (obj != null) {
						context = (SecurityContext) obj;
                       flushTokenValid(tokenStr);
					}
				} else {
					requestResponseHolder.getResponse().setHeader("token", RandomUtil.uuid());
				}
				return context;
			}

			@Override
			public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
				Authentication authentication = context.getAuthentication();
				if (authentication != null) {
					String token = response.getHeader("token");
					if (token == null) {
						token = request.getParameter("_token");
					}
					Object obj = authentication.getPrincipal();
					if (obj != null && token != null) {
						if (obj instanceof AccountDetail) {
							AccountDetail accountDetail = (AccountDetail) obj;
                           saveToken(token, context, accountDetail);
							SecurityContextHolder.getContext().setAuthentication(context.getAuthentication());
						}
					}
				}
			}

			@Override
			public boolean containsContext(HttpServletRequest request) {
				String tokenStr = request.getHeader("token");
				if (tokenStr == null) {
					tokenStr = request.getParameter("_token");
				}
				if (tokenStr != null && !tokenStr.trim().equals("undefined")) {
					Object obj = redisUtil.get("custom_login_token_" + tokenStr);
					if (obj != null) {
                       flushTokenValid(tokenStr);
						return true;
					}
				}
				return false;
			}
		});
	}

	private void saveToken(String token, SecurityContext context, AccountDetail accountDetail){
		iRedisUtil.setObject("custom_login_context_" + token, context);
		redisUtil.set("custom_login_token_" + token, accountDetail+ "");
       flushTokenValid(token);
	}

	private void flushTokenValid(String token){
       if (mobileTimeout != null) {
           iRedisUtil.expire("login_context_" + token, mobileTimeout);
       }else {
           iRedisUtil.expire("login_context_" + token, LOGIN_TIMEOUT);
       }
       redisUtil.expire("login_token_" + token, LOGIN_TIMEOUT);
   }

   
   /**
    * 此处增加一个安全配置方法
    * @param request
    * @param response
    * @param authentication
    * @throws IOException
    */
   public WebSecurityConfig(UserDetailsService userService, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
       this.userService = userService;
       this.objectMapper = objectMapper;
       this.passwordEncoder = passwordEncoder;
   }
   
   

   
   
   
  
	 
	    private void logoutSuccessHandler(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        Authentication authentication) throws IOException {
	    	//当流程完整地从AuthenticationFilter中出来之后，我们会得到一个Authentication对象
	    	//我现在要看的是这个authentication对象的isAuthenticated属性
	    	System.out.println("-----------------Authentication对象-------");
	    	System.out.println("------------我们现在得到的authentication对象的isAuthenticated属性值是:"+authentication.isAuthenticated());
	 
	        response.setStatus(HttpStatus.OK.value());
	        objectMapper.writeValue(response.getWriter(), "Bye!");
	    }
    

}
