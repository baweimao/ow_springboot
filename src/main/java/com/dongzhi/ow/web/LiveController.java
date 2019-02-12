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

import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.Live;
import com.dongzhi.ow.pojo.Web;
import com.dongzhi.ow.service.LiveService;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     LiveController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:15:36
 */
@RestController
public class LiveController {

	@Autowired LiveService liveService;
	
	@GetMapping("/games/{gid}/lives")
	public Object list(@PathVariable("gid") int gid) throws Exception{
		List<Live> beans = liveService.listByGame(gid);
		return Result.success(beans);
	}
	
	@PostMapping("/lives")
	public Object add(@RequestBody Live bean) throws Exception{
		int gid = bean.getGame().getId();
		int order;
		List<Live> ls = liveService.listByGame(gid);
		if(ls.isEmpty())
			order = 0;
		else
			order = ls.get(ls.size()-1).getLiveOrder();
		order++;
		bean.setLiveOrder(order);
		liveService.add(bean);
		return Result.success(bean);
	}
	
	@DeleteMapping("/lives/{id}")
	public Object delete(@PathVariable("id") int id) throws Exception{
		//正式删除前排序
		Live l = liveService.get(id);
		int gid = l.getGame().getId();
		List<Live> ls = liveService.listByGame(gid);
		Live llast = ls.get(ls.size()-1); //获取最后一位元素
		 //判读是否是最后一位元素
		if (l.getId()!=llast.getId()) {
			ls = ls.subList(l.getLiveOrder()-1, ls.size());	
			for(Live le:ls) {
				le.setLiveOrder(le.getLiveOrder()-1);
				liveService.update(le);
			}
		}
		liveService.delete(id);
		return Result.success("");
	}

	@GetMapping("/lives/{id}")
	public Object get(@PathVariable("id") int id) throws Exception{
		Live bean = liveService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping("/lives")
	public Object update(@RequestBody Live bean) throws Exception{
		int gid = bean.getGame().getId();
		liveService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping("/lives/{id}/down")
	public Object down(@PathVariable("id") int id) throws Exception{
		Live l = liveService.get(id);
		int gid = l.getGame().getId();
		int order = l.getLiveOrder();

		List<Live> ls = liveService.listByGame(gid);
		Live llast = ls.get(ls.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(l.getId()!=llast.getId()) {
			//元素后一位元素往前移动1位
			Live lnext = liveService.listByGameAndOrder(gid, order+1).get(0);
			lnext.setLiveOrder(order);
			liveService.update(lnext);
			//元素本身往后移动1位
			order++;
			l.setLiveOrder(order);
			liveService.update(l);
		}
		return Result.success();
	}
	
	@GetMapping("/lives/{id}/up")
	public Object up(@PathVariable("id") int id) throws Exception{
		Live l = liveService.get(id);
		int gid = l.getGame().getId();
		int order = l.getLiveOrder();
		
		List<Live> ls = liveService.listByGame(gid);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			Live lpre = liveService.listByGameAndOrder(gid, order-1).get(0);
			lpre.setLiveOrder(order);
			liveService.update(lpre);
			//元素本身往前移动1位
			order--;
			l.setLiveOrder(order);
			liveService.update(l);
		}
		return Result.success();
	}
}
