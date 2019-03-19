package com.online.mall.shoppv.common.util;

import javax.servlet.http.HttpSession;

/**
 * 便于后续通过中间件缓存做session共享
 * @author kaka_lijian
 *
 */
public class SessionUtil {

	public static final String USER = "user";
	
	public static void setAttribute(HttpSession session,String key,Object value)
	{
		session.setAttribute(key,value);
	}
	
	
	public static Object getAttribute(HttpSession session,String key)
	{
		return session.getAttribute(key);
	}
	
}
