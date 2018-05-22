package cn.itcast.core.service.user;

import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.user.Buyer;

public interface BuyerService {

	public Buyer selectBuyerByUsername(String username);
	
	public void insertOrder(Order order,String username);
}
