package com.basoft.eorder.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeanUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext; 
	
	public static Object getBean(String beanName) {
		if(applicationContext == null){
			throw new IllegalArgumentException("SpringBeanUtil applicationContext is null");
		}
        return applicationContext.getBean(beanName);
    }
	
	public static <T> T getBean(String beanName, Class<T> clazz) {
		if(applicationContext == null){
			throw new IllegalArgumentException("SpringBeanUtil applicationContext is null");
		}
        return clazz.cast(getBean(beanName));
    }

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

}
