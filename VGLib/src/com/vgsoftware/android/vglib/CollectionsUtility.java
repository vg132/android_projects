package com.vgsoftware.android.vglib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionsUtility
{
	public static <K, V> Map<K, V> asSortedKeyMap(final Map<K, V> map, Comparator<K> comparator)
	{
		List<K> sortedKeys = new ArrayList<K>(map.keySet());
		Collections.sort(sortedKeys, comparator);
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (K key : sortedKeys)
		{
			sortedMap.put(key, map.get(key));
		}
		return sortedMap;
	}
}
