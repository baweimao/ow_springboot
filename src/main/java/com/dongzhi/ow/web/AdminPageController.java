package com.dongzhi.ow.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     AdminPageController.java
 * @Description:   后台页面跳转
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午11:04:52
 */
@Controller
public class AdminPageController {
	
	@GetMapping(value="/login")
	public String login() {
		return "admin/loginPage";
	}
	
	@GetMapping(value="/admin")
	public String admin() {
		return "redirect:admin_category_list";
	}
	
	@GetMapping(value="/logout")
	public Object Signout(HttpSession session) throws Exception{
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated())
			subject.logout();
		return "redirect:login";
	}
	
	@GetMapping(value="/admin_category_list")
	public String category() {
		return "admin/listCategory";
	}
	
	@GetMapping(value="/admin_category_edit")
	public String categoryEdit() {
		return "admin/editCategory";
	}
	
	@GetMapping(value="/admin_web_list")
	public String web() {
		return "admin/listWeb";
	}
	
	@GetMapping(value="/admin_web_edit")
	public String webEdit() {
		return "admin/editWeb";
	}
	
	@GetMapping(value="/admin_web_image")
	public String webImage() {
		return "admin/imageWeb";
	}
	
	@GetMapping(value="/admin_type_list")
	public String type() {
		return "admin/listType";
	}
	
	@GetMapping(value="/admin_type_edit")
	public String typeEdit() {
		return "admin/editType";
	}
	
	@GetMapping(value="/admin_people_list")
	public String people() {
		return "admin/listPeople";
	}
	
	@GetMapping(value="/admin_people_edit")
	public String peopleEdit() {
		return "admin/editPeople";
	}
	
	@GetMapping(value="/admin_people_image")
	public String peopleImage() {
		return "admin/imagePeople";
	}
	
	@GetMapping(value="/admin_social_list")
	public String social() {
		return "admin/listSocial";
	}
	
	@GetMapping(value="/admin_social_edit")
	public String socialEdit() {
		return "admin/editSocial";
	}
	
	@GetMapping(value="/admin_news_list")
	public String news() {
		return "admin/listNews";
	}
	
	@GetMapping(value="/admin_news_edit")
	public String newsEdit() {
		return "admin/editNews";
	}
	
	@GetMapping(value="/admin_news_image")
	public String image() {
		return "admin/imageNews";
	}
	
	@GetMapping(value="/admin_article_list")
	public String article() {
		return "admin/listArticle";
	}

	@GetMapping(value="/admin_article_edit")
	public String articleEdit() {
		return "admin/editArticle";
	}
	
	@GetMapping(value="/admin_game_list")
	public String game() {
		return "admin/listGame";
	}
	
	@GetMapping(value="/admin_game_edit")
	public String gameEdit() {
		return "admin/editGame";
	}
	
	@GetMapping(value="/admin_game_image")
	public String gameImage() {
		return "admin/imageGame";
	}
	
	@GetMapping(value="/admin_gametable_list")
	public String gameTable() {
		return "admin/listGameTable";
	}
	
	@GetMapping(value="/admin_gametable_edit")
	public String gameTableEdit() {
		return "admin/editGameTable";
	}
	
	@GetMapping(value="/admin_live_list")
	public String live() {
		return "admin/listLive";
	}
	
	@GetMapping(value="/admin_live_edit")
	public String liveEdit() {
		return "admin/editLive";
	}
	
	@GetMapping(value="/admin_ranks_list")
	public String ranks() {
		return "admin/listRanks";
	}
	
	@GetMapping(value="/admin_ranks_edit")
	public String ranksEdit() {
		return "admin/editRanks";
	}
	
	@GetMapping(value="/admin_ranks_image")
	public String ranksImage() {
		return "admin/imageRanks";
	}
	
	@GetMapping(value="/admin_page_list")
	public String page() {
		return "admin/listPage";
	}
	
	@GetMapping(value="/admin_page_edit")
	public String pageEdit() {
		return "admin/editPage";
	}
	
	@GetMapping(value="/admin_user_edit")
	public String userEdit() {
		return "admin/editUser";
	}
	
	@GetMapping(value="/admin_backgroundimage_edit")
	public String backgroundImageEdit() {
		return "admin/backgroundImage";
	}

	/**
	 *  @Description: 配置Ueditor的统一请求接口路径
	 * @param:        @param request
	 * @param:        @param response    
	 * @return:       void
	 */
	@RequestMapping("config")
	public void config(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Content-Type" , "text/html");
		 
        String rootPath = request.getSession().getServletContext().getRealPath("/");
 
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
}
