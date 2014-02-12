package com.bravo.bravomerchant.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.bravomerchant.bean.OrderItem;

public class JsonUtil {

	public static String getJsonStr(List<OrderItem> OrderItemList) throws JSONException{
		
		if(OrderItemList == null
				|| OrderItemList.size() == 0){
			
			return (new JSONArray()).toString();
		}
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		OrderItem orderItem = null;
		
		for (int i = 0; i < OrderItemList.size(); i++) {
			
			orderItem = OrderItemList.get(i);
			jsonObject = new JSONObject();

			jsonObject.put("barCode", orderItem.getBarCode());
			jsonObject.put("name", orderItem.getName());
			jsonObject.put("price", orderItem.getPrice());
			jsonObject.put("tax", orderItem.getTax());
			jsonObject.put("totalPrice", orderItem.getTotalPrice());
			jsonObject.put("unit", orderItem.getUnit());
			
			jsonArray.put(jsonObject);
		}
		
		return jsonArray.toString();
	}
	

	public static List<OrderItem> parseOrderItemListJsonStr(String jsonStr) throws JSONException{
		
		List<OrderItem> res = new ArrayList<OrderItem>();
		
		if(jsonStr == null
				|| "".equals(jsonStr)){
			
			return res;
		}
		
		JSONArray jsonArray = new JSONArray(jsonStr);
		JSONObject jsonObject = null;
		OrderItem orderItem = null;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			
			orderItem = new OrderItem();
			jsonObject = jsonArray.getJSONObject(i);

			orderItem.setBarCode(jsonObject.getString("barCode"));
			orderItem.setName(jsonObject.getString("name"));
			orderItem.setPrice(jsonObject.getDouble("price"));
			orderItem.setTax(jsonObject.getDouble("tax"));
			orderItem.setTotalPrice(jsonObject.getDouble("totalPrice"));
			orderItem.setUnit(jsonObject.getInt("unit"));
			
			res.add(orderItem);
		}
		
		return res;
	}
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}

}
