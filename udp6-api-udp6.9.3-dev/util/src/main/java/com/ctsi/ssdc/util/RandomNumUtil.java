package com.ctsi.ssdc.util;

import java.util.Random;

/**
 * 产生随机数字符串
 * @author guojz
 * @version 1.0.1
 * @Description 
 * @date  2016年6月1日 上午11:23:06 
 * @Copyright 中国电信集团系统集成有限责任公司
 */
public class RandomNumUtil {
	
	/**
	 * 获取由num个随机数组成的字符串，每个随机数在0到9中产生
	 * @param num 产生随机数字符串的长度
	 * @return 长度为num的字符串
	 */
	

	public static String findRandomNums(int num){
		Random rand = new Random();
		int k;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			k=rand.nextInt(10);
			sb.append(k);
		}
		return sb.toString();
		
	}
	
	/**
	 * 获取由num个随机数组成的字符串，每个随机数在0到9中产生
	 * @param num 产生随机数字符串的长度
	 * @return 长度为num的字符串
	 */
	

	public static String findRandomNums(String num){
		int m = 6;
		try {
			m = Integer.parseInt(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Random rand = new Random();
		int k;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < m; i++) {
			k=rand.nextInt(10);
			sb.append(k);
		}
		return sb.toString();
		
	}


}
