package cn.itcast.core.service.product;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.dao.product.BrandDao;
import cn.itcast.common.page.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sknife on 2018/1/6.
 */
@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;

    @Override
    public Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo) {
        BrandQuery brandQuery = new BrandQuery();

        brandQuery.setPageNo(Pagination.cpn(pageNo));

        brandQuery.setPageSize(3);

        StringBuilder params = new StringBuilder();

        if (null != name) {
            brandQuery.setName(name);
            params.append("name=").append(name);
        }
        if (null != isDisplay) {
            brandQuery.setIsDisplay(isDisplay);
//            params.append("&isDisplay=").append(isDisplay);
        } else {
            brandQuery.setIsDisplay(1);
//            params.append("&isDisplay=").append(1);
        }

        Pagination pagination = new Pagination(
            brandQuery.getPageNo(),
            brandQuery.getPageSize(),
            brandDao.selectCount(brandQuery)
        );

        pagination.setList(brandDao.selectBrandListByQuery(brandQuery));

        String url = "/brand/list.do";

        pagination.pageView(url, params.toString());

        return pagination;
    }

    @Override
    public Brand selectBrandById(Long id) {
        return brandDao.selectBrandById(id);
    }
}
