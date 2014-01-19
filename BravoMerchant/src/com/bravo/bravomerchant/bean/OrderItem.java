package com.bravo.bravomerchant.bean;

public class OrderItem {
	
	private String barCode;
	private Integer unit;
	
	public OrderItem(String barCode, Integer unit) {
		super();
		this.barCode = barCode;
		this.unit = unit;
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
		return this;
	}

	public OrderItem subtractOne() {
		
		if(--unit <= 0){
			return null;
		}
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
}
