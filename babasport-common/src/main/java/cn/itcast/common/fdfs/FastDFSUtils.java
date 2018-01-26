package cn.itcast.common.fdfs;

//import cn.itcast.core.common.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

/**
 * 连接分布式文件系统的Java接口
 * @author lx
 *
 */
public class FastDFSUtils {

	
	//上传图片到分布式文件系统
	public static String uploadPic(byte[] pic,String name,Long size) throws Exception{
		//设置tracker的ip  读取fdfs_client.conf
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		//创建客户端
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		
		StorageServer storageServer = null;
		
		StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
		
		// jpg png 
		String ext = FilenameUtils.getExtension(name);
		
		NameValuePair[] meta_list = new NameValuePair[3];
		meta_list[0] = new NameValuePair("filename",name);
		meta_list[1] = new NameValuePair("fileext",ext);
		meta_list[2] = new NameValuePair("filesize",String.valueOf(size));
		//路径
		String path = storageClient1.upload_file1(pic, ext, meta_list);
		
		
		return path;
	}
}
