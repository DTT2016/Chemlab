package com.chemlab.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtil {
	
	public static String ID = MyApplication.ID;
	public static String PW = MyApplication.PW;
	public static String HOST = "bxw2359770225.my3w.com";
	
	public static final String ADDRESS_LOGIN_HANDLER = MyApplication.HOST+"/Ashx/LoginHandler.ashx";
	public static final String ADDRESS_NEWS_HANDLER = MyApplication.HOST+"/Ashx/NewsHandler.ashx";
	public static final String ADDRESS_DRUG_HANDLER = MyApplication.HOST+"/Ashx/DrugHandler.ashx";
	
	
	public static void sendHttpRequest(final String address,final String argvs,final HttpCallbackListener listener) {
		
		if (!isNetworkAvailable()) {
			//Toast.makeText(MyApplication.getContext(), "Network is unavailable!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					
					//GET
					/*connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);*/
					
					//POST
					connection.setRequestMethod("POST");
					DataOutputStream out = new DataOutputStream(connection.getOutputStream());
					out.writeBytes(argvs);
					
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					
					while ((line=reader.readLine())!=null) {
						response.append(line);
					}
					
					if (listener!=null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener!=null) {
						listener.onError(e);
					}
				} finally {
					if (connection!=null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	public static boolean isNetworkAvailable() {
		Context context = MyApplication.getContext();
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		return networkInfo!=null&&networkInfo.isConnectedOrConnecting();
	}
	
	public static String requestWebservice(final String endPoint,String methodName,
	          String [] parameters,String [] parValues){
		return "";
	}
	
	public static String createJsonStr(String function,String attachArgvs){
		String jsonString="json={" +
				"\"type\":\""+function+"\"," +
				"\"id\":\""+ID+"\"," +
				"\"pw\":\""+PW+"\"," +
				attachArgvs+
				"}";
		return jsonString;
	}
}
