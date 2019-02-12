package com.dongzhi.ow.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dongzhi.ow.pojo.News;
import com.dongzhi.ow.service.NewsService;
import com.dongzhi.ow.service.WebService;
import com.dongzhi.ow.util.Filepath;
import com.dongzhi.ow.util.ImageUtil;
import com.dongzhi.ow.util.Page4Navigator;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     NewsController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:15:46
 */
@RestController
public class NewsController {

	@Autowired NewsService newsService;
	@Autowired WebService webService;
	
	@GetMapping(value="/news")
	public Object list(@RequestParam(value = "start", defaultValue = "0") int start,@RequestParam(value = "size", defaultValue = "5") int size) throws Exception{
	
		//置顶资讯集合
		List<News> nsTop = newsService.listByUp(newsService.top);
		//正常资讯集合
		Page4Navigator <News> page = newsService.listByUp(newsService.art, start, size, 5);
		Map<String, Object> map = new HashMap<>();
		map.put("nsTop", nsTop);
		map.put("page", page);
		return Result.success(map);
	}
	
	@PostMapping(value="/news")
	public Object add(News bean, HttpServletRequest request, MultipartFile image) throws Exception {
		//判断是否插入内嵌视频
		String content = bean.getContent();
		if(content.contains("iframe"))
			bean.setVideo(1);
		else
			bean.setVideo(0);
		
		Date d = new Date();
		int up = 0;
		bean.setImg(1);
		bean.setNewsOrder(1);
		bean.setNewsDate(d);
		bean.setUp(up);
		newsService.add(bean);

		if(!image.isEmpty()) {
			File imageFolder = new File(new Filepath().path()+"img/news");
			File file = new File(imageFolder, bean.getId()+".jpg");
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			image.transferTo(file);
			BufferedImage img = ImageUtil.change2jpg(file);	
			ImageIO.write(img, "jpg", file);	
		}
		return Result.success();
	}
	
	@PostMapping(value="/news_noimage")
	public Object addNoPic(News bean) {
		//判断是否插入内嵌视频
		String content = bean.getContent();
		if(content.contains("iframe"))
			bean.setVideo(1);
		else
			bean.setVideo(0);
		
		Date d = new Date();
		int up = 0;
		bean.setImg(0);
		bean.setNewsOrder(1);
		bean.setNewsDate(d);
		bean.setUp(up);
		newsService.add(bean);
		return Result.success();
	}
	
	@DeleteMapping(value="/news/{id}")
	public Object delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
		//正式删除前排序
		News n = newsService.get(id);
		if(n.getUp()==newsService.top) {
			List<News> ns = newsService.listByUpAndOrderNot(newsService.top, newsService.hide);//获取top列表
			News nlast = ns.get(ns.size()-1); //获取top列表最后一位元素
			 //判读是否是最后一位元素
			if (n.getId()!= nlast.getId()) {
				ns = ns.subList(n.getNewsOrder()-1, ns.size());	
				for(News ne:ns) {
					ne.setNewsOrder(ne.getNewsOrder()-1);
					newsService.update(ne);
				}
			}	
		}
		newsService.delete(id);
		//删除对应图片
		File imageFolder = new File(new Filepath().path()+"img/news");
		File file = new File(imageFolder, id+".jpg");
		file.delete();
		return Result.success("");
	}
	
	@GetMapping(value="/news/{id}")
	public Object get(@PathVariable("id") int id) throws Exception{
		News bean = newsService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping(value="/news")
	public Object update(@RequestBody News bean) throws Exception{
		//判断是否插入内嵌视频
		String content = bean.getContent();
		if(content.contains("iframe")) {
			bean.setVideo(1);
			bean.setUp(newsService.art);
			bean.setNewsOrder(1);
		}
		else
			bean.setVideo(0);
		newsService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping(value="/news/{id}/top")
	public Object top(@PathVariable("id") int id) throws Exception{
		News n = newsService.get(id);
		int up = n.getUp();
		List<News> ns = newsService.listByUp(newsService.top);//获取top列表
		if(!ns.isEmpty()) {
			for(News ne:ns) {
				ne.setNewsOrder(ne.getNewsOrder()+1);
				newsService.update(ne);
			}
		}
		n.setNewsOrder(1);
		n.setUp(newsService.top);
		newsService.update(n);
		return Result.success();
	}
	
	@GetMapping(value="/news/{id}/art")
	public Object art(@PathVariable("id") int id) throws Exception{
		News n = newsService.get(id);
		int order = n.getNewsOrder();
		List<News> ns = newsService.listByUp(newsService.top);//获取top列表
		News nlast = ns.get(ns.size()-1); //获取top列表最后一位元素
		 //判读是否是最后一位元素
		if (n.getId()!= nlast.getId()) {
			ns = ns.subList(n.getNewsOrder()-1, ns.size());
			for(News ne:ns) {
				ne.setNewsOrder(ne.getNewsOrder()-1);
				newsService.update(ne);
			}
		}	
		n.setNewsOrder(1);
		n.setUp(newsService.art);
		newsService.update(n);
		return Result.success();
	}
	
	@GetMapping(value="/news/{id}/down")
	public Object down(@PathVariable("id") int id) throws Exception{
		News n = newsService.get(id);
		int order = n.getNewsOrder();
		
		List<News> ns = newsService.listByUp(newsService.top);
		News nlast = ns.get(ns.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(n.getId()!= nlast.getId()) {
			//元素后一位元素往前移动1位
			News nnext = newsService.listByUpAndOrder(newsService.top,order+1).get(0);
			nnext.setNewsOrder(order);
			newsService.update(nnext);
			//元素本身往后移动1位
			order++;
			n.setNewsOrder(order);
			newsService.update(n);
		}
		return Result.success();
	}
	
	@GetMapping(value="/news/{id}/up")
	public Object up(@PathVariable("id") int id) throws Exception{
		News n = newsService.get(id);
		int order = n.getNewsOrder();
		
		List<News> ns = newsService.listByUp(newsService.top);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			News npre = newsService.listByUpAndOrder(newsService.top,order-1).get(0);
			npre.setNewsOrder(order);
			newsService.update(npre);
			//元素本身往前移动1位
			order--;
			n.setNewsOrder(order);
			newsService.update(n);
		}
		return Result.success();
	}
	
	@GetMapping(value="/news/{id}/hide")
	public Object hide(@PathVariable("id") int id) throws Exception{
		News n = newsService.get(id);
		int order = n.getNewsOrder();
		if(n.getUp()==newsService.top) {
			List<News> ns = newsService.listByUpAndOrderNot(newsService.top, newsService.hide);//获取top列表
			News nlast = ns.get(ns.size()-1); //获取top列表最后一位元素
			 //判读是否是最后一位元素
			if (n.getId()!= nlast.getId()) {
				ns = ns.subList(n.getNewsOrder()-1, ns.size());	
				for(News ne:ns) {
					ne.setNewsOrder(ne.getNewsOrder()-1);
					newsService.update(ne);
				}
			}	
		}
		n.setNewsOrder(0);
		newsService.update(n);
		return Result.success();
	}
	
	@GetMapping(value="/news/{id}/show")
	public Object show(@PathVariable("id") int id) throws Exception{
		News n = newsService.get(id);
		int order = n.getNewsOrder();
		if(n.getUp()==newsService.top) {
			List<News> ns = newsService.listByUpAndOrderNot(newsService.top, newsService.hide);
			for(News ne:ns) {
				ne.setNewsOrder(ne.getNewsOrder()+1);
				newsService.update(ne);
			}
		}
		order=1;
		n.setNewsOrder(order);
		newsService.update(n);
		return Result.success();
	}
	
	@PutMapping(value="/news/{id}/image")
	public Object imageUpdate(@PathVariable("id") int id, HttpServletRequest request, MultipartFile image) throws Exception {
		News n = newsService.get(id);
		n.setImg(1);
		newsService.update(n);

		File imageFolder = new File(new Filepath().path()+"img/news");
		File file = new File(imageFolder, n.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		return Result.success();
	}

}
