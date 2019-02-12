package com.dongzhi.ow.util;

/**
 * @ClassName:     Filepath.java
 * @Description:   统一配置的图片文件存储路径，本地与云端更换需要修改路径
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:34:09
 */
public class Filepath {

	public String path() {

//		return "D:/resources/ows/";//本地图片存储路径resources文件夹定位
		return "/usr/local/resources/ows/";//云主机图片存储路径resources文件夹定位
	}
}
