package com.dongzhi.ow.web;

import java.awt.image.BufferedImage;

import java.io.File;
import java.util.List;

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

import com.dongzhi.ow.pojo.Article;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.Live;
import com.dongzhi.ow.pojo.News;
import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Social;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.service.ArticleService;
import com.dongzhi.ow.service.LiveService;
import com.dongzhi.ow.service.NewsService;
import com.dongzhi.ow.service.PeopleService;
import com.dongzhi.ow.service.SocialService;
import com.dongzhi.ow.service.WebService;
import com.dongzhi.ow.util.Filepath;
import com.dongzhi.ow.util.ImageUtil;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     WebController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:17:12
 */
@RestController
public class WebController {
	
	@Autowired WebService webService;
	@Autowired NewsService newsService;
	@Autowired ArticleService articleService;
	@Autowired SocialService socialService;
	@Autowired LiveService liveService;
	
	@GetMapping("/categories/{cid}/webs")
	public Object list(@PathVariable("cid") int cid) throws Exception{
		List<Web> beans = webService.listByCategory(cid);
		return Result.success(beans);
	}
	
	@GetMapping("/webs")
	public Object list() throws Exception{
		List<Web> beans = webService.list();
		return Result.success(beans);
	}
	
	@PostMapping("/webs")
	public Object add(Web bean, HttpServletRequest request, MultipartFile image) throws Exception {
		int order;
		int cid = bean.getCategory().getId();
		List<Web> ws = webService.listByCategory(cid);
		if(ws.isEmpty())
			order = 0;
		else
			order = ws.get(ws.size()-1).getWebOrder();
		order++;
		bean.setWebOrder(order);
		webService.add(bean);
		
		//图片上传
		File imageFolder = new File(new Filepath().path()+"img/webLogo");
		File file = new File(imageFolder, bean.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		ImageUtil.resizeImage(file, 50, 50, file);
		return Result.success(bean);
	}
	
	@DeleteMapping("/webs/{id}")
	public Object delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
		Web w = webService.get(id);
		
		//判断是否可以删除
		List<News> ns = newsService.list();
		List<Article> as = articleService.list();
		List<Social> ss = socialService.list();
		List<Live> ls = liveService.list();
		for(News n : ns) {
			if(n.getWeb().equals(w))
				webService.delete(id);
		}
		for(Article a : as) {
			if(a.getWeb().equals(w))
				webService.delete(id);
		}
		for(Live l : ls) {
			if(l.getWeb().equals(w))
				webService.delete(id);
		}
		for(Social s : ss) {
			if(s.getWeb().equals(w))
				webService.delete(id);
		}
		
		//微信不可删除
		if(id==44)
			return Result.fail("默认数据，无法删除");
		
		//正式删除前排序
		List<Web> ws = webService.listByCategory(w.getCategory().getId());
		Web wlast = ws.get(ws.size()-1); //获取最后一位元素
		 //判读是否是最后一位元素
		if (w.getId()!=wlast.getId()) {
			ws = ws.subList(w.getWebOrder()-1, ws.size());	
			for(Web we:ws) {
				we.setWebOrder(we.getWebOrder()-1);
				webService.update(we);
			}
		}
		
		webService.delete(id);
		//删除对应图片
		File imageFolder = new File(new Filepath().path()+"img/webLogo");
		File file = new File(imageFolder, w.getId()+".jpg");
		file.delete();
		return Result.success("");
	}
	
	@GetMapping("/webs/{id}")
	public Object get(@PathVariable("id") int id) {
		Web w = webService.get(id);
		return Result.success(w);
	}
	
	@PutMapping("/webs")
	public Object update(@RequestBody Web bean, @RequestParam(name="oldCid") int oldCid) throws Exception{
		//判断是否更换分类
		if(bean.getCategory().getId()!=oldCid) {
			int order = bean.getWebOrder();
			
			//旧列表元素之后均往前移1位
			Web wod = webService.listByCategoryAndOrder(oldCid,order).get(0); //获取更新前的元素
			List<Web> wods = webService.listByCategory(oldCid);	//获取旧列表
			Web wodlast = wods.get(wods.size()-1);	//获取旧列表最后一位元素	
			if (wod.getId()!= wodlast.getId()) //判读是否是最后一位元素
				wods = wods.subList(wod.getWebOrder()-1, wods.size());	
			else
				wods = wods.subList(wods.size(), wods.size());
			
			for(Web wo:wods) {
				wo.setWebOrder(wo.getWebOrder()-1);
				webService.update(bean);
			}
			
			//获取新列表序号
			int cid = bean.getCategory().getId();
			List<Web> wns = webService.listByCategory(cid);
			if(wns.isEmpty())
				order = 0;
			else
				order = wns.get(wns.size()-1).getWebOrder();
			order++;
			bean.setWebOrder(order);
		}
		webService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping("/webs/{id}/down")
	public Object down(@PathVariable("id") int id) throws Exception{
		Web w = webService.get(id);
		int cid = w.getCategory().getId();
		int order = w.getWebOrder();
		
		List<Web> ws = webService.listByCategory(cid);
		Web wlast = ws.get(ws.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(w.getId()!=wlast.getId()) {
			//元素后一位元素往前移动1位
			Web wnext = webService.listByCategoryAndOrder(cid,order+1).get(0);
			wnext.setWebOrder(order);
			webService.update(wnext);
			//元素本身往后移动1位
			order++;
			w.setWebOrder(order);
			webService.update(w);
		}
		return Result.success();
	}
	
	@GetMapping("/webs/{id}/up")
	public Object up(@PathVariable("id") int id) throws Exception{
		Web w = webService.get(id);
		int cid = w.getCategory().getId();
		int order = w.getWebOrder();
		
		List<Web> ws = webService.listByCategory(cid);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			Web wpre = webService.listByCategoryAndOrder(cid,order-1).get(0);
			wpre.setWebOrder(order);
			webService.update(wpre);
			//元素本身往前移动1位
			order--;
			w.setWebOrder(order);
			webService.update(w);
		}
		return Result.success();
	}
	
	@PutMapping("/webs/{id}/image")
	public Object imageUpdate(@PathVariable("id") int id, HttpServletRequest request, MultipartFile image) throws Exception{
		Web w = webService.get(id);
		int cid = w.getCategory().getId();
		
		//图片上传
		File imageFolder = new File(new Filepath().path()+"/img/webLogo");
		System.out.println(imageFolder.getPath());
		File file = new File(imageFolder, w.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		ImageUtil.resizeImage(file, 50, 50, file);
		return Result.success();
	}
}
