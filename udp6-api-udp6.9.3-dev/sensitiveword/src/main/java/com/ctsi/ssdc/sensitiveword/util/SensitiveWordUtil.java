package com.ctsi.ssdc.sensitiveword.util;

import com.ctsi.ssdc.sensitiveword.domain.CscpSensitiveWord;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 敏感词处理工具 - DFA算法实现
 *
 */
public class SensitiveWordUtil {
	
	private SensitiveWordUtil() {
	}
    /**
     * 最小匹配规则
     */
    public static final int TYPE_MIN_MATCH = 1;
    /**
     * 最大匹配规则
     */
    public static final int TYPE_MAX_MATCH = 2;
 
    /**
     * 敏感词集合
     */
    @SuppressWarnings("rawtypes")
    protected static Map sensitiveWordMap = new ConcurrentHashMap();
    
    
    /**
     * 敏感词集合(替换)
     */
    protected  static Map <String, String> sensitiveReplaceMap = new ConcurrentHashMap<String, String>();
	
 
    /**
     * 初始化敏感词库，构建DFA算法模型
     *
     * @param list 敏感词库
     */
    public static synchronized void init(List <CscpSensitiveWord> list) {
    	for(CscpSensitiveWord cscpSensitiveWord : list) {
    	    if(null == cscpSensitiveWord.getSenReplace()){
                sensitiveReplaceMap.put(cscpSensitiveWord.getSenContent(),"***");
            }else{
                sensitiveReplaceMap.put(cscpSensitiveWord.getSenContent(), cscpSensitiveWord.getSenReplace());
            }

    	}
        initSensitiveWordMap(sensitiveReplaceMap.keySet());
    }
    
    /**
     * 敏感词库新增，构建DFA算法模型
     *
     * @param
     */
    public static void add(CscpSensitiveWord cscpSensitiveWord) {
        if(null == cscpSensitiveWord.getSenReplace()){
            sensitiveReplaceMap.put(cscpSensitiveWord.getSenContent(), "***");
        }else{
            sensitiveReplaceMap.put(cscpSensitiveWord.getSenContent(), cscpSensitiveWord.getSenReplace());
        }
    	addSensitiveWordMap(cscpSensitiveWord.getSenContent());
    }
    
    /**
     * 敏感词库删除，构建DFA算法模型
     *
     *
     */
    public static void delete(CscpSensitiveWord cscpSensitiveWord) {
    	sensitiveReplaceMap.remove(cscpSensitiveWord.getSenContent());
    	sensitiveWordMap.clear();
    	initSensitiveWordMap(sensitiveReplaceMap.keySet());
    }
    
    /**
     * 敏感词库批量删除，构建DFA算法模型
     *
     *
     */
    public static void deleteBatch(List <CscpSensitiveWord> list) {
    	for(CscpSensitiveWord cscpSensitiveWord : list) {
    		sensitiveReplaceMap.remove(cscpSensitiveWord.getSenContent());
    	}
    	sensitiveWordMap.clear();
    	initSensitiveWordMap(sensitiveReplaceMap.keySet());
    }
    
    /**
     * 敏感词库更新，构建DFA算法模型
     *
     * @param cscpSensitiveWord 敏感词库
     */
    public static void update(CscpSensitiveWord cscpSensitiveWord) {
        if(null == cscpSensitiveWord.getSenReplace()){
            sensitiveReplaceMap.put(cscpSensitiveWord.getSenContent(), "***");
        }else{
            sensitiveReplaceMap.put(cscpSensitiveWord.getSenContent(), cscpSensitiveWord.getSenReplace());
        }
    	sensitiveWordMap.clear();
    	initSensitiveWordMap(sensitiveReplaceMap.keySet());
    }
    
    
    /**
     * 初始化敏感词库，构建DFA算法模型
     *
     * @param map 敏感词库
     */
    public static synchronized void init(Map <String, String> map) {
    	sensitiveReplaceMap.clear();
    	sensitiveReplaceMap.putAll(map);
        initSensitiveWordMap(sensitiveReplaceMap.keySet());
    }
    
    /**
     * 初始化敏感词库，构建DFA算法模型
     *
     * @param sensitiveWordSet 敏感词库
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        //初始化敏感词容器，减少扩容操作
        sensitiveWordMap = new ConcurrentHashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //迭代sensitiveWordSet
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            //关键字
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);
                //库中获取关键字
                Object wordMap = nowMap.get(keyChar);
                //如果存在该key，直接赋值，用于下一个循环获取
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new ConcurrentHashMap<String,String>();
                    //不是最后一个
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                if (i == key.length() - 1) {
                    //最后一个
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }
    
    /**
     * 新增敏感词库，构建DFA算法模型
     *
     * @param key 敏感词库
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addSensitiveWordMap(String key) {
        Map nowMap = sensitiveWordMap;
        Map<String, String> newWorMap;
        for (int i = 0; i < key.length(); i++) {
            //转换成char型
            char keyChar = key.charAt(i);
            //库中获取关键字
            Object wordMap = nowMap.get(keyChar);
            //如果存在该key，直接赋值，用于下一个循环获取
            if (wordMap != null) {
                nowMap = (Map) wordMap;
            } else {
                //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                newWorMap = new ConcurrentHashMap<String,String>();
                //不是最后一个
                newWorMap.put("isEnd", "0");
                nowMap.put(keyChar, newWorMap);
                nowMap = newWorMap;
            }
            if (i == key.length() - 1) {
                //最后一个
            	nowMap.put("isEnd", "1");
            }
        }
    }
    
    /**
     	* 检查文字中是否包含敏感字符
     */
    @SuppressWarnings("rawtypes")
    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        //敏感词结束标识位：用于敏感词只有1位的情况
        boolean flag = false;
        //匹配标识数默认为0
        int matchFlag = 0;
        char word;
		Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            //获取指定key
            nowMap = (Map) nowMap.get(word);
            //存在，则判断是否为最后一个
            if (nowMap != null) {
                //找到相应key，匹配标识+1
                matchFlag++;
                //如果为最后一个匹配规则,结束循环，返回匹配标识数
                if ("1".equals(nowMap.get("isEnd"))) {
                    //结束标志位为true
                    flag = true;
                    //最小规则，直接返回,最大规则还需继续查找
                    if (TYPE_MIN_MATCH == matchType) {
                        break;
                    }
                }
            } else {//不存在，直接返回
                break;
            }
        }
        //长度必须大于等于1，为词
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }
 
    /**
     	* 判断文字是否包含敏感字符
     */
    public static boolean contains(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int matchFlag = checkSensitiveWord(txt, i, matchType);
            //大于0存在，返回true
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }
 
    /**
     	* 判断文字是否包含敏感字符
     */
    public static boolean contains(String txt) {
        return contains(txt, TYPE_MAX_MATCH);
    }
 
    /**
     	* 获取文字中的敏感词
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();
        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int length = checkSensitiveWord(txt, i, matchType);
            //存在,加入list中
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                //减1的原因，是因为for会自增
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }
 
    /**
     	* 获取文字中的敏感词
     */
    public static Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, TYPE_MAX_MATCH);
    }
 
    /**
     	* 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
    	if(txt == null) {
    		return txt;
    	}
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        return resultTxt;
    }
 
    /**
     	* 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, TYPE_MAX_MATCH);
    }
 
    /**
     	* 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            if(sensitiveReplaceMap != null) {
            	String value = sensitiveReplaceMap.get(word);
            	if(value != null) {
            		replaceStr = value;
            	}
            }
            resultTxt = resultTxt.replaceAll(word, replaceStr);
        }
        return resultTxt;
    }
    
    
    /**
     	* 匹配敏感字字符
     */
    public static Map<String, String> matchSensitiveWord(String txt) {
    	Map <String, String> map = new HashMap<String, String>();
        Set<String> set = getSensitiveWord(txt, TYPE_MAX_MATCH);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            if(sensitiveReplaceMap != null) {
            	String value = sensitiveReplaceMap.get(word);
            	map.put(word, value);
            }
        }
        return map;
    }
 
    /**
     	* 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, String replaceStr) {
        return replaceSensitiveWord(txt, replaceStr, TYPE_MAX_MATCH);
    }
 
    /**
     	* 获取替换字符串
     */
    private static String getReplaceChars(char replaceChar, int length) {
        String resultReplace = String.valueOf(replaceChar);
        StringBuilder stringBuilder = new StringBuilder(resultReplace);
        for (int i = 1; i < length; i++) {
//            resultReplace += replaceChar;
            stringBuilder.append(replaceChar);
        }
//        return resultReplace;
        return stringBuilder.toString();
    }
    
}