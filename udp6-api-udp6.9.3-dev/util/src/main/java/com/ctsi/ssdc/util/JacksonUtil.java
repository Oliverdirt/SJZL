package com.ctsi.ssdc.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * jackson工具类
 * @author ctsi
 *
 */
public class JacksonUtil {

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 将对象转化为json字符串
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 * @return String
	 * @exception @createTime：2018年9月28日
	 * 
	 */
	

	public static String bean2Json(Object obj) throws IOException {
		StringWriter sw = new StringWriter();
		try (JsonGenerator gen = new JsonFactory().createGenerator(sw);) {
			mapper.writeValue(gen, obj);

			return sw.toString();
		}

	}

	/**
	 * 将json字符串转化为对象
	 * 
	 * @param jsonStr
	 * @param objClass
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @return T
	 * @exception @createTime：2018年9月28日
	 */
	

	public static <T> T json2Bean(String jsonStr, Class<T> objClass)
			throws JsonProcessingException, JsonMappingException, IOException {
		return mapper.readValue(jsonStr, objClass);
	}

}