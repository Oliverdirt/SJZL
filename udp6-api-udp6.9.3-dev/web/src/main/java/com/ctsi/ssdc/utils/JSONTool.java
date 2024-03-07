package com.ctsi.ssdc.utils;

/**
 * @author Len
 * @describe json数据格式化为json格式文本
 * @Date 2022/8/17 16:29
 */
public class JSONTool {

    private JSONTool() {

    }

    private static boolean isTab = true;

    /**
     *
     * @param strJson
     * @return
     */
    public static String stringToJSON(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuilder jsonFormat = new StringBuilder();
        int length = strJson.length();

        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else {
                jsonFormat.append(c);
            }
        }
        return jsonFormat.toString();
    }

    /**
     *
     * @param tabNum
     * @return
     */
    public static String getSpaceOrTab(int tabNum) {
        StringBuilder sbTab = new StringBuilder();
        for (int i = 0; i < tabNum; i++) {
            if (isTab) {
                sbTab.append('\t');
            } else {
                sbTab.append("    ");
            }
        }
        return sbTab.toString();
    }
}