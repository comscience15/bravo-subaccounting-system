package com.bravo.bravomerchant.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.widget.ArrayAdapter;

public class Order{

	private String cardId;
	private Map<String, OrderItem> orderItems;

	public Order() {

		cardId = "";
		orderItems = new HashMap<String, OrderItem>();
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Map<String, OrderItem> getOrderItems() {
		return orderItems;
	}

	public void addItem(String barCode) {

		if (orderItems.containsKey(barCode)) {
			orderItems.get(barCode).addOne();
		} else {
			orderItems.put(barCode, new OrderItem(barCode, 1));
		}
	}

	public void subtract(String barCode) {

		if (orderItems.containsKey(barCode)) {
			
			if(orderItems.get(barCode).subtractOne() == null){

				orderItems.remove(barCode);
			}
		}
	}

	public void negate(String barCode) {

		orderItems.get(barCode).negate();
	}

	public List<String> getItemInfoList() {
		
		List<String> res = new ArrayList<String>();
		
		Iterator orderItemsIterator = orderItems.entrySet().iterator();
		Entry entry = null;
		OrderItem orderItem = null;
		
		while(orderItemsIterator.hasNext()){
			
			entry = (Entry) orderItemsIterator.next();
			orderItem = (OrderItem) entry.getValue();
			
			res.add("BarCode:"+orderItem.getBarCode()+"  unit:"+orderItem.getUnit());
		}
		return res;
	}

}
