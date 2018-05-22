package cn.itcast.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.CmsService;
import cn.itcast.core.service.SearchService;
import cn.itcast.core.service.product.BrandService;

import cn.itcast.common.page.Pagination;

/**
 * 前台商品
 * @author Administrator
 *
 */
@Controller
public class ProductController {
	
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private BrandService brandService;
	
	/*@Autowired
	private CmsService cmsService;*/
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/search")
	public String search(@RequestParam(defaultValue="1")Integer pageNo,String keyword,Long brandId,String price,Model model) throws Exception {
//	public String search(Integer pageNo, String keyword, Model model) throws Exception {
		//查询品牌redis
		List<Brand> brands = brandService.selectBrandListFromRedis();
		model.addAttribute("brands", brands);
		
		//查询品牌结构集
//		Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword);
//		model.addAttribute("pagination", pagination);
		
		Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword,brandId,price);
		model.addAttribute("pagination", pagination);
		
		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		
		//已选条件
		Map<String,String> map = new HashMap<String,String>();
		//品牌
		if(null!=brandId) {
			for (Brand brand : brands) {
				if(brandId == brand.getId()) {
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		
		//价格
		if(null != price) {
			if(price.contains("-")) {
				map.put("价格", price);
			}else {
				map.put("价格", price+"以上");
			}
			
		}
		
		model.addAttribute("map", map);
		model.addAttribute("keyword", keyword);
		return "search";
		
	}
	
	/**
	 * 商品详细页面
	 * @param id
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value="/product/detail")
	public String detail(Long id,Model model) {
		Product product = cmsService.selectProductById(id);
		
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		
		//遍历一次相同的不要 set集合可以去重
		Set<Color> colors = new HashSet<Color>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		
		model.addAttribute("product", product);
		model.addAttribute("skus", skus);
		model.addAttribute("colors", colors);
		
		return "product";
	}*/
	
}
