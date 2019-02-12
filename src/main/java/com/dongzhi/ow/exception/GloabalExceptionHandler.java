package com.dongzhi.ow.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.dongzhi.ow.util.Result;

/**
 * @ClassName:     GloabalExceptionHandler.java
 * @Description:   错误全局统一拦截处理类
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午9:18:12
 */
@RestController
@ControllerAdvice
public class GloabalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    	e.printStackTrace();
    	Class constraintViolationException = Class.forName("org.hibernate.exception.ConstraintViolationException");
    	if(null!=e.getCause()  && constraintViolationException==e.getCause().getClass()) {
    		return Result.fail("违反了约束，多半是外键约束");
    	}
        return Result.fail(e.getMessage());
    }

}

