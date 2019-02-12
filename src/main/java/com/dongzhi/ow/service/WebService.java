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

import com.dongzhi.ow.dao.WebDAO;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.Web;

/**
 * @ClassName:     WebService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存 
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:33:42
 */
@Service
@CacheConfig(cacheNames="webs")
public class WebService {

	@Autowired WebDAO webDAO;
	@Autowired CategoryService categoryService;
	
	@CacheEvict(allEntries=true)
	public void add(Web bean) {
		webDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		webDAO.delete(id);
	}

	@Cacheable(key="'webs-one-'+#p0")
	public Web get(int id) {
		return webDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Web bean) {
		webDAO.save(bean);
	}

	@Cacheable(key="'webs-all'")
	public List<Web> list() {
		Sort sort = new Sort(Direction.ASC, "webOrder");
		return webDAO.findAll(sort);
	}
	
	@Cacheable(key="'webs-category-'+#p0")
	public List<Web> listByCategory(int cid) {
		Category category = categoryService.get(cid);
		Sort sort = new Sort(Direction.ASC, "webOrder");
		return webDAO.findByCategory(category, sort);
	}
	
	@Cacheable(key="'webs-category-page-'+#p0+ '-' + #p1+ '-' + #p1")
	public List<Web> listByCategory(int cid, int start, int size) {
		Category category = categoryService.get(cid);
		Sort sort = new Sort(Direction.ASC, "webOrder");
		Pageable pageable = new PageRequest(start, size, sort);
		return webDAO.findByCategory(category, pageable);
	}
	
	@Cacheable(key="'webs-category-order-'+#p0+ '-' + #p1")
	public List<Web> listByCategoryAndOrder(int cid, int order) {
		Category category = categoryService.get(cid);
		return webDAO.findByCategoryAndWebOrder(category, order);
	}
}
