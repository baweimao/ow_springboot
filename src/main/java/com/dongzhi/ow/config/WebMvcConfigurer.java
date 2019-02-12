package com.dongzhi.ow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.dongzhi.ow.interceptor.LoginInterceptor;
import com.dongzhi.ow.interceptor.OtherInterceptor;

/**
 * @ClassName:     WebMvcConfigurer.java
 * @Description:   Mvc登陆拦截配置
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午8:31:27
 */
@Configuration
class WebMvcConfigurer extends WebMvcConfigurerAdapter{

	/**
	 * @Description:  将登陆拦截类装配入Bean
	 * @param:        @return    
	 * @return:       LoginInterceptor
	 */
	@Bean
	public LoginInterceptor getLoginInterceptor() {
		return new LoginInterceptor();
	}
	
	@Bean
	public OtherInterceptor getOtherInterceptor() {
		return new OtherInterceptor();
	}
	
	/**
	 * @Description:  配置过滤拦截器
	 * @param:        @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getLoginInterceptor())
		.addPathPatterns("/**");
		registry.addInterceptor(getOtherInterceptor())
		.addPathPatterns("/**");
	}

}
