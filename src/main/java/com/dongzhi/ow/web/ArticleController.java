package com.dongzhi.ow.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongzhi.ow.pojo.Article;
import com.dongzhi.ow.service.ArticleService;
import com.dongzhi.ow.service.WebService;
import com.dongzhi.ow.util.Page4Navigator;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     ArticleController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:05:59
 */
@RestController
public class ArticleController {
	
	@Autowired ArticleService articleService;
	@Autowired WebService webService;
	
	@GetMapping("/articles")
	public Object list(@RequestParam(value = "start", defaultValue = "0") int start,@RequestParam(value = "size", defaultValue = "5") int size) throws Exception{

		//推荐文章集合
		List<Article> asRec = articleService.listByUp(articleService.recommend);
		
		//置顶文章集合
		List<Article> asTop = articleService.listByUp(articleService.top);
		
		//正常文章集合
		Page4Navigator<Article> page = articleService.listByUp(articleService.art, start, size, 5);

		Map<String, Object> map = new HashMap<>();
		map.put("asRec", asRec);
		map.put("asTop", asTop);
		map.put("page", page);
		return Result.success(map);
	}
	
	@PostMapping(value="/articles")
	public Object add(@RequestBody Article bean) throws Exception{
		Date d = new Date();
		int up = 0;
		bean.setArticleDate(d);
		bean.setArticleOrder(1);
		bean.setUp(up);
		articleService.add(bean);
		return Result.success(bean);
	}
	
	@DeleteMapping(value="/articles/{id}")
	public Object delete(@PathVariable("id") int id) throws Exception{
		//正式删除前排序
		Article a = articleService.get(id);
		int up = a.getUp();
		if(up!=articleService.art) {
			List<Article> as = articleService.listByUpAndOrderNot(up, articleService.hide);//获取列表
			Article alast = as.get(as.size()-1); //获取列表最后一位元素
			 //判读是否是最后一位元素
			if (a.getId()!= alast.getId()) {
				as = as.subList(a.getArticleOrder()-1, as.size());	
				for(Article ae:as) {
					ae.setArticleOrder(ae.getArticleOrder()-1);
					articleService.update(ae);
				}
			}	
		}
		articleService.delete(id);
		return Result.success("");
	}
	
	@GetMapping(value="/articles/{id}")
	public Object get(@PathVariable("id") int id) throws Exception{
		Article bean = articleService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping(value="/articles")
	public Object update(@RequestBody Article bean) {
		articleService.update(bean);
		return Result.success(bean);
	}
	
	//文章置顶
	@GetMapping(value="/articles/{id}/top")
	public Object top(@PathVariable("id") int id) throws Exception{
		Article a = articleService.get(id);
		int up = a.getUp();
		//如果原表是推荐循环减1
		if(up == articleService.recommend) {
			List<Article> as = articleService.listByUp(up);//获取列表
			Article alast = as.get(as.size()-1); //获取列表最后一位元素
			 //判读是否是最后一位元素
			if (a.getId()!= alast.getId()) {
				as = as.subList(a.getArticleOrder()-1, as.size());	
				for(Article ae:as) {
					ae.setArticleOrder(ae.getArticleOrder()-1);
					articleService.update(ae);
				}
			}
		}
		
		//新表加1
		List<Article> as = articleService.listByUpAndOrderNot(articleService.top, articleService.hide);//获取列表
		for(Article ae:as) {
			ae.setArticleOrder(ae.getArticleOrder()+1);
			articleService.update(ae);
		}
		
		a.setArticleOrder(1);
		a.setUp(articleService.top);
		articleService.update(a);
		return Result.success();
	}
	
	//文章推荐
	@GetMapping(value="/articles/{id}/recommend")
	public Object recommend (@PathVariable("id") int id) throws Exception{
		Article a = articleService.get(id);
		int up = a.getUp();
		
		//如果原表是置顶循环减1
		if(up == articleService.top) {
			List<Article> as = articleService.listByUpAndOrderNot(up, articleService.hide);//获取列表
			Article alast = as.get(as.size()-1); //获取列表最后一位元素
			 //判读是否是最后一位元素
			if (a.getId()!= alast.getId()) {
				as = as.subList(a.getArticleOrder()-1, as.size());	
				for(Article ae:as) {
					ae.setArticleOrder(ae.getArticleOrder()-1);
					articleService.update(ae);
				}
			}
		}
		
		//新表加1
		List<Article> as = articleService.listByUpAndOrderNot(articleService.recommend, articleService.hide);//获取列表
		for(Article ae:as) {
			ae.setArticleOrder(ae.getArticleOrder()+1);
			articleService.update(ae);
		}
		a.setArticleOrder(1);
		a.setUp(articleService.recommend);
		articleService.update(a);
		return Result.success();
	}
	
	//普通文章
	@GetMapping(value="/articles/{id}/art")
	public Object art(@PathVariable("id") int id) throws Exception{
		Article a = articleService.get(id);
		int order = a.getArticleOrder();
		int up = a.getUp();//获取类别
		List<Article> as = articleService.listByUpAndOrderNot(up, articleService.hide);//获取列表
		Article alast = as.get(as.size()-1); //获取列表最后一位元素
		 //判读是否是最后一位元素
		if (a.getId()!= alast.getId()) {
			as = as.subList(a.getArticleOrder()-1, as.size());
			for(Article ae:as) {
				ae.setArticleOrder(ae.getArticleOrder()-1);
				articleService.update(ae);
			}
		}	
		a.setArticleOrder(1);
		a.setUp(articleService.art);
		articleService.update(a);
		return Result.success();
	}
	
	@GetMapping(value="/articles/{id}/down")
	public Object down(@PathVariable("id") int id) throws Exception{
		Article a = articleService.get(id);
		int up = a.getUp();//获取类别
		int order = a.getArticleOrder();

		List<Article> as = articleService.listByUpAndOrderNot(up, articleService.hide);
		Article alast = as.get(as.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(a.getId()!= alast.getId()) {
			//元素后一位元素往前移动1位
			Article anext = articleService.listByUpAndOrder(up,order+1).get(0);
			anext.setArticleOrder(order);
			articleService.update(anext);
			//元素本身往后移动1位
			order++;
			a.setArticleOrder(order);
			articleService.update(a);
		}
		return Result.success();
	}
	
	@GetMapping(value="/articles/{id}/up")
	public Object up(@PathVariable("id") int id) throws Exception{
		Article a = articleService.get(id);
		int up = a.getUp();//获取类别
		int order = a.getArticleOrder();
		
		List<Article> as = articleService.listByUpAndOrderNot(up, articleService.hide);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			System.out.println(up);
			System.out.println(order);
			Article apre = articleService.listByUpAndOrder(up,order-1).get(0);
			apre.setArticleOrder(order);
			articleService.update(apre);
			//元素本身往前移动1位
			order--;
			a.setArticleOrder(order);
			articleService.update(a);
		}
		return Result.success();
	}
	
	@GetMapping(value="/articles/{id}/hide")
	public Object hide(@PathVariable("id") int id) throws Exception{
		Article a = articleService.get(id);
		int order = a.getArticleOrder();
		int up = a.getUp();
		if(up!=articleService.art) {
			List<Article> as = articleService.listByUpAndOrderNot(up, articleService.hide);//获取列表
			Article alast = as.get(as.size()-1); //获取列表最后一位元素
			 //判读是否是最后一位元素
			if (a.getId()!= alast.getId()) {
				as = as.subList(a.getArticleOrder()-1, as.size());	
				for(Article ae:as) {
					ae.setArticleOrder(ae.getArticleOrder()-1);
					articleService.update(ae);
				}
			}	
		}
		a.setArticleOrder(0);
		articleService.update(a);
		return Result.success();
	}
	
	@GetMapping(value="/articles/{id}/show")
	public Object show(@PathVariable("id") int id) throws Exception{
		Article a = articleService.get(id);
		int order = a.getArticleOrder();
		int up = a.getUp();
		if(up!=articleService.art) {
			List<Article> as = articleService.listByUpAndOrderNot(up, articleService.hide);
			for(Article ae:as) {
				ae.setArticleOrder(ae.getArticleOrder()+1);
				articleService.update(ae);
			}
		}
		a.setArticleOrder(1);
		articleService.update(a);
		return Result.success();
	}
	
//	/**
//	 * UMeditor 图片上传jsp转java
//	 * 
//	 **/
//	@ResponseBody   
//	@RequestMapping("imageUp")
//	public String imageUp(MultipartFile upfile, HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap) {
// 
//		Uploader up = new Uploader(request);
//	    up.setSavePath("upload");
//	    String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
//	    up.setAllowFiles(fileType);
//	    up.setMaxSize(10000); //单位KB
//	    try {
//			up.upload(upfile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
// 
//	    String callback = request.getParameter("callback");
// 
//	    String result = "{\"name\":\""+ up.getFileName() +"\", \"originalName\": \""+ up.getOriginalName() +"\", \"size\": "+ up.getSize() +", \"state\": \""+ up.getState() +"\", \"type\": \""+ up.getType() +"\", \"url\": \""+ up.getUrl() +"\"}";
// 
//	    result = result.replaceAll( "\\\\", "\\\\" );
// 
//	    if(callback == null ){
//	        return result ;
//	    }else{
//	       return "<script>"+ callback +"(" + result + ")</script>";
//	    }
//	}
	


}
