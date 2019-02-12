package com.dongzhi.ow;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import com.dongzhi.ow.util.PortUtil;

/**
 * @ClassName:     Application.java
 * @Description:   SpringBoot启动类，打包war需要新加@ServletComponentScan注解, 继承SpringBootServletInitializer
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午7:45:50
 */
@ServletComponentScan
@SpringBootApplication
@EnableCaching
public class Application extends SpringBootServletInitializer {
	
	/**
	 * 	通过端口检测redis是否启动 
	 */
	static {
        PortUtil.checkPort(6379,"Redis 服务端",true);
    }
	
	/**
	 * 	打包war需要重写该方法
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
