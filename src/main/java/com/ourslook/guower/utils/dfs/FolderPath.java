package com.ourslook.guower.utils.dfs;

import com.ourslook.guower.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dazer
 */
public class FolderPath {
	/**
	 * 用户图像文件夹/upload/user
	 * @see Constant
	 * @see com.ourslook.guower.utils.XaUtils#getWebRootPath(HttpServletRequest)
	 * @see com.ourslook.guower.utils.XaUtils#getWebUploadVirtalRootPath(HttpServletRequest, String)
	 */
	public static String PATH_USER_DEFAULT = "user";

	public static String[] IMAGE_SUFFFIXS = new String[]{".png", ".jpg", ".jpeg", ".gif", ".TIFF", ".PCX", ".TGA", ".EXIF", ".SVG", ".PSD", ".CDR", ".PCD", ".DXF", ".UFO", ".EPS", ".CDR", ".SWF", ".EMF", ".RAW"};
	/**
	 * Banner广告图片文件夹Banner
	 */
	public static String Banner = "Banner";
	/**
	 * 新闻图片文件夹newsImage
	 */
	public static String newsImage = "newsImage";
	/**
	 * 二维码文件夹Qrcode
	 */
	public static String Qrcode = "Qrcode";
	/**
	 * 广告图片loadAD
	 */
	public static String loadAD = "loadAD";
	/**
	 * 展商LOGO图片
	 */
	public static String companyLogo = "companyLogo";
	/**
	 * 商品展示图片
	 */
	public static String productImg = "productImg";
	/**
	 * 运行的项目名/cioe/
	 */
	public static String rootFolder = Constant.SERVIER_NAME;

	/**
	 * 获取物理路径
	 * 
	 * @param request
	 * @param path
	 *            相对路径
	 * @return
	 */
	public static String getPhysicalPath(HttpServletRequest request, String path) {
	 String folder=	 request.getSession().getServletContext().getRealPath(path);
	 
	 File f=new File(folder);
			if(!f.exists()) {
				f.mkdirs();
			}
		return folder;
	}

	/**
	 * 创建日期文件夹
	 * @param request
	 * @return
	 */
	public static String CreateFilePath(HttpServletRequest request, String folderPath)
	{
    	String physicalFolder= FolderPath.getPhysicalPath(request,folderPath);

		String dateFolder=new SimpleDateFormat("yyyy_MM_dd").format(new Date());
		File file = new File(physicalFolder,dateFolder);
		if(!file.exists()&&!file.isDirectory())
		{
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}
}
