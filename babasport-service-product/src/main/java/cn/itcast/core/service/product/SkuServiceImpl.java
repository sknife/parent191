package cn.itcast.core.service.product;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
//import cn.itcast.core.bean.BuyerCart;
//import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import redis.clients.jedis.Jedis;

//import redis.clients.jedis.Jedis;
/**
 * 库存管理
 * @author Administrator
 *
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService{
	
	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private ColorDao colorDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private Jedis jedis;
	
	/**
	 * 根据商品ID 查询 库存结果集
	 * @param productId
	 * @return
	 */
	public List<Sku> selectSkuListByProductId(Long productId) {
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(productId);
		List<Sku> list = skuDao.selectByExample(skuQuery);
		for (Sku sku : list) {
			Color color = colorDao.selectByPrimaryKey(sku.getColorId());
			sku.setColor(color);
		}
		return list;
		
		
	}
	
	
	/**
	 * 修改库存
	 * @param id
	 */
	public void updateSkuById(Sku sku) {
		skuDao.updateByPrimaryKeySelective(sku);
	}
	
	
	/**
	 * 通过skuId查询sku对象
	 * @param id
	 * @return
	 */
	public Sku selectSkuById(Long id) {
		//Sku对象
		Sku sku = skuDao.selectByPrimaryKey(id);
		sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		
		return sku;
	}
	
	/**
	 * 保存商品到redis
	 * @param buyerCart
	 */
	public void insertBuyerCartToRedis(BuyerCart buyerCart,String username) {
		//判断购物项的长度大于0
		List<BuyerItem> items = buyerCart.getItems();
		if(items.size() > 0) {
			for (BuyerItem buyerItem : items) {
				//判断是否已经存在
				if(jedis.hexists("buyerCart:"+username, String.valueOf(buyerItem.getSku().getId()))) {
					//加数量
					jedis.hincrBy("buyerCart:"+username, String.valueOf(buyerItem.getSku().getId()), buyerItem.getAmount());
				}else{
					
					jedis.hset("buyerCart:"+username, String.valueOf(buyerItem.getSku().getId()), String.valueOf(buyerItem.getAmount()));
				}
			}
		}
	}
	
	/**
	 * 从redis中取出所有购物车
	 * @param username
	 * @return
	 */
	public BuyerCart selectBuyerCartFromRedis(String username) {
		
		BuyerCart buyerCart = new BuyerCart();
		Map<String, String> hgetAll = jedis.hgetAll("buyerCart:"+username);
		if(null != hgetAll) {
			Set<Entry<String,String>> entrySet = hgetAll.entrySet();
			for (Entry<String, String> entry : entrySet) {
				Sku sku = new Sku();
				sku.setId(Long.parseLong(entry.getKey()));
				BuyerItem buyerItem = new BuyerItem();
				buyerItem.setSku(sku);
				
				buyerItem.setAmount(Integer.parseInt(entry.getValue()));
				buyerCart.addItem(buyerItem);
			}
		}
		
		return buyerCart;
	}
	
	
}
