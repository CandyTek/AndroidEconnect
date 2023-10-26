package econnect.main;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.*;

import com.sun.glass.events.KeyEvent;

public class Main extends JFrame{
	
	private Box headIconBox = Box.createHorizontalBox();
	private Box ipBox = Box.createHorizontalBox();
	private Box portBox = Box.createHorizontalBox();
	private Box portBox2 = Box.createHorizontalBox();
	
	private Box controlBox = Box.createHorizontalBox();
	private Box bigBox = Box.createVerticalBox();
	
	private Icon icon = null;
	private JLabel topLogoBg = new JLabel();
	private JLabel ipTip = new JLabel("本机IP地址：  ");
	private JLabel portTip = new JLabel("设置端口号：  ");
	private JLabel portTip2 = new JLabel("TCP端口号：  ");
	private JTextField ipText = new JTextField(20);
	final JTextField portText  = new JTextField(20);
	final JTextField portText2  = new JTextField(20);
	private JButton startBtn = new JButton("开启");
	private JButton stopBtn = new JButton("停止");
	
	public static int portNum = 55555;
	public static int portNum2 = 33333;
	public static int states = 0;
	public ServerThread serverThread;
	public TcpThread tcpThread;
	
	private void init(){
		icon = new ImageIcon(this.getClass().getResource("/econnect/main/head.jpg"));
		topLogoBg.setSize(400, 50); 
		topLogoBg.setIcon(icon);
		headIconBox.add(topLogoBg);
		
		ipBox.add(Box.createHorizontalStrut(30));
		ipBox.add(ipTip);
		ipBox.add(ipText);
		ipBox.add(Box.createHorizontalStrut(30));
		
		portBox.add(Box.createHorizontalStrut(30));
		portBox.add(portTip);
		portBox.add(portText);
		portText.setText("55555");
		portBox.add(Box.createHorizontalStrut(30));
		
		
		portBox2.add(Box.createHorizontalStrut(30));
		portBox2.add(portTip2);
		portBox2.add(portText2);
		portText2.setText("33333");
		portBox2.add(Box.createHorizontalStrut(30));
		
		controlBox.add(Box.createHorizontalStrut(35));
		controlBox.add(startBtn);
		controlBox.add(Box.createHorizontalStrut(20));
		controlBox.add(stopBtn);
		
		
		bigBox.add(headIconBox);
		bigBox.add(Box.createVerticalStrut(20));
		bigBox.add(ipBox);
		bigBox.add(Box.createVerticalStrut(20));
		bigBox.add(portBox);
		bigBox.add(Box.createVerticalStrut(20));
		bigBox.add(portBox2);
		bigBox.add(Box.createVerticalStrut(20));
		bigBox.add(controlBox);
		bigBox.add(Box.createVerticalStrut(30));
		
		this.add(bigBox);
	}
	
	private Main(){
		super("Econnect PC 服务端");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		setSize(450, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation((width - 450) / 2, (height - 350) / 2);
		setResizable(false);
		init();
		try {
			ipText.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		ipText.setEditable(false);
		
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String portStr = portText.getText().trim();
				String portStr2 = portText2.getText().trim();
				if(portStr.equals("")){
					JOptionPane.showMessageDialog(null,"输入的信息不能为空");
            		return;
				}
				
				int num = 55555;
				int num2 = 33333;
				try{
					num = Integer.parseInt(portStr);
					num2 = Integer.parseInt(portStr2);
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null,"输入的信息必须为数字");
					return;
				}
				
				if ((num < 0 || num > 65535) || (num2 < 0 || num2 > 65535)) {
					JOptionPane.showMessageDialog(null,"端口号应该在0~65535之间");
            		return;
				}
				
				portNum = num;
				portNum2 = num2;
				
				startBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				startServer();
			}
		});
		
		stopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				stopServer();
				startBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				stopServer();
			}
		});
		
		this.add(bigBox);
		setVisible(true);
	}
	
	private void startServer(){
		if (states == 0){
			serverThread = new ServerThread();
			serverThread.start();
			tcpThread = new TcpThread();
			tcpThread.start();
			states = 1;
			portText.setEditable(false);
			portText2.setEditable(false);
			System.out.println("启动");
		}
		
		if (states == 2){ 
			serverThread.resume();
			tcpThread.resume();
			states = 1;
			System.out.println("恢复");
		}
	}
	
	private void stopServer(){
		if(states == 1){
			serverThread.suspend();
			tcpThread.suspend();
			states = 2;
			System.out.println("暂停");
		}
	}
	
	
	public static void main(String[] args){
		File file = new File(Constant.DEFAULT_PATH);
		file.mkdirs();
		new Main();
	}
	
	public class ServerThread extends Thread{
		
		public void run(){
			DatagramSocket socket = null;
			try {
				socket = new DatagramSocket(portNum);
				  
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"该端口号已被占用，请重新输入");
				startBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				states = 0;
		    	portText.setEnabled(true);
				return;
			}
			byte data[] = new byte[1024*1024];
		    DatagramPacket packet =	new DatagramPacket(data, data.length);
			System.out.println("开启端口监听"+ socket.getLocalPort());
			
			while(true){
				try {
					socket.receive(packet);
					String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
					System.out.println("收到了客户端发送的消息:" + result);
					
					try {
						operationCommand(result);
					} catch (AWTException e) {
						e.printStackTrace();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
	}
	
	//对客户端发来的指令进行相应的操作
	public void operationCommand(String command) throws AWTException{
		java.awt.Robot robot = new Robot();
		String[] info = command.split(";");
		//粘贴板的功能
		if (info.length == 2) {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(new StringSelection(info[1]), null);
			if (info[0].equals(EventID.CLIPBOARD)) {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_V);
				return;
				
			}else if(info[0].equals(EventID.OPEN_WEB)){
				String url = "rundll32 url.dll,FileProtocolHandler " + info[1];
				try {
					Runtime.getRuntime().exec(url);
				} catch (IOException e) {
					System.out.println("无法访问该网址");
				}
			}else if (info[0].equals(EventID.EVENT_TIME_SHUTDOWN)){
				String cmd = "shutdown -s -t " + info[1];
				try {
					System.out.println(cmd);
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					System.out.println(info[1]);
				}
			}
			
			return;
		}
		
		String eventType = command;
		if (eventType.equals(EventID.EVENT_UP)) {
			robot.keyPress(KeyEvent.VK_UP);
		}else if(eventType.equals(EventID.EVENT_DOWN)){
			robot.keyPress(KeyEvent.VK_DOWN);
		}else if(eventType.equals(EventID.EVENT_LEFT)){
			robot.keyPress(KeyEvent.VK_LEFT);
		}else if(eventType.equals(EventID.EVENT_RIGHT)){
			robot.keyPress(KeyEvent.VK_RIGHT);
		}else if(eventType.equals(EventID.EVENT_BACK)){
			robot.keyPress(KeyEvent.VK_BACKSPACE);
		}else if(eventType.equals(EventID.EVENT_ENTER)){
			robot.keyPress(KeyEvent.VK_ENTER);
		}else if(eventType.equals(EventID.EVENT_SPACE)){
			robot.keyPress(KeyEvent.VK_SPACE);
		}else if(eventType.equals(EventID.EVENT_SLEEP)){
			String cmd = "at " + getCurrentTime() + " rundll32 powrprof.dll,SetSuspendState";
			try {
				System.out.println(cmd);
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getCurrentTime() {
		  Date time = new Date(System.currentTimeMillis() + 360000);
		  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		  String hm = sdf.format(time);
		  return hm;
	}
	
	public class TcpThread extends Thread{
		public void run() {
			try {
				new Server();
			} catch (Exception e) {
				System.out.print("tcp连接失败, 映射文件失效");
			}
		}
	}
	
	
}
