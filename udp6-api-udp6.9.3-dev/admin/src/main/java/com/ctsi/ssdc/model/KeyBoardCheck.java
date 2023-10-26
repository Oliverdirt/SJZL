package com.ctsi.ssdc.model;//package com.ctsi.ssdc.model;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//
///**
// * 键盘横向规则检测
// * @author MyBatis Generator
// */
//@Component
//public class KeyBoardCheck implements IPasswordCheck {
//    public static String[] KEYBOARD_HORIZONTAL_ARR = {
//            "01234567890-=",
//            "!@#$%^&*()_+",
//            "qwertyuiop[]",
//            "QWERTYUIOP{}",
//            "asdfghjkl;'",
//            "ASDFGHJKL:",
//            "zxcvbnm,./",
//            "ZXCVBNM<>?",};
//
//
//    /**
//     * 满足键盘排序的字符个数
//     */
//    public static final int LIMIT_NUM = 2;
//
//    @Value("${ctsi.password-check.check-keyboard: true}")
//    private boolean checkKeyBoard;
//
//
//    @Override
//    public boolean check(String password) throws Exception {
//        if(!checkKeyBoard){
//            return true;
//        }
//
//
//        //将所有输入字符转为小写
//        String pwd = password.toLowerCase();
//        int n = pwd.length();
//
//        boolean flag = true;
//        int arrLen = KEYBOARD_HORIZONTAL_ARR.length;
//        int limitNum = LIMIT_NUM ;
//
//        for(int i=0; i+limitNum<=n; i++) {
//            String str = pwd.substring(i, i+limitNum);
//
//            for(int j=0; j<arrLen; j++) {
//                String configStr = KEYBOARD_HORIZONTAL_ARR[j];
//                String revOrderStr = new StringBuffer(configStr).reverse().toString();
//
//                if(configStr.indexOf(str) != -1) {
//                    throw new Exception("密码包含键盘排序");
//                }
//                //考虑逆序输入情况下 连续输入
//                if(revOrderStr.indexOf(str) != -1) {
//                    throw new Exception("密码包含键盘排序");
//                }
//
//
//            }
//        }
//        return flag;
//
//
//    }
//}
