package com.dongzhi.ow.web;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.dongzhi.ow.pojo.User;
import com.dongzhi.ow.service.UserService;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     UserController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:16:56
 */
@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping(value="/login")
	public Object login(@RequestBody User bean, HttpSession session) throws Exception{
		String name = HtmlUtils.htmlEscape(bean.getName());

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(name, bean.getPassword());
		try {
			subject.login(token);
			User user = userService.get(name).get(0);
			session.setAttribute("user", user);
			return Result.success();
		} catch (AuthenticationException e) {
			String message ="账号密码错误";
            return Result.fail(message);
		}		
	}

	@GetMapping(value="/user")
	public Object get() {
		User bean = userService.get().get(0);
		return Result.success(bean);
	}
	
	@PutMapping(value="/user")
	public Object update(@RequestBody User bean, HttpSession session) {
		String name = HtmlUtils.htmlEscape(bean.getName());
		String password = bean.getPassword();
		String salt = new SecureRandomNumberGenerator().nextBytes().toString();
		int times = 2;
		String algorithmName = "md5";
		String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
		
		bean.setSalt(salt);
		bean.setPassword(encodedPassword);
		bean.setName(name);
		userService.update(bean);
		session.removeAttribute("user");
		return Result.success(bean);
	}
}
