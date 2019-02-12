package com.dongzhi.ow.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName:     SpringContextUtil.java
 * @Description:   通过重新加载Bean解决Service中调用其他方法，无法触发Redis的aop切面问题
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:49:52
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
     
    private SpringContextUtil() {
         
    }
     
    private static ApplicationContext applicationContext;
     
    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        SpringContextUtil.applicationContext = applicationContext;
    }
     
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
 
}