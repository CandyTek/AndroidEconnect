package com.lzq.econnect.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.lzq.econnect.R;
import com.lzq.econnect.base.BaseActivity;
import com.lzq.econnect.utils.UIUtil;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * function: 关于和分享App界面
 * Created by lzq
 */

public class AboutActivity extends BaseActivity {
	private TextView tvVersion;

	// @Bind(R.id.toolbar)
	Toolbar toolbar;
	// @Bind(R.id.version_num)
	// TextView tvVersion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}
	@Override
	protected int setContentViewId() {
		return R.layout.activity_about;
	}

	@Override
	protected void doBusiness() {
		initView();
		initToolbar();
		tvVersion.setText("V" + UIUtil.getVersion() + "版本");
	}

	private void initToolbar() {

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle("关于");
		}

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_about,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_share) {//Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
			showShare();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	private void showShare() {
		ShareSDK.initSDK(this);

		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
		oks.setTitle("分享");
		oks.setText("大家来一起使用E连吧！");
		//分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		oks.setImageUrl("http://wx4.sinaimg.cn/mw690/e2dee9c4ly1fe7m6zpjagj20dw09b0u8.jpg");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://huaban.com/c3yxwyd4ajp/");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("ShareSDK");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://huaban.com/c3yxwyd4ajp/");

		// 启动分享GUI
		oks.show(this);
	}

	private void initView() {
		toolbar = findViewById(R.id.toolbar);
		tvVersion = findViewById(R.id.version_num);
	}
}
