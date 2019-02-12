package com.dongzhi.ow.web;

import java.awt.image.BufferedImage;

import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dongzhi.ow.pojo.Foot;
import com.dongzhi.ow.service.FootService;
import com.dongzhi.ow.util.Filepath;
import com.dongzhi.ow.util.ImageUtil;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     PageController.java
 * @Description:   后台页面数据响应接口
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:15:57
 */
@RestController
public class PageController {

	@Autowired FootService footService;
	
	@GetMapping(value="/page")
	public Object list() throws Exception{
		List<Foot> beans = footService.list();
		return Result.success(beans);
	}
	
	@GetMapping(value="/page/{id}")
	public Object get(@PathVariable("id") int id) throws Exception{
		Foot bean = footService.get(id);
		return Result.success(bean);
	}
	
	@PutMapping(value="/page")
	public Object update(@RequestBody Foot bean) throws Exception{
		footService.update(bean);
		return Result.success(bean);
	}
	
	@PutMapping("/backgroundimage")
	public Object backgroundImageUpdate(HttpServletRequest request, @RequestBody MultipartFile image) throws Exception {
		File imageFolder = new File(new Filepath().path()+"img/web");
		File file = new File(imageFolder, "backgroundimage.jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);	
		ImageIO.write(img, "jpg", file);
		return Result.success();
	}

}
