package cn.itcast.core.controller.product;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.ProductService;

import cn.itcast.common.page.Pagination;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@RequestMapping(value="/product/list.do")
	public String list(Integer pageNo,String name,Long brandId,Boolean isShow,Model model) {
		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
		
		List<Brand> brands = brandService.selectBrandListByQuery(1);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		model.addAttribute("brands", brands);
		
		if(null != isShow) {
			model.addAttribute("isShow", isShow);
		}else {
			model.addAttribute("isShow", false);
		}
		
		
		return "product/list";
	}
	
	
	//准备商品添加
	@RequestMapping(value="/product/toAdd.do")
	public String toAdd(Model model) {
		List<Color> colors = productService.selectColorList();
		List<Brand> brands = brandService.selectBrandListByQuery(1);
		model.addAttribute("colors", colors);
		model.addAttribute("brands", brands);
		return "product/add";
		
	}
	
	@RequestMapping(value="/product/add.do")
	public String add(Product product) {
		productService.insertProduct(product);
		return "redirect:/product/list.do";
	}
	
	/**
	 * 上架
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/product/isShow.do")
	public String isShow(Long[] ids) {
		productService.isShow(ids);
		return "forward:/product/list.do";
	}
	
	
}