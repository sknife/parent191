package cn.itcast.core.service.upload;

/**
 * Created by sknife on 2018/1/9.
 */
public interface UploadService {
    String uploadPic(byte[] pic, String name, long size);
}
