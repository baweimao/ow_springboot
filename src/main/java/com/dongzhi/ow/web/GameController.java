package com.dongzhi.ow.web;

import java.awt.image.BufferedImage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dongzhi.ow.pojo.Game;
import com.dongzhi.ow.pojo.GameTable;
import com.dongzhi.ow.pojo.Live;
import com.dongzhi.ow.pojo.Ranks;
import com.dongzhi.ow.service.GameService;
import com.dongzhi.ow.service.GameTableService;
import com.dongzhi.ow.service.LiveService;
import com.dongzhi.ow.service.RanksService;
import com.dongzhi.ow.util.Filepath;
import com.dongzhi.ow.util.ImageUtil;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     GameController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:14:21
 */
@RestController
public class GameController {

	@Autowired GameService gameService;
	@Autowired GameTableService gameTableService;
	@Autowired RanksService ranksService;
	@Autowired LiveService liveService;
	
	@GetMapping("/games")
	public Object list() {
		List<Game> gsTop = gameService.listByUp(gameService.top);
		List<Game> gs = gameService.listByUp(gameService.art);
		Map<String, Object> map = new HashMap<>();
		map.put("gsTop", gsTop);
		map.put("gs", gs);
		return Result.success(map);
	}
	
	@GetMapping("/games/list")
	public Object listAll() {
		List<Game> gs = gameService.list();
		return Result.success(gs);
	}
	
	@PostMapping("/games")
	public Object add(Game bean, HttpServletRequest request, MultipartFile image) throws Exception {
		int order;

		List<Game> gs = gameService.listByUpAndOrderNot(bean.getUp(), gameService.hide);
		if(gs.isEmpty())
			order = 0;
		else
			order = gs.get(gs.size()-1).getGameOrder();
		order++;
		bean.setGameOrder(order);
		gameService.add(bean);
		
		File imageFolder = new File(new Filepath().path()+"img/gameLogo");
		File file = new File(imageFolder, bean.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		ImageUtil.resizeImage(file, 400, 340, file);
		return Result.success();
	}
	
	@DeleteMapping("/games/{id}")
	public Object delete(@PathVariable("id") int id, HttpServletRequest request, MultipartFile image) throws Exception{
		Game g = gameService.get(id);
		int order = g.getGameOrder();
		int up = g.getUp();
		
		//判断是否可以删除，正式删除前排序
		List<Ranks> rs = ranksService.listByGame(g.getId());
		List<GameTable> gts = gameTableService.listByGame(g.getId());
		List<Live> ls = liveService.listByGame(g.getId());
		
		if(rs.isEmpty()&&gts.isEmpty()&&ls.isEmpty()) {
			List<Game> gs = gameService.listByUpAndOrderNot(up, gameService.hide);
			Game glast = gs.get(gs.size()-1); //获取top列表最后一位元素
			 //判读是否是最后一位元素
			if (g.getId()!=glast.getId()&&g.getGameOrder()!=0) {
				gs = gs.subList(g.getGameOrder()-1, gs.size());	
				for(Game ge:gs) {
					ge.setGameOrder(ge.getGameOrder()-1);
					gameService.update(ge);
				}
			}
		}
		
		gameService.delete(id);
		//删除对应图片
		File imageFolder = new File(new Filepath().path()+"img/gameLogo");
		File file = new File(imageFolder, id+".jpg");
		file.delete();
		return Result.success("");
	}
	
	@GetMapping("/games/{id}")
	public Object get(@PathVariable("id") int id) throws Exception{
		Game bean = gameService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping("/games")
	public Object update(@RequestBody Game bean) throws Exception{
		gameService.update(bean);
		return Result.success(bean);
	}
	
	@GetMapping("/games/{id}/down")
	public Object down(@PathVariable("id") int id) throws Exception{
		Game g = gameService.get(id);
		int order = g.getGameOrder();
		int up = g.getUp();
		
		List<Game> gs = gameService.listByUpAndOrderNot(up, gameService.hide);
		Game glast = gs.get(gs.size()-1); //获取最后一位元素
		//判读是否是最后一位元素
		if(g.getId()!=glast.getId()) {
			//元素后一位元素往前移动1位
			Game gnext = gameService.listByUpAndOrder(up, order+1).get(0);
			gnext.setGameOrder(order);
			gameService.update(gnext);
			//元素本身往后移动1位
			order++;
			g.setGameOrder(order);
			gameService.update(g);
		}
		return Result.success();
	}
	
	@GetMapping("/games/{id}/up")
	public Object up(@PathVariable("id") int id) throws Exception{
		Game g = gameService.get(id);
		int order = g.getGameOrder();
		int up = g.getUp();
		
		List<Game> gs = gameService.listByUpAndOrderNot(up, gameService.hide);
		//判读是否是第一位元素
		if(order > 1) {
			//元素前一位元素往后移动1位
			Game gpre = gameService.listByUpAndOrder(up, order-1).get(0);
			gpre.setGameOrder(order);
			gameService.update(gpre);
			//元素本身往前移动1位
			order--;
			g.setGameOrder(order);
			gameService.update(g);
		}
		return Result.success();
	}
	
	@GetMapping("/games/{id}/hide")
	public Object hide(@PathVariable("id") int id) throws Exception{
		Game g = gameService.get(id);
		int order = g.getGameOrder();
		int up = g.getUp();
		
		List<Game> gs = gameService.listByUpAndOrderNot(up, gameService.hide);
		Game glast = gs.get(gs.size()-1); //获取top列表最后一位元素
		 //判读是否是最后一位元素
		if (g.getId()!=glast.getId()) {
			gs = gs.subList(g.getGameOrder()-1, gs.size());	
			for(Game ge:gs) {
				ge.setGameOrder(ge.getGameOrder()-1);
				gameService.update(ge);
			}
		}	
		g.setGameOrder(0);
		gameService.update(g);
		return Result.success();
	}
	
	@GetMapping("/games/{id}/show")
	public Object show(@PathVariable("id") int id) throws Exception{
		Game g = gameService.get(id);
		int up = g.getUp();

		List<Game> gs = gameService.listByUpAndOrderNot(up, gameService.hide);
		for(Game ge:gs) {
			ge.setGameOrder(ge.getGameOrder()+1);
			gameService.update(ge);
		}
		g.setGameOrder(1);
		gameService.update(g);
		return Result.success();
	}
	
	@PutMapping("/games/{id}/image")
	public Object imageUpdate(@PathVariable("id") int id, HttpServletRequest request, MultipartFile image) throws Exception {
		Game g = gameService.get(id);
		
		File imageFolder = new File(new Filepath().path()+"img/gameLogo");
		File file = new File(imageFolder, g.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		ImageUtil.resizeImage(file, 400, 340, file);
		return Result.success();
	}
}
