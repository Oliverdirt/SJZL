package com.ctsi.ssdc.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JSON相关工具类
 * @author liminqiang
 *
 */
public class JSONUtil {
	/**
	 * 将对象转成JSONObject
	 * @param obj 对象
	 * @param objClass 对象类型
	 * @return JSONObject
	 */
	

	public static <T> JSONObject changeJSONObject(T obj, Class<T> objClass) {
		if (obj == null) {
			return null;
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(objClass, Date.class, new DataJsonValueProcessor(1));
		return JSONObject.fromObject(obj, config);
	}

	/**
	 * 将对象集合转成JSONArray
	 * @param objList 对象集合
	 * @param objClass 对象类型
	 * @return JSONArray
	 */
	

	public static <T> JSONArray changeJSONArray(List<T> objList, Class<T> objClass) {
		if (objList == null) {
			objList = new ArrayList<T>();
		}
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(objClass, Date.class, new DataJsonValueProcessor(1));
		return JSONArray.fromObject(objList, config);
	}
	
	static class DataJsonValueProcessor implements JsonValueProcessor {
		// 日期格式，默认是"yyyy年MM月dd日",1为"yyyy年MM月dd日 HH时mm分ss秒"
		private int dateType = 0;

		public DataJsonValueProcessor() {
			super();
		}

		public DataJsonValueProcessor(int dateType) {
			this.dateType = dateType;
		}

		@Override
		public Object processArrayValue(Object arg0, JsonConfig arg1) {
			return null;
		}

		@Override
		public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
			if (arg1 == null) {
				return "";
			} else {
				if (arg1 instanceof Date) {
					if (dateType == 1) {
						return DateUtil.formatDateObjectChnTwo((Date) arg1);
					} else if (dateType == 2) {
						return DateUtil.format((Date) arg1, "yyyy-MM-dd");
					} else {
						return DateUtil.formatDateObjectChn((Date) arg1);
					}
				}
			}
			return arg1;
		}

	}
	
}
