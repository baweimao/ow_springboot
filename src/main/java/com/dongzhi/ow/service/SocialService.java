package com.dongzhi.ow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.SocialDAO;
import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Social;

/**
 * @ClassName:     SocialService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:21:49
 */
@Service
@CacheConfig(cacheNames="socials")
public class SocialService {
	
	@Autowired SocialDAO socialDAO;
	@Autowired PeopleService peopleService;
	
	@CacheEvict(allEntries=true)
	public void add(Social bean) {
		socialDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		socialDAO.delete(id);
	}

	@Cacheable(key="'socials-one-'+#p0")
	public Social get(int id) {
		return socialDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Social bean) {
		socialDAO.save(bean);
	}

	@Cacheable(key="'socials-all'")
	public List<Social> list() {
		return socialDAO.findAll();
	}
	
	@Cacheable(key="'socials-people-'+#p0")
	public List<Social> listByPeople(int pid) {
		People people = peopleService.get(pid);
		Sort sort = new Sort(Direction.ASC, "socialOrder");
		return socialDAO.findByPeople(people, sort);
	}
	
	@Cacheable(key="'socials-people-order-'+#p0+ '-' + #p1")
	public List<Social> listByPeopleAndOrder(int pid, int order) {
		People people = peopleService.get(pid);
		return socialDAO.findByPeopleAndSocialOrder(people, order);
	}
}
