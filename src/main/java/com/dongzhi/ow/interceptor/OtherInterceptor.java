package com.dongzhi.ow.interceptor;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @ClassName:     qitalanjiepeizhi
 * @Description:   其他拦截配置
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:25:51
 */
public class OtherInterceptor extends HandlerInterceptorAdapter {

     /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     *    执行下一个拦截器,直到所有的拦截器都执行完毕
     *    再执行被拦截的Controller
     *    然后进入拦截器链,
     *    从最后一个拦截器往回执行所有的postHandle()
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        return true;
 
    }
  
    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
 
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,   
            ModelAndView modelAndView) throws Exception {
 
//        HttpSession session = request.getSession();
//        String contextPath=session.getServletContext().getContextPath();
    	String contextPath="www.owobs.com";
        request.getSession().setAttribute("contextPath", contextPath);
        
    }
  
    /** 
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等  
     *  
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
     */
      
    public void afterCompletion(HttpServletRequest request,   
            HttpServletResponse response, Object handler, Exception ex) 
    throws Exception { 
            
//        System.out.println("afterCompletion(), 在访问视图之后被调用");
    } 
        
}