package com.dongzhi.ow.web;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
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

import com.dongzhi.ow.pojo.People;
import com.dongzhi.ow.pojo.Social;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.service.PeopleService;
import com.dongzhi.ow.service.SocialService;
import com.dongzhi.ow.service.TypeService;
import com.dongzhi.ow.util.Filepath;
import com.dongzhi.ow.util.ImageUtil;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     PeopleController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:16:06
 */
@RestController
public class PeopleController {

	@Autowired PeopleService peopleService;
	@Autowired SocialService socialService;
	@Autowired TypeService typeService;
	
	@GetMapping("/types/{tid}/peoples")
	public Object list(@PathVariable("tid") int tid) throws Exception{
		List<People> beans = peopleService.listByType(tid);
		return Result.success(beans);
	}
	
	@PostMapping("/peoples")
	public Object add( People bean, HttpServletRequest request, MultipartFile image) throws Exception {
		int order;
		int tid = bean.getType().getId();
		List<People> ps = peopleService.listByType(tid);
		if(ps.isEmpty())
			order = 0;
		else
			order = ps.get(ps.size()-1).getPeopleOrder();
		order++;
		bean.setPeopleOrder(order);
		peopleService.add(bean);
		
		File imageFolder = new File(new Filepath().path()+"img/peopleLogo");
		File file = new File(imageFolder, bean.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		ImageUtil.resizeImage(file, 120, 120, file);
		return Result.success();
	}
	
	@DeleteMapping("/peoples/{id}")
	public Object delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
		People p = peopleService.get(id);
		
		//判断是否可以删除，正式删除前排序
		List<Social> ss = socialService.listByPeople(p.getId());
		if(ss.isEmpty()) {
			int tid = p.getType().getId();
			List<People> ps = peopleService.listByType(tid);
			People plast = ps.get(ps.size()-1); //获取最后一位元素
			 //判读是否是最后一位元素
			if (p.getId()!=plast.getId()) {
				ps = ps.subList(p.getPeopleOrder()-1, ps.size());	
				for(People pe:ps) {
					pe.setPeopleOrder(pe.getPeopleOrder()-1);
					peopleService.update(pe);
				}
			}
		}
		peopleService.delete(id);
		//删除对应图片
		File imageFolder = new File(new Filepath().path()+"img/peopleLogo");
		File file = new File(imageFolder, p.getId()+".jpg");
		file.delete();
		return Result.success("");
	}
	
	
//	@RequestMapping("admin_people_dodelete")
//	@ResponseBody
//	public Object dodelete(int id) {
//		List<Social> ss = socialService.list(id);
//		if(!ss.isEmpty()) {
//			return "false";
//		}
//		return "true";
//	}
	
	@GetMapping("/peoples/{id}")
	public Object get(@PathVariable("id") int id) {
		People bean = peopleService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping("/peoples")
	public Object update(@RequestBody People bean, @RequestParam(name="oldTid") int oldTid) throws Exception{
		//判断是否更换分类
		if(bean.getType().getId()!=oldTid) {
			int order = bean.getPeopleOrder();
			
			//旧列表元素之后均往前移1位
			People pod = peopleService.listByTypeAndOrder(oldTid,order).get(0); //获取更新前的元素
			List<People> pods = peopleService.listByType(oldTid);	//获取旧列表
			People podlast = pods.get(pods.size()-1);	//获取旧列表最后一位元素	
			if (pod.getId()!= podlast.getId()) //判读是否是最后一位元素
				pods = pods.subList(pod.getPeopleOrder()-1, pods.size());	
			else
				pods = pods.subList(pods.size(), pods.size());
			
			for(People po:pods) {
				po.setPeopleOrder(po.getPeopleOrder()-1);
				peopleService.update(po);
			}
			
			//获取新列表序号
			int tid = bean.getType().getId();
			List<People> pns = peopleService.listByType(tid);
			if(pns.isEmpty())
				order = 0;
			else
				order = pns.get(pns.size()-1).getPeopleOrder();
			order++;
			bean.setPeopleOrder(order);
		}
		peopleService.update(bean);

		return Result.success();
	}
	
	@GetMapping("/peoples/{id}/down")
	public Object down(@PathVariable("id") int id) throws Exception{
		People p = peopleService.get(id);
		int order = p.getPeopleOrder();
		int tid = p.getType().getId();
		
		List<People> ps = peopleService.listByType(tid);
		People plast = ps.get(ps.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(p.getId()!=plast.getId()) {
			//元素后一位元素往前移动1位
			People pnext = peopleService.listByTypeAndOrder(tid,order+1).get(0);
			pnext.setPeopleOrder(order);
			peopleService.update(pnext);
			//元素本身往后移动1位
			order++;
			p.setPeopleOrder(order);
			peopleService.update(p);
		}
		return Result.success();
	}
	
	@GetMapping("/peoples/{id}/up")
	public Object up(@PathVariable("id") int id) throws Exception{
		People p = peopleService.get(id);
		int order = p.getPeopleOrder();
		int tid = p.getType().getId();
		
		List<People> ps = peopleService.listByType(tid);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			People ppre = peopleService.listByTypeAndOrder(tid,order-1).get(0);
			ppre.setPeopleOrder(order);
			peopleService.update(ppre);
			//元素本身往前移动1位
			order--;
			p.setPeopleOrder(order);
			peopleService.update(p);
		}
		return Result.success();
	}
	
	@PutMapping("/peoples/{id}/image")
	public Object imageUpdate(@PathVariable("id") int id, HttpServletRequest request, MultipartFile image) throws IOException {
		People p = peopleService.get(id);
		
		File imageFolder = new File(new Filepath().path()+"img/peopleLogo");
		File file = new File(imageFolder, p.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		ImageUtil.resizeImage(file, 120, 120, file);
		return Result.success();
	}
}
