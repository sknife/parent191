package cn.itcast.core.service.product;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.dao.product.BrandDao;
import redis.clients.jedis.Jedis;
import cn.itcast.common.page.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


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
	
	@Autowired
	private Jedis jedis;

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
			 params.append("&isDisplay=").append(isDisplay);
		} else {
			brandQuery.setIsDisplay(1);
			 params.append("&isDisplay=").append(1);
		}

		Pagination pagination = new Pagination(brandQuery.getPageNo(), brandQuery.getPageSize(),
				brandDao.selectCount(brandQuery));

		pagination.setList(brandDao.selectBrandListByQuery(brandQuery));

		String url = "/brand/list.do";

		pagination.pageView(url, params.toString());

		return pagination;
	}

	@Override
	public Brand selectBrandById(Long id) {
		return brandDao.selectBrandById(id);
	}

	@Override
	public void updateBrandById(Brand brand) {
		// 修改redis

		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());

		brandDao.updateBrandById(brand);
	}

	public List<Brand> selectBrandListFromRedis() {
		List<Brand> brands = new ArrayList<Brand>();
		Map<String, String> hgetAll = jedis.hgetAll("brand");
		Set<Entry<String,String>> entrySet = hgetAll.entrySet();
		for (Entry<String, String> entry : entrySet) {
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());
			brands.add(brand);
		}
		
		return brands;
	}


	@Override
	public void deletes(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			brandDao.doDelete(ids[i]);
		}

	}

	@Override
	public void deleteBrandByIds(Long[] ids) {
		// TODO Auto-generated method stub
		brandDao.deleteBrandByIds(ids);
	}

	@Override
	public List<Brand> selectBrandListByQuery(Integer isDisplay) {
		BrandQuery brandQuery = new BrandQuery();
		brandQuery.setIsDisplay(isDisplay);
		List<Brand> list = brandDao.selectBrandListByQuery(brandQuery);
		return list;
	}
}
