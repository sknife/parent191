package cn.itcast.core.controller.product.upload;

import cn.itcast.core.common.web.Constants;
import cn.itcast.core.service.upload.UploadService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public void uploadPic(@RequestParam(required = false) MultipartFile pic, HttpServletResponse response) throws IOException {
        String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
        String url = Constants.IMG_URL + path;
        JSONObject jo = new JSONObject();
        jo.put("url",url);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jo.toString());

    }
}
