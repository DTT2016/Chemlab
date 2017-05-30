package com.chemlab.managers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chemlab.objs.Drug;
import com.chemlab.objs.DrugMix;
import com.chemlab.objs.Equipment;
import com.chemlab.objs.News;
import com.chemlab.objs.WorkInfo;

public class JsonManager {

	public static final int TYPE_SIGN = 0;
	public static final int TYPE_WORK = 0;
	
	public JsonManager() {
	}

	public static List<News> getNewsArray(JSONArray jsonArray) {

		if (jsonArray.length() == 0) {
			return null;
		}

		List<News> list = new ArrayList<News>();

		try {
			News news;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject newsInfo = jsonArray.getJSONObject(i);
				news = new News();

				news.setArticleID(newsInfo.getString("ArticleID"));
				news.setTitle(newsInfo.getString("Title"));
				// news.setContent_1(newsInfo.getString("Content_1"));
				news.setDateTime(newsInfo.getString("DateTime"));
				news.setMember_Name(newsInfo.getString("Member_Name"));
				news.setInfor(newsInfo.getString("infor"));
				news.setType(newsInfo.getString("type"));
				news.setHits(newsInfo.getString("Hits"));
				// Log.i("Tag", news.getTitle());

				list.add(news);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static List<Drug> getDrugArray(JSONArray jsonArray) {

		if (jsonArray.length() == 0) {
			return null;
		}

		List<Drug> list = new ArrayList<Drug>();

		try {
			Drug drug;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject drugInfo = jsonArray.getJSONObject(i);
				drug = new Drug();
				
				drug.setDrug_name(drugInfo.getString("drug_name"));
				drug.setDrug_another_name(drugInfo.getString("drug_another_name"));
				drug.setFen_zi_shi(drugInfo.getString("fen_zi_shi"));
				//LogUtil.d("Tag", drug.getDrug_name()+": "+drugInfo.getString("counting"));
				//drug.setCounting(drugInfo.getString("counting"));
				//drug.setStandard(drugInfo.getString("standard"));
				
				list.add(drug);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public static List<DrugMix> getDrugMixArray(JSONArray jsonArray) {

		if (jsonArray.length() == 0) {
			return null;
		}

		List<DrugMix> list = new ArrayList<DrugMix>();

		try {
			DrugMix drugMix;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject drugMixInfo = jsonArray.getJSONObject(i);
				drugMix = new DrugMix();
				
				drugMix.setMix_name(drugMixInfo.getString("drug_mix"));
				drugMix.setAttention(drugMixInfo.getString("attention"));
				drugMix.setStandard(drugMixInfo.getString("standard"));
				list.add(drugMix);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public static List<Equipment> getEquipmentArray(JSONArray jsonArray) {

		if (jsonArray.length() == 0) {
			return null;
		}

		List<Equipment> list = new ArrayList<Equipment>();

		try {
			Equipment equipment;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject equipmentInfo = jsonArray.getJSONObject(i);
				equipment = new Equipment();
				
				equipment.setName(equipmentInfo.getString("equip_name"));
				equipment.setPrice(equipmentInfo.getString("price"));
				equipment.setFactory(equipmentInfo.getString("factory"));
				equipment.setModel(equipmentInfo.getString("model"));
				equipment.setDetail(equipmentInfo.getString("detail"));
				
				list.add(equipment);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public static List<WorkInfo> getWorkInfoInfoArray(JSONArray jsonArray,int type) {

		if (jsonArray.length() == 0) {
			return null;
		}

		List<WorkInfo> list = new ArrayList<WorkInfo>();

		try {
			WorkInfo sInfo;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject signInfo = jsonArray.getJSONObject(i);
				sInfo = new WorkInfo();
				
				sInfo.setTime_id(signInfo.getString("time_id"));
				sInfo.setID(signInfo.getString("ID"));
				sInfo.setName(signInfo.getString("name"));
				sInfo.setDate_time(signInfo.getString("date_time"));
				
				if (type == TYPE_SIGN) {
					sInfo.setState(signInfo.getString("state"));
				} else if (type == TYPE_WORK) {
					sInfo.setState(signInfo.getString("time_type"));
				}
				

				list.add(sInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

}
