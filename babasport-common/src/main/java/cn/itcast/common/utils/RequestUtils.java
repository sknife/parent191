package cn.itcast.common.utils;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取 CSESSIONID
 * @author Administrator
 *
 */
public class RequestUtils {
	
	//
	public static String getCSESSIONID(HttpServletRequest request,HttpServletResponse response) {
		//1 取出Cookie
		Cookie[] cookies = request.getCookies();
		if(null != cookies && cookies.length >0) {
			for (Cookie cookie : cookies) {
				if("CSESSIONID".equals(cookie.getName())) {
					return cookie.getValue();
				}
				
			}
		}
		
		String csessionid = UUID.randomUUID().toString().replace("-", "");
		
		Cookie cookie = new Cookie("CSESSIONID",csessionid);
		//设置存活时间
		cookie.setMaxAge(-1);
		//设置路径
		cookie.setPath("/");
		
		//设置跨域
		//cookie.setDomain(".jd.com");
		
		
		
		
		
		//2判断Cookie中是否有CSESSIONID
		
		response.addCookie(cookie);
		
		return csessionid;
	}
}
