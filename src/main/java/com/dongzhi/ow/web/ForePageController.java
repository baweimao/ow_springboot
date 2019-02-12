package com.dongzhi.ow.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName:     ForePageController.java
 * @Description:   前台页面跳转
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:09:04
 */
@Controller
public class ForePageController {
	
	@GetMapping(value="")
	public String def() {
		return "home";
	}
	
	@GetMapping(value="/home")
	public String home() {
		return "home";
	}
	
	@GetMapping(value="/new")
	public String news() {
		return "new";
	}
	
	@GetMapping(value="/title")
	public String title() {
		return "title";
	}
	
	@GetMapping(value="/article")
	public String article() {
		return "article";
	}
	
	@GetMapping(value="/game")
	public String event() {
		return "game";
	}
	
	@GetMapping(value="/info")
	public String info() {
		return "info";
	}
}
