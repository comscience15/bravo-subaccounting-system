package com.bravo.bravomerchant.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author jiawl
 *
 */
public class Order{

	private String cardId;// no use currently
	private Map<String, OrderItem> orderItems;

	public Order() {

		cardId = "";
		orderItems = new HashMap<String, OrderItem>();
	}

	/**
	 * use bar codes list to init the itemList
	 * @param productBarCodesList
	 */
	public Order(List<String> productBarCodesList) {
		
		this();
		
		if(productBarCodesList != null
				&& !productBarCodesList.isEmpty()){
			
			for(String productBarCode : productBarCodesList){
				
				this.addItem(productBarCode);
			}
		}
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

	public List<OrderItem> getItemInfoList() {
		
		List<OrderItem> res = new ArrayList<OrderItem>();
		Iterator<Entry<String, OrderItem>> orderItemsIterator = orderItems.entrySet().iterator();
		Entry<String, OrderItem> entry = null;
		while(orderItemsIterator.hasNext()){
			
			entry = orderItemsIterator.next();
			res.add(entry.getValue());
		}
		return res;
	}

}
