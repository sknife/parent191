package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.product.Sku;

public interface SkuService {

	public List<Sku> selectSkuListByProductId(Long productId);
	
	public void updateSkuById(Sku sku);
	
	public Sku selectSkuById(Long id);
	
	public void insertBuyerCartToRedis(BuyerCart buyerCart,String username);
	
	/**
	 * 从redis中取出所有购物车
	 * @param username
	 * @return
	 */
	public BuyerCart selectBuyerCartFromRedis(String username);
}
