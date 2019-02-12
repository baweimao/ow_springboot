package com.dongzhi.ow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.PeopleDAO;
import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Type;

/**
 * @ClassName:     PeopleService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:19:46
 */
@Service
@CacheConfig(cacheNames="peoples")
public class PeopleService {
	
	@Autowired PeopleDAO peopleDAO;
	@Autowired TypeService typeService;
	
	@CacheEvict(allEntries=true)
	public void add(People bean) {
		peopleDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		peopleDAO.delete(id);
	}

	@Cacheable(key="'peoples-one-'+#p0")
	public People get(int id) {
		return peopleDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(People bean) {
		peopleDAO.save(bean);
	}

	@Cacheable(key="'peoples-all'")
	public List<People> list() {
		return peopleDAO.findAll();
	}
	
	@Cacheable(key="'peoples-type-'+#p0")
	public List<People> listByType(int tid) {
		Type type = typeService.get(tid);
		Sort sort = new Sort(Direction.ASC, "peopleOrder");
		return peopleDAO.findByType(type, sort);
	}
	
	@Cacheable(key="'peoples-type-order-'+#p0+ '-' + #p1")
	public List<People> listByTypeAndOrder(int tid, int order) {
		Type type = typeService.get(tid);
		return peopleDAO.findByTypeAndPeopleOrder(type, order);
	}
}
