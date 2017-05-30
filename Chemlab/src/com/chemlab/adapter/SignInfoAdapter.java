package com.chemlab.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chemlab.R;
import com.chemlab.objs.WorkInfo;

public class SignInfoAdapter extends ArrayAdapter<WorkInfo>{

	private int resourceId;

	public SignInfoAdapter(Context context, int resource, List<WorkInfo> objects) {
		super(context, resource, objects);
		this.resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// return super.getView(position, convertView, parent);
		View view = null;

		WorkInfo signInfo= getItem(position);
		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder.text1 = (TextView) view.findViewById(R.id.text_col1);
			viewHolder.text2 = (TextView) view.findViewById(R.id.text_col2);
			
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.text1.setText(signInfo.getDate_time());
		viewHolder.text2.setText(signInfo.getState());
		
		return view;
	}

	class ViewHolder {
		TextView text1;
		TextView text2;
	}
}

