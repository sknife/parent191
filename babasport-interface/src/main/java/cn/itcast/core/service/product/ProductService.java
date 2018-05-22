package cn.itcast.core.service.product;
import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;

public interface ProductService {
	
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Long brandId,Boolean isShow);
	
	public List<Color> selectColorList();
	
	public void insertProduct(Product product);
	
	public void isShow(Long[] ids);
}