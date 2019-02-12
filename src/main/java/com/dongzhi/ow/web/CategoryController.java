package com.dongzhi.ow.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.service.CategoryService;
import com.dongzhi.ow.service.WebService;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     CategoryController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:07:41
 */
@RestController
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	WebService webService;

	@GetMapping("/categories")
	public Object list() throws Exception{
		List<Category> beans = categoryService.list();
		return Result.success(beans);
	}
	
	@PostMapping("/categories")
	public Object add(@RequestBody Category bean) throws Exception{
		int order;
		List<Category> beans = categoryService.list();
		if(beans.isEmpty())
			order = 0;
		else
			order = beans.get(beans.size()-1).getCategoryOrder();
		order++;
		bean.setCategoryOrder(order);
		categoryService.add(bean);
		return Result.success(bean);
	}
	
	@DeleteMapping("/categories/{id}")
	public Object delete(@PathVariable("id") int id) throws Exception{
		Category c = categoryService.get(id);
		//判断是否可以删除
		List<Web> ws = webService.listByCategory(c.getId());
		if(ws.isEmpty()) {
			List<Category> cs = categoryService.listByOrderNot(categoryService.hide);
			Category clast = cs.get(cs.size()-1); //获取最后一位元素
			 //判读是否是最后一位元素
			if (c.getId()!=clast.getId()&&c.getCategoryOrder()!=0) {
				cs = cs.subList(c.getCategoryOrder()-1, cs.size());	
				for(Category ce:cs) {
					ce.setCategoryOrder(ce.getCategoryOrder()-1);
					categoryService.update(ce);
				}
			}
		}
		categoryService.delete(id);
		return Result.success("");
	}
	
	@GetMapping("/categories/{id}")
	public Object get(@PathVariable("id") int id) throws Exception{
		Category c = categoryService.get(id);
		return Result.success(c);
	}
	
	@PutMapping("/categories")
	public Object update(@RequestBody Category bean) throws Exception{
        categoryService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping("/categories/{id}/down")
	public Object down(@PathVariable("id") int id) throws Exception{
		Category c = categoryService.get(id);
		int order = c.getCategoryOrder();

		List<Category> cs = categoryService.list();
		Category clast = cs.get(cs.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(c.getId()!=clast.getId()) {
			//元素后一位元素往前移动1位
			Category cnext = categoryService.listByOrder(order+1).get(0);
			cnext.setCategoryOrder(order);
			categoryService.update(cnext);
			//元素本身往后移动1位
			order++;
			c.setCategoryOrder(order);
			categoryService.update(c);
		}
		return Result.success();
	}
	
	@GetMapping("/categories/{id}/up")
	public Object up(@PathVariable("id") int id) throws Exception{
		Category c = categoryService.get(id);
		int order = c.getCategoryOrder();
		
		List<Category> cs = categoryService.list();
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			Category cpre = categoryService.listByOrder(order-1).get(0);
			cpre.setCategoryOrder(order);
			categoryService.update(cpre);
			//元素本身往前移动1位
			order--;
			c.setCategoryOrder(order);
			categoryService.update(c);
		}
		return Result.success();
	}
	
	@GetMapping("/categories/{id}/hide")
	public Object hide(@PathVariable("id") int id) throws Exception{
		Category c = categoryService.get(id);
		int order = c.getCategoryOrder();
		
		List<Category> cs = categoryService.listByOrderNot(categoryService.hide);
		Category clast = cs.get(cs.size()-1); //获取最后一位元素
		 //判读是否是最后一位元素
		if (c.getId()!=clast.getId()) {
			cs = cs.subList(c.getCategoryOrder()-1, cs.size());	
			for(Category ce:cs) {
				ce.setCategoryOrder(ce.getCategoryOrder()-1);
				categoryService.update(ce);
			}
		}
		order=0;
		c.setCategoryOrder(order);
		categoryService.update(c);
		return Result.success();
	}
	
	@GetMapping("/categories/{id}/show")
	public Object show(@PathVariable("id") int id) throws Exception{
		Category c = categoryService.get(id);

		List<Category> cs = categoryService.listByOrderNot(categoryService.hide);
		for(Category ce:cs) {
			ce.setCategoryOrder(ce.getCategoryOrder()+1);
			categoryService.update(ce);
		}
		c.setCategoryOrder(1);
		categoryService.update(c);
		return Result.success();
	}
}
