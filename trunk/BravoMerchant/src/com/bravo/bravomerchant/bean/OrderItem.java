package com.bravo.bravomerchant.bean;

import java.util.Random;

public class OrderItem {

	private String barCode;
	private String name;
	private Integer unit;
	private Double tax;
	private Double price;
	private Double totalPrice;

	public OrderItem(String barCode, Integer unit) {
		super();
		Random r = new Random();
		this.barCode = barCode;
		this.name = "productName"+r.nextInt(200);
		this.unit = unit;
		this.tax = 0d;
		this.price = (r.nextInt(200)+1)/100d;
		this.totalPrice = price*unit;
	}

	public OrderItem(){
		super();
		this.barCode = "";
		this.name = "";
		this.unit = 0;
		this.tax = 0d;
		this.price = 0d;
		this.totalPrice = 0d;
	}
	
	public OrderItem(String barCode, String name, Integer unit, Double tax, Double price,
			Double totalPrice) {
		super();
		this.barCode = barCode;
		this.name = name;
		this.unit = unit;
		this.tax = tax;
		this.price = price;
		this.totalPrice = totalPrice;
	}
	
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	

	public OrderItem addOne() {
		
		unit++;
		totalPrice += price;
		return this;
	}

	public OrderItem subtractOne() {
		
		if(--unit <= 0){
			return null;
		}
		totalPrice -= price;
		return this;
	}
	
	public OrderItem negate(){
		return null;
	}

	public boolean isEqual(OrderItem obj) {
		
		if(obj != null
				&& barCode.equals(obj.getBarCode())){
			return true;
		}
		
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
