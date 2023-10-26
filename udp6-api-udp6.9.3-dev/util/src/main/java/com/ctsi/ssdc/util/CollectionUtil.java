package com.ctsi.ssdc.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import static com.ctsi.ssdc.consts.ComponentConstant.UTIL;

/**
 * Collection工具类
 * @author liyongsheng
 *
 */
public class CollectionUtil {
	/**
	 * 不允许实例化
	 */
	private CollectionUtil() {
	}

	/**
	 * 获取两个集合的不同元素
	 * 
	 * @param coll1
	 * @param coll2
	 * @return Map,其中key=PolymericElementsOfColl1为coll1中独有元素，key=PolymericElementsOfColl2为coll2中独有元素
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	

	public static Map getDiffent(Collection coll1, Collection coll2) {
		// 使用LinkeList防止差异过大时,元素拷贝
		Map csReturn = new HashMap<String, Object>();
		Collection dif1 = new LinkedList();
		Collection dif2 = new LinkedList();
		Collection max = coll1;
		Collection min = coll2;
		// 先比较大小,这样会减少后续map的if判断次数
		if (coll1.size() < coll2.size()) {
			max = coll2;
			min = coll1;
		}
		// 直接指定大小,防止再散列
		Map<Object, Integer> map = new HashMap<Object, Integer>(max.size());
		for (Object object : max) {
			map.put(object, 1);
		}
		for (Object object : min) {
			if (map.get(object) == null) {
				dif1.add(object);
			} else {
				map.put(object, 2);
			}
		}
		for (Map.Entry<Object, Integer> entry : map.entrySet()) {
			if (entry.getValue() == 1) {
				dif2.add(entry.getKey());
			}
		}
		if (coll1.size() >= coll2.size()) {
			csReturn.put("PolymericElementsOfColl1", dif2);
			csReturn.put("PolymericElementsOfColl2", dif1);
		} else {
			csReturn.put("PolymericElementsOfColl1", dif1);
			csReturn.put("PolymericElementsOfColl2", dif2);
		}
		return csReturn;
	}
}
