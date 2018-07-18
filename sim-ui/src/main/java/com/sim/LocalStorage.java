package com.sim;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalStorage {

	private static Map<String, Object> localMap = new ConcurrentHashMap<>();

	public static Object getValue(String key) {
		return localMap.get(key);
	}

	public static void setValue(String key, Object value) {
		localMap.put(key, value);
	}

	public static Object clearValue(String key) {
		if (localMap.get(key) != null) {
			return localMap.remove(key);
		}
		return null;
	}
}
