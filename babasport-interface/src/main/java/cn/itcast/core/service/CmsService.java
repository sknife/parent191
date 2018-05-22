package cn.itcast.core.service;
import java.util.List;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;

public interface CmsService {
	
	public Product selectProductById(Long productId);
	
	public List<Sku> selectSkuListByProductId(Long productId);
}
