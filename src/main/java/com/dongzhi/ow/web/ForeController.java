package com.dongzhi.ow.web;

import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.hsqldb.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.dongzhi.ow.pojo.Article;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.Foot;
import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.News;
import com.dongzhi.ow.pojo.Type;
import com.dongzhi.ow.service.ArticleService;
import com.dongzhi.ow.service.CategoryService;
import com.dongzhi.ow.service.FootService;
import com.dongzhi.ow.service.GameService;
import com.dongzhi.ow.service.NewsService;
import com.dongzhi.ow.service.TypeService;
import com.dongzhi.ow.util.Page4Navigator;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     ForeController.java
 * @Description:   前台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:08:47
 */
@RestController
public class ForeController {
	
	@Autowired
	ArticleService articleService;
	@Autowired
	GameService gameService;
	@Autowired
	NewsService newsService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	TypeService typeService;
	@Autowired
	FootService footService;
	
	@GetMapping("/forehomes")
	public Object home() throws Exception{
		List<News> ns = newsService.listByUpAndOrderNot(newsService.top, newsService.hide);
		newsService.removeCategoryFromWebFromNews(ns);
		List<Category> cs = categoryService.listByOrderNot(categoryService.hide);
		categoryService.fill(cs);
		categoryService.removeCategoryFromWeb(cs);
		List<Type> ts = typeService.listByOrderNot(typeService.hide);
		typeService.fill(ts);
		typeService.removeTypeFromPeople(ts);
		Map<String, Object> map = new HashMap<>();
		map.put("ns", ns);
		map.put("cs", cs);
		map.put("ts", ts);
		return Result.success(map);
	}
	
	@GetMapping("/foregametables")
	public Object gameTable(@RequestParam("time") long time) throws Exception{
		Date date = new Date(time);
		List<Game> gs = gameService.fillGameTableAndLive(date);
		gameService.removeGameFromGameTableLive(gs);
		return Result.success(gs);
	}
	
	@GetMapping("/forenews")
	public Object news(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "count", defaultValue = "8") int count) throws Exception{
		List<Article> as = articleService.listByUp(articleService.recommend);
		List<News> all = newsService.listByOrderNot(newsService.hide);
		int allSize = all.size();
		Page4Navigator<News> page = newsService.listByOrderNot(newsService.hide, start, count, 8);
		Map<String, Object> map = new HashMap<>();
		System.out.println(as);
		map.put("as", as);
		map.put("page", page);
		map.put("allSize", allSize);
		if(page.getContent().get(page.getContent().size()-1).getId()==all.get(all.size()-1).getId()) {
			return Result.fail("已到底", map);
		}
		return Result.success(map);
	}
	
	@GetMapping("/forearticles")
	public Object title(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "count", defaultValue = "8") int count) throws Exception{
		List<Article> all = articleService.listByUpNotAndOrderNot(articleService.top, articleService.hide);
		int allSize = all.size();
		Page4Navigator<Article> page = articleService.listByUpNotAndOrderNot(articleService.top, articleService.hide, start, count, 8);
		List<Article> asTop = articleService.listByUpAndOrderNot(articleService.top, articleService.hide);
		articleService.fillTitle(asTop);
		articleService.fillTitle(page.getContent());
		Map<String, Object> map = new HashMap<>();
		map.put("asTop", asTop);
		map.put("page", page);
		map.put("allSize", allSize);
		if(page.getContent().get(page.getContent().size()-1).getId()==all.get(all.size()-1).getId()) {
			return Result.fail("已到底", map);
		}
		return Result.success(map);
	}
	
	@GetMapping("/forearticles/{id}")
	public Object article(@PathVariable("id") int id) throws Exception{
		Article bean = articleService.get(id);
		String content = bean.getContent();
		String contentHTML = StringEscapeUtils.unescapeHtml(content);
		bean.setContent(contentHTML);
		return Result.success(bean);
	}
	
	@GetMapping("/foregame")
	public Object game() throws Exception{
		List<Game> gsTop = gameService.listByUpAndOrderNot(gameService.top, gameService.hide);
		List<Game> gs = gameService.listByUpAndOrderNot(gameService.art, gameService.hide);
		gameService.fillGameRanks(gsTop);
		gameService.removeGameFromRanks(gsTop);
		gameService.fillGameRanks(gs);
		gameService.removeGameFromRanks(gs);
		Map<String, Object> map = new HashMap<>();
		map.put("gsTop", gsTop);
		map.put("gs", gs);
		return Result.success(map);
	}
	
	@GetMapping("/forepage/{id}")
	public Object page(@PathVariable("id") int id) throws Exception{
		Foot bean = footService.get(id);
		return Result.success(bean);
	}

}
