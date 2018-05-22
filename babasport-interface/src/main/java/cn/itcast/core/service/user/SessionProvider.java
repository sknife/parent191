package cn.itcast.core.service.user;
public interface SessionProvider {
	
	public void setAttribuerForUsername(String name,String value);
	
	//取用户名从redis中
	public String getAttributeForUsername(String name);
	
	//退出登录
	
}
