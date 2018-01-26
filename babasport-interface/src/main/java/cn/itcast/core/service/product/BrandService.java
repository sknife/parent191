package cn.itcast.core.service.product;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.common.page.Pagination;

/**
 * Created by sknife on 2018/1/6.
 */
public interface BrandService {
    Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo);
    Brand selectBrandById(Long id);
}
