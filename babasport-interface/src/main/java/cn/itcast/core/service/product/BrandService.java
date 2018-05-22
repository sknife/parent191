package cn.itcast.core.service.product;

import cn.itcast.core.bean.product.Brand;

import java.util.List;

import cn.itcast.common.page.Pagination;

/**
 * Created by sknife on 2018/1/6.
 */
public interface BrandService {
	public List<Brand> selectBrandListByQuery(Integer isDisplay);

    Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo);
    
    Brand selectBrandById(Long id);
    
	public void updateBrandById(Brand brand);
	
	public void deleteBrandByIds(Long[] ids);
	
	public void deletes(Long[] ids);
	
	public List<Brand> selectBrandListFromRedis();

}
