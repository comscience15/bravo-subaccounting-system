package com.bravo.webapp.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CollectionUtil {

	public static <T extends CollectionOperator<T>> Map<String, T> listToMap(
			List<T> list) {
		HashMap<String, T> map = new HashMap<String, T>(list.size());
		Iterator<T> iterator = list.iterator();

		while (iterator.hasNext()) {
			T obj = iterator.next();
			map.put(obj.getKey(), obj);
		}

		return map;
	}

	public static <T extends CollectionOperator<T>> Map<String, T> uniqueListToMap(
			List<T> list) {
		HashMap<String, T> map = new HashMap<String, T>(list.size());
		Iterator<T> iterator = list.iterator();
		
		while (iterator.hasNext()) {
			T obj = iterator.next();
			T obj2 = map.get(obj.getKey());
			if (obj2 == null){
				map.put(obj.getKey(), obj);
			} else {
				map.put(obj.getKey(), obj.add(obj2));
			}
		}

		return map;
	}
}
