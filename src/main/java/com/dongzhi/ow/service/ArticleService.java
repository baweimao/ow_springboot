package com.dongzhi.ow.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.dongzhi.ow.dao.ArticleDAO;
import com.dongzhi.ow.pojo.Article;
import com.dongzhi.ow.util.Page4Navigator;

/**
 * @ClassName:     ArticleService.java
 * @Description:   在Service层启用redis缓存, 避免数据不同步，增删改重置所有缓存
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:32:26
 */
@Service
@CacheConfig(cacheNames="articles")
public class ArticleService {
	
	public final int art = 0;//普通文章
	
	public final int top = 1;//置顶文章
	
	public final int recommend = 2;//推荐文章
	
	public final int hide = 0;//隐藏类
	
	@Autowired ArticleDAO articleDAO;

	@CacheEvict(allEntries=true)
	public void add(Article bean) {
		articleDAO.save(bean);
	}
	
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		articleDAO.delete(id);
	}
	
	@Cacheable(key="'articles-one-'+#p0")
	public Article get(int id) {
		return articleDAO.findOne(id);
	}
	
	@CacheEvict(allEntries=true)
	public void update(Article bean) {
		articleDAO.save(bean);
	}
	
	@Cacheable(key="'articles-all'")
	public List<Article> list() {
		return articleDAO.findAll();
	}

	@Cacheable(key="'articles-up-'+#p0")
	public List<Article> listByUp(int up) {
		Sort sort = new Sort(Direction.ASC, "articleOrder");
		return articleDAO.findByUp(up, sort);
	}
	
	@Cacheable(key="'articles-page-'+#p0+ '-' + #p1 + '-' + #p2")
	public Page4Navigator listByUp(int up, int start, int size, int navigatePages) {
		Sort sort = new Sort(Direction.DESC, "articleDate");
		Pageable pageable = new PageRequest(start, size, sort);
		Page pageFromJPA = articleDAO.findByUp(up, pageable);
		return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
	
	@Cacheable(key="'articles-up-order-'+#p0+ '-' + #p1")
	public List<Article> listByUpAndOrder(int up, int articleorder){
		return articleDAO.findByUpAndArticleOrder(up, articleorder);
	}
	
	@Cacheable(key="'articles-up-notorder-'+#p0+ '-' + #p1")
	public List<Article> listByUpAndOrderNot(int up, int articleorder){
		Sort sort = new Sort(Direction.ASC, "articleOrder");
		return articleDAO.findByUpAndArticleOrderNot(up, articleorder, sort);
	}
	
	@Cacheable(key="'articles-notup-notorder-page-'+#p0+ '-' + #p1 + '-' + #p2+ '-' + #p3")
	public Page4Navigator listByUpNotAndOrderNot(int up, int articleorder, int start, int size, int navigatePages) {
		Sort sort = new Sort(Direction.DESC, "articleDate");
		Pageable pageable = new PageRequest(start, size, sort);
		Page pageFromJPA = articleDAO.findByUpNotAndArticleOrderNot(up, articleorder, pageable);
		return new Page4Navigator(pageFromJPA, navigatePages);
	}
	
	@Cacheable(key="'articles-notup-notorder-'+#p0+ '-' + #p1")
	public List<Article> listByUpNotAndOrderNot(int up, int articleorder) {
		Sort sort = new Sort(Direction.ASC, "articleDate");
		return articleDAO.findByUpNotAndArticleOrderNot(up, articleorder, sort);
	}
	
	/**
	 * @Description:  初始化文章封面图片
	 * @param:        @param as    
	 * @return:       void
	 */
	public void fillTitle(List<Article> as) {
		for(Article a:as) {
			//初始化img
			String content = a.getContent();
			Pattern pt = Pattern.compile("(src=\"[^\"]*\")");
			Matcher matcher = pt.matcher(content);
			String str;
			if(matcher.find()) {
				str = matcher.group();
//				System.out.println(str);
				str = str.substring(5,str.length()-1);
//				System.out.println(str);
			}
			else {
				str = "img/news/01.jpg";
			}
			a.setFirstImgUrl(str);
		}
	}
}
