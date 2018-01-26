package cn.itcast.core.service.upload;

import org.springframework.stereotype.Service;

import cn.itcast.common.fdfs.FastDFSUtils;

/**
 * Created by sknife on 2018/1/9.
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {
    @Override
    public String uploadPic(byte[] pic, String name, long size) {
    	
        try {
			return FastDFSUtils.uploadPic(pic,name,size);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
}
