package cn.itcast.core.controller.product;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.SkuService;

@Controller
public class SkuController {
	
	@Autowired
	private SkuService skuService;
	
	@RequestMapping(value = "/sku/list.do")
	public String list(Long productId,Model model) {
		List<Sku> list = skuService.selectSkuListByProductId(productId);
		model.addAttribute("skus", list);
		return "sku/list";
	}

	@RequestMapping(value = "/sku/addSku.do")
	@ResponseBody
	public String updateSku(Sku sku){
		JSONObject jo = null;
		try {
			skuService.updateSkuById(sku);
			
			jo = new JSONObject();
			jo.put("message", "修改成功");
	
		} catch (Exception e) {
			jo = new JSONObject();
			jo.put("message", "修改失败");
			return jo.toString();
		}
		return jo.toString();
		
	}
	
	
}
