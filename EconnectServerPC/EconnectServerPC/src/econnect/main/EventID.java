package econnect.main;

public class EventID {
	
	public static final String EVENT_UP = "up";    //向上
	public static final String EVENT_LEFT = "left"; //向左
	public static final String EVENT_DOWN = "down"; //向下
	public static final String EVENT_RIGHT = "right"; //向右
	public static final String EVENT_BACK = "back"; //返回
	public static final String EVENT_SPACE = "space"; //空格键
	public static final String EVENT_ENTER = "enter"; //Enter键
	
	public static final String EVENT_SLEEP = "sleep"; //定时休眠 直接时间段后
	public static final String EVENT_TIME_SHUTDOWN = "shutdown_pc"; //定时关机
    

	public static final String CLIPBOARD = "clipCopy"; //粘贴板
	public static final String OPEN_WEB = "clipWeb";
	
	//对于剪切板的调用，需要添加一个判断，如果是网页，可以在电脑浏览器中打开
	//解决的一类问题: 对于在手机上看到的一些网页，为了更好的体验（界面的大小等方面）
}
