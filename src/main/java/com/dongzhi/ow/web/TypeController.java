package com.dongzhi.ow.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Type;

import com.dongzhi.ow.service.PeopleService;
import com.dongzhi.ow.service.TypeService;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     TypeController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:16:42
 */
@RestController
public class TypeController {

	@Autowired TypeService typeService;
	@Autowired PeopleService peopleService;
	
	@GetMapping("/types")
	public Object list() throws Exception{
		List<Type> beans = typeService.list();
		return Result.success(beans);
	}
	
	@PostMapping("/types")
	public Object add(@RequestBody Type bean) throws Exception{
		int order;
		List<Type> ts = typeService.list();
		if(ts.isEmpty())
			order = 1;
		else
			order = ts.get(ts.size()-1).getTypeOrder();
		order++;
		bean.setTypeOrder(order);
		typeService.add(bean);
		return Result.success(bean);
	}
	
	@DeleteMapping("/types/{id}")
	public Object delete(@PathVariable("id") int id) {
		Type t = typeService.get(id);
		
		//判断是否可以删除，正式删除前排序
		List<People> ps = peopleService.listByType(t.getId());
		if(ps.isEmpty()) {
			List<Type> ts = typeService.listByOrderNot(typeService.hide);
			Type tlast = ts.get(ts.size()-1); //获取top列表最后一位元素
			 //判断是否是最后一位元素
			if (t.getId()!=tlast.getId()&&t.getTypeOrder()!=0) {
				ts = ts.subList(t.getTypeOrder()-1, ts.size());	
				for(Type te:ts) {
					te.setTypeOrder(te.getTypeOrder()-1);
					typeService.update(te);
				}
			}
		}
		typeService.delete(id);
		return Result.success("");
	}
	
	@GetMapping("/types/{id}")
	public Object get(@PathVariable("id") int id) {
		Type bean = typeService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping("/types")
	public Object update(@RequestBody Type bean) {
		typeService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping("/types/{id}/down")
	public Object down(@PathVariable("id") int id) {
		Type t = typeService.get(id);
		int order = t.getTypeOrder();

		List<Type> ts = typeService.list();
		Type tlast = typeService.get(ts.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(t.getId()!=tlast.getId()) {
			//元素后一位元素往前移动1位
			Type tnext = typeService.listByOrder(order+1).get(0);
			tnext.setTypeOrder(order);
			typeService.update(tnext);
			//元素本身往后移动1位
			order++;
			t.setTypeOrder(order);
			typeService.update(t);
		}
		return Result.success();
	}
	
	@GetMapping("/types/{id}/up")
	public Object up(@PathVariable("id") int id) {
		Type t = typeService.get(id);
		int order = t.getTypeOrder();
		List<Type> ts = typeService.list();
		if(order > 1) {
			//元素前一位元素往后移动1位
			Type tpre = typeService.listByOrder(order-1).get(0);
			tpre.setTypeOrder(order);
			typeService.update(tpre);
			//元素本身往前移动1位
			order--;
			t.setTypeOrder(order);
			typeService.update(t);
		}
		return Result.success();
	}
	
	@GetMapping("/types/{id}/hide")
	public Object hide(@PathVariable("id") int id) {
		Type t = typeService.get(id);
		int order = t.getTypeOrder();

		List<Type> ts = typeService.listByOrderNot(typeService.hide);
		Type tlast = ts.get(ts.size()-1); //获取top列表最后一位元素
		 //判断是否是最后一位元素
		if (t.getId()!=tlast.getId()) {
			ts = ts.subList(t.getTypeOrder()-1, ts.size());	
			for(Type te:ts) {
				te.setTypeOrder(te.getTypeOrder()-1);
				typeService.update(te);
			}
		}
		order=0;
		t.setTypeOrder(order);
		typeService.update(t);
		return Result.success();
	}
	
	@RequestMapping("/types/{id}/show")
	public Object show(@PathVariable("id") int id) {
		Type t = typeService.get(id);

		List<Type> ts = typeService.listByOrderNot(typeService.hide);
		for(Type te:ts) {
			te.setTypeOrder(te.getTypeOrder()+1);
			typeService.update(te);
		}
		t.setTypeOrder(1);
		typeService.update(t);
		return Result.success();
	}
}

