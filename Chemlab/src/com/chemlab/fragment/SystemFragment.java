package com.chemlab.fragment;

import com.chemlab.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.chemlab.activity.CaptureActivity;
import com.chemlab.adapter.MyGridAdapter;
import com.chemlab.systems.AssistentSystemActivity;
import com.chemlab.systems.DrugSystemActivity;
import com.chemlab.util.MyApplication;
import com.chemlab.view.CircleMenuLayout;
import com.chemlab.view.CircleMenuLayout.OnMenuItemClickListener;
import com.chemlab.view.MyGridView;

public class SystemFragment extends Fragment {

	private View view;
	
	/*
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_functions, container, false);
		
		return view;
	}
	*/
	
	private CircleMenuLayout mCircleMenuLayout;
	
	private MyGridView system_gridview;
	private MyGridView assist_gridview;
	private MyGridView ext_gridview;
	
	//子系统
	private String[] mMenuTexts = new String[] {  "开放性实验", "药品管理 ", "出入库", 
			"实验器材","课程管理", " "};
	private int[] mMenuImgs = new int[] { 
		R.drawable.v2_apps1,
		R.drawable.v2_apps2, 
		R.drawable.v2_apps3,
		R.drawable.v2_apps4,
		R.drawable.v2_apps5,
		R.drawable.transparent
	};
	
	//学生助理
	private String[] mMenuTexts2 = new String[] {"留言板","签到/签退", "工作安排 ", "工时查询","扫一扫"," "};
	private int[] mMenuImgs2 = new int[] { 
		R.drawable.v2_apps1,
		R.drawable.v2_apps2, 
		R.drawable.v2_apps3,
		R.drawable.v2_apps4,
		R.drawable.v2_apps5,
		R.drawable.transparent
	};
	
	//其他探索
	private String[] mMenuTexts3 = new String[] {"探索","扫一扫"," "};
	private int[] mMenuImgs3 = new int[] { 
		R.drawable.v2_apps1,
		R.drawable.v2_apps2,
		R.drawable.transparent
	};
	/*private int[] mItemImgs = new int[] { R.drawable.home_mbank_1_normal,
			R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
			R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
			R.drawable.home_mbank_6_normal };
	*/
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_functions, container, false);
		
		//initCircleMenuView();
		initGridMenuView();
		
		return view;
	}
	
	private void initGridMenuView() {
		system_gridview=(MyGridView) view.findViewById(R.id.system_gridview);
		system_gridview.setAdapter(new MyGridAdapter(getActivity(),mMenuImgs,mMenuTexts));
		
		assist_gridview=(MyGridView) view.findViewById(R.id.assist_gridview);
		assist_gridview.setAdapter(new MyGridAdapter(getActivity(),mMenuImgs2,mMenuTexts2));
		
		ext_gridview=(MyGridView) view.findViewById(R.id.adventure_gridview);
		ext_gridview.setAdapter(new MyGridAdapter(getActivity(),mMenuImgs3,mMenuTexts3));
		
		system_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parant, View view, int position,
					long id) {
				Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
			}
		});
		
		assist_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parant, View view, int position,
					long id) {
				Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
			}
		});
		
		ext_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parant, View view, int position,
					long id) {
				Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
				if (position == 1) {
					startAction(CaptureActivity.class);
				}
			}
		});
	}
	
	void initCircleMenuView(){
		mCircleMenuLayout = (CircleMenuLayout) view.findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mMenuImgs, mMenuTexts);
		
		mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			
			@Override
			public void itemClick(View view, int pos)
			{
				Toast.makeText(MyApplication.getContext(), mMenuTexts[pos],
						Toast.LENGTH_SHORT).show();
				
				switch (pos) {
				case 0:
					break;
				case 1:
					startAction(DrugSystemActivity.class);
					break;
				case 4:
					startAction(AssistentSystemActivity.class);
					break;
				default:
					break;
				}
			}
			
			@Override
			public void itemCenterClick(View view)
			{
				Toast.makeText(MyApplication.getContext(),
						"必须登录才能使用完整功能!",
						Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	
	private void startAction(Class<?> clazz){
		Intent intent = new Intent(getActivity(), clazz);
		startActivity(intent);
	}
}
