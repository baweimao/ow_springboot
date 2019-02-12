package com.dongzhi.ow.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.dongzhi.ow.pojo.User;
import com.dongzhi.ow.service.UserService;
/**
 * @ClassName:     JPARealm.java
 * @Description:   Realm安全框架用于登陆判断
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午6:17:57
 */
public class JPARealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
		return s;
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = token.getPrincipal().toString();
		User user = userService.get(userName).get(0);
		String passwordInDB = user.getPassword();
		String salt = user.getSalt();
		SimpleAuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo(userName, passwordInDB, ByteSource.Util.bytes(salt), getName());
		return authorizationInfo;
	}
}
