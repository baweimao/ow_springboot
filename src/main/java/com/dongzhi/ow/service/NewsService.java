package com.dongzhi.ow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dongzhi.ow.dao.NewsDAO;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.News;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.util.Page4Navigator;

/**
 * @ClassName:     NewsService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:17:27
 */
@Service
@CacheConfig(cacheNames="news")
public class NewsService {

	public final int art = 0;//普通资讯
	
	public final int top = 1;//置顶资讯
	
	public final int hide = 0;//隐藏类
	
	@Autowired NewsDAO newsDAO;
	
	@CacheEvict(allEntries=true)
	public void add(News bean) {
		newsDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		newsDAO.delete(id);
	}

	@Cacheable(key="'news-one-'+#p0")
	public News get(int id) {
		return newsDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(News bean) {
		newsDAO.save(bean);
	}
	
	@Cacheable(key="'news-all'")
	public List<News> list() {
		return newsDAO.findAll();
	}
	
	@Cacheable(key="'news-up-'+#p0")
	public List<News> listByUp(int up) {
		Sort sort = new Sort(Direction.ASC,"newsOrder");
		return newsDAO.findByUp(up, sort);
	}
	
	@Cacheable(key="'news-up-page-'+#p0+ '-' + #p1+ '-' + #p2")
	public Page4Navigator listByUp(int up, int start, int size, int navigatePages) {
		Sort sort = new Sort(Direction.DESC,"newsDate");
		Pageable pageable = new PageRequest(start, size, sort);
		Page pageFromJPA = newsDAO.findByUp(up, pageable);
		return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
	
	@Cacheable(key="'news-up-order-'+#p0+ '-' + #p1")
	public List<News> listByUpAndOrder(int up, int order) {
		Sort sort = new Sort(Direction.ASC,"newsOrder");
		return newsDAO.findByUpAndNewsOrder(up, order);
	}
	
	@Cacheable(key="'news-up-notorder-'+#p0+ '-' + #p1")
	public List<News> listByUpAndOrderNot(int up, int order) {
		Sort sort = new Sort(Direction.ASC,"newsOrder");
		return newsDAO.findByUpAndNewsOrderNot(up, order, sort);
	}
	
	@Cacheable(key="'news-up-notorder-page-'+#p0+ '-' + #p1+ '-' + #p2")
	public Page4Navigator listByOrderNot(int order, int start, int size, int navigatePages) {
		Sort sort = new Sort(Direction.DESC,"newsDate");
		Pageable pageable = new PageRequest(start, size, sort);
		Page pageFromJPA = newsDAO.findByNewsOrderNot(order, pageable);
		return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
	
	@Cacheable(key="'news-notorder-'+#p0")
	public List<News> listByOrderNot(int order) {
		Sort sort = new Sort(Direction.DESC,"newsDate");
		return newsDAO.findByNewsOrderNot(order, sort);
	}

	/**
	 * @Description:  避免序列化死循环，移除New中Web的cid属性
	 * @param:        @param ns    
	 * @return:       void
	 */
	public void removeCategoryFromWebFromNews(List<News> ns) {
		for(News n : ns) {
			removeCategoryFromWebFromNews(n);
			datetimepicker(n);
		}
	}
	
	public void removeCategoryFromWebFromNews(News n) {
		Web w = n.getWeb();
		if(null!=w) {
			w.setCategory(null);
		}
	}
	
	/**
	 * 	日期选择器和new.web.id冲突，导致日期选择器无法点击弹出;
	 *	在实体类new中加入临时变量wid中转web.id;
	 * @param n
	 */
	public void datetimepicker(News n) {
		n.setWid(n.getWeb().getId());
	}
}
