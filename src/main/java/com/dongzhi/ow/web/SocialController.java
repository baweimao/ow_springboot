package com.dongzhi.ow.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Social;
import com.dongzhi.ow.pojo.Type;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.service.PeopleService;
import com.dongzhi.ow.service.SocialService;
import com.dongzhi.ow.service.TypeService;
import com.dongzhi.ow.service.WebService;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     SocialController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:16:31
 */
@RestController
public class SocialController {

	@Autowired SocialService socialService;
	@Autowired PeopleService peopleService;
	@Autowired WebService webService;
	@Autowired TypeService typeService;
	
	@GetMapping("/peoples/{pid}/socials")
	public Object list(@PathVariable("pid") int pid) throws Exception{
		List<Social> beans = socialService.listByPeople(pid);		
		return Result.success(beans);
	}
	
	@PostMapping("/socials")
	public Object add(@RequestBody Social bean) throws Exception{
		int order;
		int pid = bean.getPeople().getId();
		List<Social> ss = socialService.listByPeople(pid);
		if(ss.isEmpty())
			order = 0;
		else
			order = ss.get(ss.size()-1).getSocialOrder();
		order++;
		bean.setSocialOrder(order);
		socialService.add(bean);
		return Result.success(bean);
	}
	
	@DeleteMapping("/socials/{id}")
	public Object delete(@PathVariable("id") int id) throws Exception{
		Social s = socialService.get(id);
		int pid = s.getPeople().getId();
		List<Social> ss = socialService.listByPeople(pid);
		Social slast = ss.get(ss.size()-1); //获取最后一位元素
		 //判读是否是最后一位元素
		if (s.getId()!=slast.getId()) {
			ss = ss.subList(s.getSocialOrder()-1, ss.size());	
			for(Social se:ss) {
				se.setSocialOrder(se.getSocialOrder()-1);
				socialService.update(se);
			}
		}
		socialService.delete(id);
		return Result.success("");
	}
	
	@GetMapping("/socials/{id}")
	public Object get(@PathVariable("id") int id) throws Exception{
		Social bean = socialService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping("/socials")
	public Object update(@RequestBody Social bean) {
		socialService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping("/socials/{id}/down")
	public Object down(@PathVariable("id") int id) {
		Social s = socialService.get(id);
		int pid = s.getPeople().getId();
		int order = s.getSocialOrder();

		List<Social> ss = socialService.listByPeople(pid);
		Social slast = ss.get(ss.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(s.getId()!=slast.getId()) {
			//元素后一位元素往前移动1位
			Social snext = socialService.listByPeopleAndOrder(pid,order+1).get(0);
			snext.setSocialOrder(order);
			socialService.update(snext);
			//元素本身往后移动1位
			order++;
			s.setSocialOrder(order);
			socialService.update(s);
		}
		return Result.success();
	}
	
	@GetMapping("/socials/{id}/up")
	public Object up(@PathVariable("id") int id) {
		Social s = socialService.get(id);
		int pid = s.getPeople().getId();
		int order = s.getSocialOrder();

		List<Social> ss = socialService.listByPeople(pid);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			Social spre = socialService.listByPeopleAndOrder(pid,order-1).get(0);
			spre.setSocialOrder(order);
			socialService.update(spre);
			//元素本身往前移动1位
			order--;
			s.setSocialOrder(order);
			socialService.update(s);
		}
		return Result.success();
	}

}
