package com.bravo.webapp.bean;

import java.math.BigDecimal;

import com.bravo.webapp.exception.UnknownResourceException;
import com.bravo.webapp.util.CollectionOperator;

public class OrderItem implements CollectionOperator<OrderItem>, Cloneable {

	private long transactionID;
	private String productID;
	private String productName;
	private BigDecimal unit;
	private BigDecimal tax;
	private BigDecimal totalPrice;
	private boolean refundable;

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getUnit() {
		return unit;
	}

	public void setUnit(BigDecimal unit) {
		this.unit = unit;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public boolean isRefundable() {
		return refundable;
	}

	public void setRefundable(boolean refundable) {
		this.refundable = refundable;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return getProductID();
	}

	@Override
	public OrderItem add(OrderItem obj) {
		// TODO Auto-generated method stub
		unit = unit.add(obj.getUnit());
		totalPrice = totalPrice.add(obj.getTotalPrice());

		return this;
	}

	@Override
	public OrderItem subtract(OrderItem obj) {
		// TODO Auto-generated method stub
		unit = unit.subtract(obj.getUnit());
		totalPrice = totalPrice.subtract(obj.getTotalPrice());

		return this;
	}
	
	@Override
	public OrderItem negate(){
		unit = unit.negate();
		totalPrice = totalPrice.negate();
		
		return this;
	}

	@Override
	public int compareTo(OrderItem obj) {
		// TODO Auto-generated method stub
		if (productID.equals(obj.getProductID())) {
			return unit.compareTo(obj.getUnit());
		}
		throw new UnknownResourceException(
				"Can't compare item with different ProductID");
	}

	@Override
	public String toString() {
		return "OrderItem [transactionID=" + transactionID + ", productID="
				+ productID + ", productName=" + productName + ", unit=" + unit
				+ ", tax=" + tax + ", totalPrice=" + totalPrice + "]";
	}

}
