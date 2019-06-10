package com.dongzhi.ow.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongzhi.ow.pojo.GameTable;
import com.dongzhi.ow.service.GameTableService;
import com.dongzhi.ow.util.Page4Navigator;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     GameTableController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:14:56
 */
@RestController
public class GameTableController {

	@Autowired
	GameTableService gameTableService;
	
	@GetMapping("/games/{gid}/gametables")
	public Object list(@PathVariable("gid") int gid, @RequestParam(value = "start", defaultValue = "0") int start,@RequestParam(value = "size", defaultValue = "5") int size) throws Exception{
		start = start<0?0:start;
		Page4Navigator<GameTable> page = gameTableService.listByGame(gid, start, size, 5);
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		//获取最后一次输入赛事表时间
		List<GameTable> gbsList = gameTableService.list();
		if(!gbsList.isEmpty()) {
			GameTable gb = gbsList.get(0);
			map.put("gbLast", gb);
		}
		return Result.success(map);
	}
	
	@PostMapping("/gametables")
	public Object add(@RequestBody GameTable bean) throws Exception {
		System.out.println(bean.getRanks_a().getId());
		System.out.println(bean.getRanks_b().getId());
		gameTableService.add(bean);
		return Result.success(bean);
	}
	
	@DeleteMapping("/gametables/{id}")
	public Object delete(@PathVariable("id") int id) throws Exception {
		gameTableService.delete(id);
		return Result.success("");
	}

	@GetMapping("/gametables/{id}")
	public Object get(@PathVariable("id") int id) throws Exception {
		GameTable bean = gameTableService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping("/gametables")
	public Object update(@RequestBody GameTable bean) throws Exception {
		gameTableService.update(bean);
		return Result.success(bean);
	}
	
//	@ResponseBody
//	@RequestMapping("admin_gametable_gamedate")
//	public Object gamedate(String strDate) {
//		String datePattern = "yyyy-MM-dd HH:mm";
//		if(DateUtil.isRightDateStr(strDate, datePattern)) {
//			return "true";
//		}
//		return "false";
//	}
}
