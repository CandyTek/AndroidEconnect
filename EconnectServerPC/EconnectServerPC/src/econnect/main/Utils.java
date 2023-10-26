package econnect.main;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class Utils {

	/**
	 * 获取电脑桌面的路径
	 * @return
	 */
	public static String getPCHomePath(){
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File com = fsv.getHomeDirectory();
		return com.getAbsolutePath();
	}
}
