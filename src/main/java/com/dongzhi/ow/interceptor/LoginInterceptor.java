package com.dongzhi.ow.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @ClassName:     LoginInterceptor.java
 * @Description:   登陆拦截配置类
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:20:08
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();
        String contextPath=session.getServletContext().getContextPath();
        
        //用于校验的字符串数组
        String[] requireAuthPages = new String[]{
                "admin",
                "categories",
                "webs",
                "types",
                "peoples",
                "socials",
                "news",
                "articles",
                "games",
                "ranks",
                "lives",
                "gametables"
                };
 
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        String page = StringUtils.substringAfter(uri,"/" );
        if(begindWith(page, requireAuthPages)){
        	Subject subject = SecurityUtils.getSubject();
        	if(!subject.isAuthenticated()) {
        		response.sendRedirect("/login");
                return false;
            }
        }
        return true;
    }
	
	private boolean begindWith(String page, String[] noAuthPages) {
		boolean result = false;
		for(String noAuthPage : noAuthPages) {
//			System.out.println(page+noAuthPage+StringUtils.startsWith(page, noAuthPage));
			if(StringUtils.startsWith(page, noAuthPage)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
 
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,   
            ModelAndView modelAndView) throws Exception {

    }
  
    /** 
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等  
     *  
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
     */
      
    public void afterCompletion(HttpServletRequest request,   
            HttpServletResponse response, Object handler, Exception ex) 
    throws Exception { 
            
    } 
}
