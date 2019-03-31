package com.online.mall.shoppv.common.util;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 便于后续通过中间件缓存做session共享
 * @author kaka_lijian
 *
 */
@Component
public class SessionUtil {

	public static final String USER = "user";
	
	@Resource
	private ApplicationContext context;
	
	public static void setAttribute(HttpSession session,String key,Object value)
	{
		session.setAttribute(key,value);
	}
	
	
	public static Object getAttribute(HttpSession session,String key)
	{
		return session.getAttribute(key);
	}
	
	
	public Object getCacheContent(String cacheName,String key) {
		SimpleValueWrapper wrap = (SimpleValueWrapper)((CacheManager)context.getBean("caffeineCacheManager")).getCache(cacheName).get(key);
		if(wrap != null) {
			return wrap.get();
		}else {
			return null;
		}
		
	}
	
	public void setCacheContent(String cacheName,String key,Object value) {
		((CacheManager)context.getBean("caffeineCacheManager")).getCache(cacheName).put(key, value);
	}
	
}
