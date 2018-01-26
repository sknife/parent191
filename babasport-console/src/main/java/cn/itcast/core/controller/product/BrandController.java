package cn.itcast.core.controller.product;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.common.page.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/brand")
public class BrandController {
	@Autowired
	private BrandService brandService;

	@RequestMapping(value = "/list.do")
	public String list(String name, Integer isDisplay, Integer pageNo, Model model) {
		if (null == isDisplay) {
			isDisplay = 1;
		}
		Pagination pagination = brandService.selectPaginationByQuery(name, isDisplay, pageNo);
		model.addAttribute("name", name);
		model.addAttribute("pagination", pagination);
		if (null != isDisplay) {
			model.addAttribute("isDisplay", isDisplay);
		} else {
			model.addAttribute("isDisplay", 1);
		}

		return "brand/list";
	}

	@RequestMapping(value = "/toAdd.do")
	public String toAdd() {

		return "brand/add";
	}

	@RequestMapping(value = "/toEdit.do")
	public String toEdit(Long id, Model model) {

		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);

		return "brand/edit";
	}
}
