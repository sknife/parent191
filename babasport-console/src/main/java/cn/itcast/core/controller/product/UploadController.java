package cn.itcast.core.controller.product;

import cn.itcast.common.web.Constants;
import cn.itcast.core.service.upload.UploadService;
import net.fckeditor.response.UploadResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sknife on 2018/1/9.
 */
@Controller
@RequestMapping(value = "/upload")
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@RequestMapping(value = "/uploadPic.do")
	public void uploadPic(@RequestParam(required = false) MultipartFile pic, HttpServletResponse response)
			throws IOException {
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		String url = Constants.IMG_URL + path;
		JSONObject jo = new JSONObject();
		jo.put("url", url);

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
		// System.out.println(pic.getOriginalFilename());

	}

	@RequestMapping(value = "/uploadPics.do")
	@ResponseBody
	public List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics, HttpServletResponse response)
			throws IOException {
		List<String> urls = new ArrayList<String>();
		for (MultipartFile pic : pics) {
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			String url = Constants.IMG_URL + path;
			urls.add(url);
		}
		return urls;

	}

	// 上传Fck
	@RequestMapping(value = "/uploadFck.do")
	public void uploadFck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//无敌版
		List<String> urls = new ArrayList<String>();
		MultipartRequest mr = (MultipartRequest) request;
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String,MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			String url = Constants.IMG_URL + path;
			JSONObject jo = new JSONObject();
			jo.put("error", 0);
			jo.put("url", url);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(jo.toString());
		}

	}

}
