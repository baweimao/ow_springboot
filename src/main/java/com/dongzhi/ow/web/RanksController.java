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

import com.dongzhi.ow.pojo.GameTable;
import com.dongzhi.ow.pojo.Ranks;
import com.dongzhi.ow.service.GameTableService;
import com.dongzhi.ow.service.RanksService;
import com.dongzhi.ow.util.Filepath;
import com.dongzhi.ow.util.ImageUtil;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     RanksController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:16:21
 */
@RestController
public class RanksController {

	private static final boolean GameTable = false;
	@Autowired RanksService ranksService;
	@Autowired GameTableService gameTableService;
	
	@GetMapping("/games/{gid}/ranks")
	public Object list(@PathVariable("gid") int gid) throws Exception{
		List<Ranks> beans = ranksService.listByGame(gid);
		return Result.success(beans);
	}
	
	@GetMapping("/ranks")
	public Object list() throws Exception{
		List<Ranks> beans = ranksService.list();
		return Result.success(beans);
	}
	
	@PostMapping("/ranks")
	public Object add(Ranks bean, HttpServletRequest request, MultipartFile image) throws Exception {
		int gid = bean.getGame().getId();
		int order;
		List<Ranks> rs = ranksService.listByGameAndOrderNot(gid, ranksService.hide);
		if(rs.isEmpty())
			order = 0;
		else
			order = rs.get(rs.size()-1).getRanksOrder();
		order++;
		bean.setRanksOrder(order);
		ranksService.add(bean);
		
		if(!image.isEmpty()) {
			File imageFolder = new File(new Filepath().path()+"img/ranksLogo");
			File file = new File(imageFolder, bean.getId()+".jpg");
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			image.transferTo(file);
			BufferedImage img = ImageUtil.change2jpg(file);	
			ImageIO.write(img, "jpg", file);
			ImageUtil.resizeImage(file, 150, 150, file);
		}
		return Result.success(bean);
	}
	
	@DeleteMapping("/ranks/{id}")
	public Object delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception {
		Ranks r = ranksService.get(id);
		
		//判断是否可以删除
		List<GameTable> gts = gameTableService.list();
		for(GameTable gt : gts) {
			if(gt.getRanks_a().equals(r)||gt.getRanks_b().equals(r))
				ranksService.delete(id);	
		}
		
		//正式删除前排序
		int gid = r.getGame().getId();
		List<Ranks> rs = ranksService.listByGameAndOrderNot(gid, ranksService.hide);
		Ranks rlast = rs.get(rs.size()-1); //获取top列表最后一位元素
		 //判读是否是最后一位元素
		if (r.getId()!=rlast.getId()&&r.getRanksOrder()!=0) {
			rs = rs.subList(r.getRanksOrder()-1, rs.size());	
			for(Ranks re:rs) {
				re.setRanksOrder(re.getRanksOrder()-1);
				ranksService.update(re);
			}
		}
		
		ranksService.delete(id);
		//删除对应图片
		File imageFolder = new File(new Filepath().path()+"img/ranksLogo");
		File file = new File(imageFolder, id+".jpg");
		file.delete();
		return Result.success("");
	}
	
	@GetMapping("/ranks/{id}")
	public Object get(@PathVariable("id") int id) throws Exception {
		Ranks bean = ranksService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping("/ranks")
	public Object update(@RequestBody Ranks bean, @RequestParam(name="oldGid") int oldGid) throws Exception {
		//判断是否更换分类
		if(bean.getGame().getId()!=oldGid) {
			int order = bean.getRanksOrder();
			
			//旧列表元素之后均往前移1位
			Ranks rod = ranksService.listByGameAndOrder(oldGid,order).get(0); //获取更新前的元素
			List<Ranks> rods = ranksService.listByGame(oldGid);	//获取旧列表
			Ranks rodlast = rods.get(rods.size()-1);	//获取旧列表最后一位元素	
			if (rod.getId()!= rodlast.getId()) //判读是否是最后一位元素
				rods = rods.subList(rod.getRanksOrder()-1, rods.size());	
			else
				rods = rods.subList(rods.size(), rods.size());
			
			for(Ranks ro:rods) {
				ro.setRanksOrder(ro.getRanksOrder()-1);
				ranksService.update(ro);
			}
			
			//获取新列表序号
			int gid = bean.getGame().getId();
			List<Ranks> rns = ranksService.listByGame(gid);
			if(rns.isEmpty())
				order = 0;
			else
				order = rns.get(rns.size()-1).getRanksOrder();
			order++;
			bean.setRanksOrder(order);
		}
		ranksService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping("/ranks/{id}/down")
	public Object down(@PathVariable int id) throws Exception {
		Ranks bean = ranksService.get(id);
		int gid = bean.getGame().getId();
		int order = bean.getRanksOrder();
		
		List<Ranks> rs = ranksService.listByGame(gid);
		Ranks rlast = rs.get(rs.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(bean.getId()!=rlast.getId()) {
			//元素后一位元素往前移动1位
			Ranks rnext = ranksService.listByGameAndOrder(gid, order+1).get(0);
			rnext.setRanksOrder(order);
			ranksService.update(rnext);
			//元素本身往后移动1位
			order++;
			bean.setRanksOrder(order);
			ranksService.update(bean);
		}
		return Result.success();
	}
	
	@GetMapping("/ranks/{id}/up")
	public Object up(@PathVariable int id) throws Exception {
		Ranks bean = ranksService.get(id);
		int gid = bean.getGame().getId();
		int order = bean.getRanksOrder();
		
		List<Ranks> rs = ranksService.listByGame(gid);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			Ranks rpre = ranksService.listByGameAndOrder(gid, order-1).get(0);
			rpre.setRanksOrder(order);
			ranksService.update(rpre);
			//元素本身往前移动1位
			order--;
			bean.setRanksOrder(order);
			ranksService.update(bean);
		}
		return Result.success();
	}
	
	@GetMapping("/ranks/{id}/hide")
	public Object hide(@PathVariable int id) throws Exception {
		Ranks bean = ranksService.get(id);
		int gid = bean.getGame().getId();
		int order = bean.getRanksOrder();
		
		List<Ranks> rs = ranksService.listByGameAndOrderNot(gid, ranksService.hide);
		Ranks rlast = rs.get(rs.size()-1); //获取top列表最后一位元素
		 //判读是否是最后一位元素
		if (bean.getId()!=rlast.getId()) {
			rs = rs.subList(bean.getRanksOrder()-1, rs.size());	
			for(Ranks re:rs) {
				re.setRanksOrder(re.getRanksOrder()-1);
				ranksService.update(re);
			}
		}
		bean.setRanksOrder(0);
		ranksService.update(bean);
		return Result.success();
	}
	
	@GetMapping("/ranks/{id}/show")
	public Object show(@PathVariable int id) throws Exception {
		Ranks bean = ranksService.get(id);
		int gid = bean.getGame().getId();
		
		List<Ranks> rs = ranksService.listByGameAndOrderNot(gid, ranksService.hide);
		for(Ranks re:rs) {
			re.setRanksOrder(re.getRanksOrder()+1);
			ranksService.update(re);
		}
		bean.setRanksOrder(1);
		ranksService.update(bean);
		return Result.success();
	}
	
	@PutMapping("/ranks/{id}/image")
	public Object imageUpdate(@PathVariable("id") int id, HttpServletRequest request, MultipartFile image) throws Exception{
		Ranks bean = ranksService.get(id);
		int gid = bean.getGame().getId();
		
		File imageFolder = new File(new Filepath().path()+"img/ranksLogo");
		File file = new File(imageFolder, bean.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		ImageUtil.resizeImage(file, 150, 150, file);
		return Result.success();
	}
}
