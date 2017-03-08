package com.chemlab.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.chemlab.R;
import com.chemlab.activity.ContactActivity;
import com.chemlab.activity.UpdateContactActivity;
import com.chemlab.adapter.ContactAdapter;
import com.chemlab.objs.Contact;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.MyApplication;
import com.chemlab.view.RefreshableView;
import com.chemlab.view.RefreshableView.PullToRefreshListener;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ContactFragment extends Fragment implements OnItemClickListener {

	private static final int UPDATED = 0;
	
	private View view;
	private Button setInfoButton;

	private ContactAdapter adapter;
	private ListView contactListView;
	private List<Contact> contactList;
	RefreshableView refreshableView;
	
	private String argvs="";

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATED:
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		contactList = new ArrayList<Contact>();
		adapter = new ContactAdapter(activity, R.layout.item_contact,
				contactList);
		
		updateContacts();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_contacts, container, false);
		
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		setInfoButton = (Button) view.findViewById(R.id.button_contact);
		setInfoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UpdateContactActivity.actionStart(getActivity(), MyApplication.ID);
			}
		});

		contactListView = (ListView) view.findViewById(R.id.contacts_list);
		contactListView.setAdapter(adapter);
		contactListView.setOnItemClickListener(this);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					updateContacts();
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);
		return view;
	}
	
	private void updateContacts() {
		argvs = "Type=GetInfoAll&ID=" + HttpUtil.ID + "&PW=" + HttpUtil.PW;
		HttpUtil.sendHttpRequest(HttpUtil.ADDRESS_LOGIN_HANDLER, argvs, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				getContactsWithResponse(response);
				
				Message message = new Message();
				message.what = UPDATED;
				handler.sendMessage(message);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	
	private void getContactsWithResponse(String data){
		Log.d("ContactFragment", data);
		contactList.clear();
		
		try {
			JSONObject jsonObject = new JSONObject(data);
			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject contactInfo = jsonObject.getJSONObject("" + i);
				
				Contact contact = new Contact(R.drawable.photo, contactInfo.getString("name"), contactInfo.getString("phonenumber_long"));
				contact.setQQNumber(contactInfo.getString("QQ"));
				contact.setEmail(contactInfo.getString("email"));
				contactList.add(contact);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Contact contact = contactList.get(position);
		ContactActivity.actionStart(getActivity(), contact);
	}
}
