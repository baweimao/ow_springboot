package com.dongzhi.ow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.UserDAO;
import com.dongzhi.ow.pojo.User;

/**
 * @ClassName:     UserService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:33:24
 */
@Service
@CacheConfig(cacheNames="users")
public class UserService {
	
	@Autowired UserDAO userDAO;
	
	@Cacheable(key="'users-one-'")
	public List<User> get() {
		return userDAO.findAll();
	}
	
	@Cacheable(key="'users-one-'+#p0")
	public List<User> get(String name) {
		return userDAO.findByName(name);
	}
	
	@Cacheable(key="'users-one-'+#p0+ '-' + #p1")
	public List<User> get(String name, String password) {
		return userDAO.findByNameAndPassword(name, password);
	}
	
	@CacheEvict(allEntries=true)
	public void update(User bean) {
		userDAO.save(bean);
	}

}
