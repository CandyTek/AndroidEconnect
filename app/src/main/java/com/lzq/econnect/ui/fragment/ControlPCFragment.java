package com.lzq.econnect.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lzq.econnect.R;
import com.lzq.econnect.base.BaseFragment;
import com.lzq.econnect.common.Constant;
import com.lzq.econnect.common.EventID;
import com.lzq.econnect.utils.ClipboardUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * function: 控制PC端的一些操作
 * Created by lzq on 2017/3/19.
 */

public class ControlPCFragment extends BaseFragment implements View.OnClickListener {
	private Button upBtn;
	private Button leftBtn;
	private Button downBtn;
	private Button rightBtn;
	private Button backBtn;
	private Button spaceBtn;
	private Button enterBtn;
	private Button sleepBtn;
	private Button timeShutBtn;
	private Button pastClipBtn;
	private Button openWebBtn;

	// @Bind(R.id.upBtn)
	// Button upBtn;
	// @Bind(R.id.leftBtn)
	// Button leftBtn;
	// @Bind(R.id.downBtn)
	// Button downBtn;
	// @Bind(R.id.rightBtn)
	// Button rightBtn;
	// @Bind(R.id.backBtn)
	// Button backBtn;
	// @Bind(R.id.spaceBtn)
	// Button spaceBtn;
	// @Bind(R.id.enterBtn)
	// Button enterBtn;
	// @Bind(R.id.sleepBtn)
	// Button sleepBtn;
	// @Bind(R.id.timeShutBtn)
	// Button timeShutBtn;
	// @Bind(R.id.openWebBtn)
	// Button openWebBtn;
	// @Bind(R.id.pastClipBtn)
	// Button pastClipBtn;

	@Override
	public void onViewCreated(View view,Bundle savedInstanceState) {
		super.onViewCreated(view,savedInstanceState);
        
	}
	private DatagramSocket socket = null;

	@Override
	protected int setContentViewResId() {
		return R.layout.fragment_control_pc;
	}

	@Override
	protected void doBusiness(View view) {
		if(view!=null){
			initView(view);
		}
		initViews();
	}

	private void initViews() {
		upBtn.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
		downBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		spaceBtn.setOnClickListener(this);
		enterBtn.setOnClickListener(this);
		sleepBtn.setOnClickListener(this);
		timeShutBtn.setOnClickListener(this);
		openWebBtn.setOnClickListener(this);
		pastClipBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.upBtn) {//sendMessage2Server(EventID.EVENT_UP);
            new SendMsgThread(EventID.EVENT_UP).start();
        } else if (id == R.id.leftBtn) {//sendMessage2Server(EventID.EVENT_LEFT);
            new SendMsgThread(EventID.EVENT_LEFT).start();
        } else if (id == R.id.downBtn) {//sendMessage2Server(EventID.EVENT_DOWN);
            new SendMsgThread(EventID.EVENT_DOWN).start();
        } else if (id == R.id.rightBtn) {//sendMessage2Server(EventID.EVENT_RIGHT);
            new SendMsgThread(EventID.EVENT_RIGHT).start();
        } else if (id == R.id.backBtn) {//sendMessage2Server(EventID.EVENT_BACK);
            new SendMsgThread(EventID.EVENT_BACK).start();
        } else if (id == R.id.spaceBtn) {//sendMessage2Server(EventID.EVENT_SPACE);
            new SendMsgThread(EventID.EVENT_SPACE).start();
        } else if (id == R.id.enterBtn) {//sendMessage2Server(EventID.EVENT_ENTER);
            new SendMsgThread(EventID.EVENT_ENTER).start();
        } else if (id == R.id.sleepBtn) {//sendMessage2Server(EventID.EVENT_SLEEP);
            new SendMsgThread(EventID.EVENT_SLEEP).start();
        } else if (id == R.id.timeShutBtn) {//sendMessage2Server(EventID.EVENT_TIME_SHUTDOWN + ";3600");
            new SendMsgThread(EventID.EVENT_TIME_SHUTDOWN + ";3600").start();
        } else if (id == R.id.pastClipBtn) {//sendMessage2Server(EventID.CLIPBOARD + ";" + ClipboardUtils.pasteStringFromClipboard());
            new SendMsgThread(EventID.CLIPBOARD + ";" + ClipboardUtils.pasteStringFromClipboard()).start();
        } else if (id == R.id.openWebBtn) {//sendMessage2Server(EventID.OPEN_WEB + ";" + ClipboardUtils.pasteStringFromClipboard());
            new SendMsgThread(EventID.OPEN_WEB + ";" + ClipboardUtils.pasteStringFromClipboard()).start();
        }
	}
	private void initView(View view) {
		upBtn = view.findViewById(R.id.upBtn);
		leftBtn = view.findViewById(R.id.leftBtn);
		downBtn = view.findViewById(R.id.downBtn);
		rightBtn = view.findViewById(R.id.rightBtn);
		backBtn = view.findViewById(R.id.backBtn);
		spaceBtn = view.findViewById(R.id.spaceBtn);
		enterBtn = view.findViewById(R.id.enterBtn);
		sleepBtn = view.findViewById(R.id.sleepBtn);
		timeShutBtn = view.findViewById(R.id.timeShutBtn);
		pastClipBtn = view.findViewById(R.id.pastClipBtn);
		openWebBtn = view.findViewById(R.id.openWebBtn);
	}

	private class SendMsgThread extends Thread {
		private String msg;
		public SendMsgThread(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			sendMessage2Server(msg);
		}
	}
	private void sendMessage2Server(String msg) {
		try {
			socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName(Constant.IP);
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data,data.length,address,Constant.UDP_PORT);
			socket.send(packet);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
