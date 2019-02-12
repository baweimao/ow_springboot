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

import com.dongzhi.ow.dao.CategoryDAO;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.util.SpringContextUtil;

/**
 * @ClassName:     CategoryService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:40:11
 */
@Service
@CacheConfig(cacheNames="categories")
public class CategoryService {
	
	//隐藏类
	public final int hide = 0;
	
	@Autowired CategoryDAO categoryDAO;
	@Autowired WebService webService;
	
	@CacheEvict(allEntries=true)
	public void add(Category bean) {
		categoryDAO.save(bean);
	}

	@CacheEvict(allEntries=true)
	public void delete(int id) {
		categoryDAO.delete(id);
	}

	@Cacheable(key="'categories-one-'+#p0")
	public Category get(int id) {
		return categoryDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Category bean) {
		categoryDAO.save(bean);
	}
	
	@Cacheable(key="'categories-all'")
	public List<Category> list() {
		Sort sort = new Sort(Direction.ASC,"categoryOrder");
		return categoryDAO.findAll(sort);
	}
	
	@Cacheable(key="'categories-order-'+#p0")
	public List<Category> listByOrder(int categoryorder) {
		Sort sort = new Sort(Direction.ASC,"categoryOrder");
		return categoryDAO.findByCategoryOrder(categoryorder, sort);
	}
	
	@Cacheable(key="'categories-order-page-'+#p0+ '-' + #p1+ '-' + #p2")
	public List<Category> listByOrder(int categoryorder, int start, int size) {
		Sort sort = new Sort(Direction.ASC,"categoryOrder");
		Pageable pageable = new PageRequest(start, size, sort);
		return categoryDAO.findByCategoryOrder(categoryorder, pageable);
	}
	
	@Cacheable(key="'categories-notorder-'+#p0")
	public List<Category> listByOrderNot(int categoryorder) {
		Sort sort = new Sort(Direction.ASC,"categoryOrder");
		return categoryDAO.findByCategoryOrderNot(categoryorder, sort);
	}
	
	@Cacheable(key="'categories-notorder-page-'+#p0+ '-' + #p1+ '-' + #p2")
	public List<Category> listByOrderNot(int categoryorder, int start, int size) {
		Sort sort = new Sort(Direction.ASC,"categoryOrder");
		Pageable pageable = new PageRequest(start, size, sort);
		return  categoryDAO.findByCategoryOrderNot(categoryorder, pageable);
	}
	
	public void fill(Category c) {
		WebService webService = SpringContextUtil.getBean(WebService.class);
		int cid = c.getId();
		c.setWs(webService.listByCategory(cid));
	}
	
	/**
	 * @Description:  初始化Category中的瞬时集合Webs
	 * @param:        @param c    
	 * @return:       void
	 */
	public void fill(List<Category> cs) {
		for(Category c:cs) {
			fill(c);
		}
	}
	
	/**
	 * @Description:  避免序列化死循环，移除Category中Web的cid属性
	 * @param:        @param cs    
	 * @return:       void
	 */
	public void removeCategoryFromWeb(List<Category> cs) {
		for(Category c : cs) {
			removeCategoryFromWeb(c);
		}
	}
	
	public void removeCategoryFromWeb(Category c) {
		List<Web> ws = c.getWs();
		if(null!=ws) {
			for(Web w : ws) {
				w.setCategory(null);
			}
		}
	}
}
