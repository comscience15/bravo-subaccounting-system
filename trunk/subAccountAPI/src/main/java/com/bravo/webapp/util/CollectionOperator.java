package com.bravo.webapp.util;


public interface CollectionOperator<T> extends Comparable<T> {
	public String getKey();
	public T add(T obj);
	public T subtract(T obj);
	public T negate();
	
}
