package com.lzq.econnect.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.lzq.econnect.R;
import com.lzq.econnect.base.BaseActivity;
import com.lzq.econnect.bean.QABean;

public class QADetailActivity extends BaseActivity {
	private Toolbar toolbar;
	private TextView qaTitle;
	private TextView qaContent;

	// @Bind(R.id.qa_title)
	// TextView qaTitle;
	// @Bind(R.id.qa_content)
	// TextView qaContent;
	// @Bind(R.id.toolbar)
	// Toolbar toolbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected int setContentViewId() {
		return R.layout.activity_qadetail;
	}

	@Override
	protected void doBusiness() {
		initView();
		initToolbar();
		QABean bean = getIntent().getParcelableExtra("qaClass");
		if (bean != null) {
			qaTitle.setText(bean.getTitle());
			qaContent.setText(bean.getContent());
		}
	}

	private void initToolbar() {

		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle("QA详情");
		}

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
	private void initView() {
		toolbar = findViewById(R.id.toolbar);
		qaTitle = findViewById(R.id.qa_title);
		qaContent = findViewById(R.id.qa_content);
	}
}
