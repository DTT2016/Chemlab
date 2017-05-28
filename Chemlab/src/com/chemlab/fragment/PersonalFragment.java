package com.chemlab.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chemlab.R;
import com.chemlab.about.AboutChemlab;
import com.chemlab.about.FeedbackActivity;
import com.chemlab.activity.SettingActivity;
import com.chemlab.activity.SignInActivity;
import com.chemlab.activity.UserActivity;
import com.chemlab.util.MyApplication;

public class PersonalFragment extends Fragment implements OnClickListener {

	private View view;

	/*
	 * LinearLayout layout; BootstrapButton checkButton; BootstrapButton
	 * thougtButton; BootstrapButton aboutChemlab; BootstrapButton aboutApp;
	 * BootstrapButton quitButton;
	 */

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_personal, container, false);
		
		view.findViewById(R.id.id_user_info).setOnClickListener(this);
		view.findViewById(R.id.id_check_in).setOnClickListener(this);
		view.findViewById(R.id.id_feedback).setOnClickListener(this);
		view.findViewById(R.id.id_about_chemlab).setOnClickListener(this);
		view.findViewById(R.id.id_setting).setOnClickListener(this);
		view.findViewById(R.id.id_quit_app).setOnClickListener(this);
		
		((TextView)view.findViewById(R.id.id_user_name)).setText(MyApplication.ME.getName());
		((TextView)view.findViewById(R.id.id_user_role)).setText(MyApplication.ME.getIdebtity());

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_user_info:
			UserActivity.actionStart(getActivity(), MyApplication.ME,UserActivity.FROM_PERSONAL);
			break;
		case R.id.id_check_in:
			startAction(SignInActivity.class);
			break;
		case R.id.id_feedback:
			startAction(FeedbackActivity.class);
			break;
		case R.id.id_about_chemlab:
			startAction(AboutChemlab.class);
			break;
		case R.id.id_setting:
			startAction(SettingActivity.class);
			break;
		case R.id.id_quit_app:
			getActivity().finish();
			break;
		default:
			break;
		}
	}
	
	private void startAction(Class<?> clazz) {
		Intent intent = new Intent(getActivity(), clazz);
		startActivity(intent);
	}
}
