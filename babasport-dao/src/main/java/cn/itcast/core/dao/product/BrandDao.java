package cn.itcast.core.dao.product;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;

import java.util.List;

/**
 * Created by sknife on 2017/1/6.
 */
public interface BrandDao {
    List<Brand> selectBrandListByQuery(BrandQuery brandQuery);
    Integer selectCount(BrandQuery brandQuery);
    Brand selectBrandById(Long id);
    void updateBrandById(Brand brand);
	void doDelete(Long id);
	void deleteBrandByIds(Long[] ids);
}
