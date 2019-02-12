package com.dongzhi.ow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.TypeDAO;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Social;
import com.dongzhi.ow.pojo.Type;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.util.SpringContextUtil;

/**
 * @ClassName:     TypeService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:22:16
 */
@Service
@CacheConfig(cacheNames="types")
public class TypeService {

	public final int hide = 0;//隐藏类
	
	@Autowired TypeDAO typeDAO;
	@Autowired PeopleService peopleService;
	@Autowired SocialService socialService;
	
	@CacheEvict(allEntries=true)
	public void add(Type bean) {
		typeDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		typeDAO.delete(id);
	}

	@Cacheable(key="'types-one-'+#p0")
	public Type get(int id) {
		return typeDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Type bean) {
		typeDAO.save(bean);
	}

	@Cacheable(key="'types-all'")
	public List<Type> list() {
		Sort sort = new Sort(Direction.ASC,"typeOrder");
		return typeDAO.findAll(sort);
	}
	
	@Cacheable(key="'types-order-'+#p0")
	public List<Type> listByOrder(int order) {
		return typeDAO.findByTypeOrder(order);
	}
	
	@Cacheable(key="'types-notorder-'+#p0")
	public List<Type> listByOrderNot(int order) {
		Sort sort = new Sort(Direction.ASC,"typeOrder");
		return typeDAO.findByTypeOrderNot(order, sort);
	}
	
	public void fill(Type t) {
		PeopleService peopleService = SpringContextUtil.getBean(PeopleService.class);
		SocialService socialService = SpringContextUtil.getBean(SocialService.class);
		int tid = t.getId();
		List<People> ps = peopleService.listByType(tid);
		for(People p:ps) {
			int pid = p.getId();
			List<Social> ss = socialService.listByPeople(pid);
			p.setSs(ss);
		}
		t.setPs(ps);
	}
	
	/**
	 * @Description:  初始化Type中的瞬时集合Peopele，以及Peopele中的瞬时集合Social
	 * @param:        @param ts    
	 * @return:       void
	 */
	public void fill(List<Type> ts) {
		for(Type t:ts) {
			fill(t);
		}
	}
	
	/**
	 * @Description:  避免序列化死循环，移除Type中People的tid属性，以及移除People中Social的pid属性
	 * @param:        @param ts    
	 * @return:       void
	 */
	public void removeTypeFromPeople(List<Type> ts) {
		for(Type t : ts) {
			removeTypeFromPeople(t);
		}
	}
	
	public void removeTypeFromPeople(Type t) {
		List<People> ps = t.getPs();
		if(null!=ps) {
			for(People p : ps) {
				p.setType(null);
			}
		}
		removePeopleFromSocial(ps);
	}

	public void removePeopleFromSocial(List<People> ps) {
		for(People p : ps) {
			removePeopleFromSocial(p);
		}
	}
	
	public void removePeopleFromSocial(People p) {
		List<Social> ss = p.getSs();
		if(null!=ss) {
			for(Social s : ss) {
				s.setPeople(null);
			}
		}
	}
}
