package com.chemlab.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chemlab.R;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.LogUtil;

@SuppressLint("SimpleDateFormat")
public class WeekActivity extends Activity {

	/** 第一个无内容的格子 */
	protected TextView empty;
	/** 星期一的格子 */
	protected TextView monColum;
	/** 星期二的格子 */
	protected TextView tueColum;
	/** 星期三的格子 */
	protected TextView wedColum;
	/** 星期四的格子 */
	protected TextView thrusColum;
	/** 星期五的格子 */
	protected TextView friColum;
	/** 星期六的格子 */
	protected TextView satColum;
	/** 星期日的格子 */
	protected TextView sunColum;
	/** 课程表body部分布局 */
	protected RelativeLayout course_table_layout;
	/** 屏幕宽度 **/
	protected int screenWidth;
	/** 课程格子平均宽度 **/
	protected int aveWidth;
	
	private int gridHeight;
	//五种颜色的背景
	int[] background = {R.drawable.course_info_blue, R.drawable.course_info_green, 
						R.drawable.course_info_yellow, R.drawable.course_info_red,
						R.drawable.course_info_yellow};
			
	public static final int PURPOSE_VIEW_SCHEDULE = 0;
	public static final int PURPOSE_FREETIME_UPLOAD = 1;
	public static final int PURPOSE_VIEW_COURSE = 2;
	
	//private List<TextView> textViewList;
	private static int week_plus=0;
	private int purpose;
	private String [] timeArray = {
		"8:00\n——\n9:00",
		"9:00\n——\n10:00",
		"10:00\n——\n11:00",
		"11:00\n——\n12:00",
		"12:00\n——\n13:00",
		"13:00\n——\n14:00",
		"14:00\n——\n15:00",
		"15:00\n——\n16:00",
		"16:00\n——\n17:00",
		"17:00\n——\n18:00"
	};
	
	public static void actionStart(Context context, String title, int purpose) {
		Intent intent = new Intent(context, WeekActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("purpose", purpose);
		context.startActivity(intent);
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_table);
		
		purpose = getIntent().getIntExtra("purpose", 0);
		String titleString = getIntent().getStringExtra("title");
		((TextView) findViewById(R.id.textview_title)).setText(titleString);
		findViewById(R.id.button_back).setOnClickListener(listener); 
		
		if (purpose == PURPOSE_FREETIME_UPLOAD) {
			findViewById(R.id.work_submit).setVisibility(View.VISIBLE);
			findViewById(R.id.work_submit).setOnClickListener(listener);
		} else if (purpose == PURPOSE_VIEW_SCHEDULE) {
			//findViewById(R.id.layout_week_sel).setVisibility(View.VISIBLE);
			//findViewById(R.id.week_sel_prev).setOnClickListener(listener);
			//findViewById(R.id.week_sel_prev).setOnClickListener(listener);
		}
		//textViewList = new ArrayList<TextView>();
		
		
		//获得列头的控件
		empty = (TextView) this.findViewById(R.id.test_empty);
		monColum = (TextView) this.findViewById(R.id.test_monday_course);
		tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
		wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
		thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
		friColum = (TextView) this.findViewById(R.id.test_friday_course);
		satColum  = (TextView) this.findViewById(R.id.test_saturday_course);
		sunColum = (TextView) this.findViewById(R.id.test_sunday_course);
		course_table_layout = (RelativeLayout) this.findViewById(R.id.test_course_rl);
		DisplayMetrics dm = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//屏幕宽度
		int width = dm.widthPixels;
		//平均宽度
		int aveWidth = width / 8;
		//第一个空白格子设置为25宽
		empty.setWidth(aveWidth * 3/4);
		monColum.setWidth(aveWidth * 33/32 + 1);
		tueColum.setWidth(aveWidth * 33/32 + 1);
		wedColum.setWidth(aveWidth * 33/32 + 1);
		thrusColum.setWidth(aveWidth * 33/32 + 1);
		friColum.setWidth(aveWidth * 33/32 + 1);
		satColum.setWidth(aveWidth * 33/32 + 1);
		sunColum.setWidth(aveWidth * 33/32 + 1);
		this.screenWidth = width;
		this.aveWidth = aveWidth;
		int height = dm.heightPixels;
		gridHeight = height / 12;
		//设置课表界面
		empty.setText("");
		//动态生成12 * maxCourseNum个textview
		for(int i = 1; i <= 10; i ++){
			
			for(int j = 1; j <= 8; j ++){
				
				TextView tx = new TextView(WeekActivity.this);
				tx.setId((i - 1) * 8  + j);
				//除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
				if(j < 8)
					tx.setBackgroundDrawable(WeekActivity.this.
							getResources().getDrawable(R.drawable.course_text_view_bg));
				else
					tx.setBackgroundDrawable(WeekActivity.this.
						getResources().getDrawable(R.drawable.course_table_last_colum));
				//相对布局参数
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
						aveWidth * 33 / 32 + 1,
						gridHeight);
				//文字对齐方式
				tx.setGravity(Gravity.CENTER);
				//字体样式
				tx.setTextAppearance(this, R.style.courseTableText);
				//如果是第一列，需要设置课的序号（1 到 12）
				if(j == 1)
				{
					//tx.setText(String.valueOf(i));
					tx.setText(timeArray[i-1]);
					rp.width = aveWidth * 3/4;	
					//设置他们的相对位置
					if(i == 1)
						rp.addRule(RelativeLayout.BELOW, empty.getId());
					else
						rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
				}
				else
				{
					rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
					rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - 1);
					tx.setText("");
				}
					
				tx.setLayoutParams(rp);
				
				//textViewList.add(tx);
				tx.setTag(i+","+(j-1));
				tx.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(WeekActivity.this, v.getTag().toString(), 1).show();
					}
				});
				
				course_table_layout.addView(tx);
			}
		}
		
		//addMyCourse(2,6);
		requestSchedule();
	}
	
	private void requestSchedule(){
		// 获取记录
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		//一周第一天是否为星期天
		boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
		//获取周几
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		//若一周第一天为星期天，则-1
		if(isFirstSunday){
			weekDay = weekDay - 1;
			if(weekDay == 0){
				weekDay = 7;
			}
		}
		weekDay = -weekDay + week_plus * 7;
		calendar.add(Calendar.DATE, weekDay);
		String date_from = format.format(calendar.getTime());
		calendar.add(Calendar.DATE, 7);
		String date_to = format.format(calendar.getTime());
		
		final List<String> posList = new ArrayList<String>();
		
		HttpUtil.sendHttpRequest(
				HttpUtil.ADDRESS_STUDENT_HANDLER,
				HttpUtil.createJsonStr("GetWorkInfoByID", "\"from\":\""
						+ date_from + "\",\"to\":\"" + date_to
						+ "\",\"find_id\":\"" + HttpUtil.ID + "\","),
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						LogUtil.d("Tag", response);
						JSONObject responseObject;
						try {
							responseObject = new JSONObject(response);

							if (responseObject.getString("error").equals("0")) {
								JSONArray jsonObjArray = responseObject.getJSONArray("data");
								for (int i = 0; i < jsonObjArray.length(); i++) {
									JSONObject workInfo = jsonObjArray.getJSONObject(i);
									String pos = workInfo.getString("time_type");
									posList.add(pos);
								}
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										for (String pos : posList) {
											int w = pos.charAt(0)-'0';
											int n = Integer.parseInt(pos.substring(1));
											addMyCourse(w,n);
										}
									}
								});
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception e) {
						e.printStackTrace();
					}

				});
	}
	
	private void addMyCourse(int dayofweek,int classNum){
		String msg = "√";
		addCourseInfo(dayofweek,classNum,1,msg,dayofweek%5);
	}
	
	// 添加课程信息
	private void addCourseInfo(int dayofweek,int classNum,int classCount,String msg,int colorindex){
		
		TextView courseInfo = new TextView(this);
		courseInfo.setText(msg);
		
		//该textview的高度根据其节数的跨度来设置
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				aveWidth * 31 / 32,
				(gridHeight - 10) * classCount );
		//textview的位置由课程开始节数和上课的时间（day of week）确定
		rlp.topMargin = 5 + (classNum - 1) * gridHeight;
		//rlp.bottomMargin = classNum * gridHeight -5;
		
		rlp.leftMargin = 2;
		// 偏移由这节课是星期几决定
		rlp.addRule(RelativeLayout.RIGHT_OF, dayofweek);
		//字体剧中
		courseInfo.setGravity(Gravity.CENTER);
		// 设置一种背景
		courseInfo.setBackgroundResource(background[colorindex]);
		courseInfo.setTextSize(12);
		courseInfo.setLayoutParams(rlp);
		courseInfo.setTextColor(Color.WHITE);
		//设置不透明度
		courseInfo.getBackground().setAlpha(222);
		course_table_layout.addView(courseInfo);
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_back:
				finish();
				break;
			case R.id.week_sel_prev:
				week_plus--;
				requestSchedule();
				break;
			case R.id.week_sel_next:
				week_plus++;
				requestSchedule();
				break;
			case R.id.work_submit:
				break;
			default:
				break;
			}
		}
	};
}
