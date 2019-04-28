package com.online.mall.shoppv.common.util;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 便于后续通过中间件缓存做session共享
 *
 */
@Component
public class SessionUtil {

	public static final String USER = "user";
	
	@Resource
	private ApplicationContext context;
	
	private CacheManager cacheManager;
	
	public static void setAttribute(HttpSession session,String key,Object value)
	{
		session.setAttribute(key,value);
	}
	
	
	public static Object getAttribute(HttpSession session,String key)
	{
		return session.getAttribute(key);
	}
	
	private CacheManager getCacheBean() {
		if(cacheManager == null) {
			cacheManager = (CacheManager)context.getBean("caffeineCacheManager");
		}
		return cacheManager;
	}
	
	public Object getCacheContent(String cacheName,String key) {
		SimpleValueWrapper wrap = (SimpleValueWrapper)getCacheBean().getCache(cacheName).get(key);
		if(wrap != null) {
			return wrap.get();
		}else {
			return null;
		}
		
	}
	
	public synchronized Object putIfAbsent(String cacheName,String key,Object value) {
		SimpleValueWrapper wrap = (SimpleValueWrapper)getCacheBean().getCache(cacheName).putIfAbsent(key, value);
		if(wrap != null) {
			return wrap.get();
		}else {
			return null;
		}
	}
	
	public void remove(String cacheName,String key) {
		getCacheBean().getCache(cacheName).evict(key);
	}
	
	public void setCacheContent(String cacheName,String key,Object value) {
		getCacheBean().getCache(cacheName).put(key, value);
	}
	
}
