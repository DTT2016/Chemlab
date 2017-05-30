package com.chemlab.systems;

import com.chemlab.R;
import com.chemlab.activity.WeekActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WorkSystemActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.system_work);

		((TextView) findViewById(R.id.textview_title)).setText("学生助理系统");
		findViewById(R.id.button_back).setOnClickListener(this);
		findViewById(R.id.work_arrange).setOnClickListener(this);
		findViewById(R.id.work_view).setOnClickListener(this);
		findViewById(R.id.work_freetime_report).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_back:
			finish();
			break;
		case R.id.work_arrange:
			//gotoActivity(WeekActivity.class);
			WeekActivity.actionStart(WorkSystemActivity.this, "排班表", WeekActivity.PURPOSE_VIEW_SCHEDULE);
			break;
		case R.id.work_view:
			
			break;
		case R.id.work_freetime_report:
			WeekActivity.actionStart(WorkSystemActivity.this, "空闲时间表", WeekActivity.PURPOSE_FREETIME_UPLOAD);
			break;
		default:
			break;
		}
	}
	
	private void gotoActivity(Class<?> clazz) {
		Intent intent = new Intent(WorkSystemActivity.this, clazz);
		startActivity(intent);
	}

}
