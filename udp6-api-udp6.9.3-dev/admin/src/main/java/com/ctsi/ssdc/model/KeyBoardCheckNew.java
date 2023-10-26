package com.ctsi.ssdc.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 键盘横向规则检测
 * 密码不得包含键盘上横向或者纵向任意连续的四个字符或shift转换字符
 * @author MyBatis Generator
 */
@Component
public class KeyBoardCheckNew implements IPasswordCheck {

    //定义横向穷举
    private static final String[][] KEY_CODE = {
            {"`~·", "1！!", "2@@", "3#", "4$￥", "5%", "6^……", "7&", "8*", "9(（", "0）)", "-_", "=+"},
            {" ","qQ", "wW", "eE", "rR", "tT", "yY", "uU", "iI", "oO", "pP", "[{【", "]}】", "\\|、"},
            {" ","aA", "sS", "dD", "fF", "gG", "hH", "jJ", "kK", "lL", ";:", "\'\"’“"},
            {" ","zZ", "xX", "cC", "vV", "bB", "nN", "mM", ",《<", ".>》", "/?？"}
    };

    /**
     * 满足键盘排序的字符个数
     */
    public static final int LIMIT_NUM = 2;

    @Value("${ctsi.password-check.check-keyboard: true}")
    private boolean checkKeyBoard;


    @Override
    public CheckResult check(String str,CheckResult checkResult) throws Exception {
        if(!checkKeyBoard){
            return checkResult;
        }

        //找出给出的字符串，每个字符，在坐标系中的位置。
        char[] c = str.toCharArray();
        List<Integer> x = new ArrayList<Integer>();
        List<Integer> y = new ArrayList<Integer>();
        for (int i = 0; i < c.length; i++) {
            char temp = c[i];
            toHere:
            for (int j = 0; j < KEY_CODE.length; j++) {
                for (int k = 0; k < KEY_CODE[j].length; k++) {
                    String jk = KEY_CODE[j][k];
                    if (jk.contains(String.valueOf(temp))) {
                        x.add(j);
                        y.add(k);
                        break toHere;
                    }
                }
            }
        }
        boolean flag = false;
        for (int i = 0; i < x.size() - 3; i++) {
            // 如果X一致，那么就是在一排
            //四者在同一行上
            if (x.get(i) .equals(x.get(i + 1))  && x.get(i + 1).equals(x.get(i + 2))
                    && x.get(i + 2).equals(x.get(i + 3))) {
                if (y.get(i) > y.get(i + 3)) {
                    if (y.get(i) - 1 == y.get(i + 1) && y.get(i) - 2 == y.get(i + 2) && y.get(i) - 3 == y.get(i + 3)) {
                        flag = true;
                        break;
                    }
                } else {
                    if (y.get(i) + 1 == y.get(i + 1) && y.get(i) + 2 == y.get(i + 2) && y.get(i) + 3 == y.get(i + 3)) {
                        flag = true;
                        break;
                    }
                }

            } else if (!x.get(i).equals(x.get(i + 1))
                    && !x.get(i + 1).equals( x.get(i + 2))
                    && !x.get(i).equals(x.get(i + 2))
                    && !x.get(i).equals(x.get(i + 3))
                    && !x.get(i + 1).equals(x.get(i + 3))
                    && !x.get(i + 2).equals(x.get(i + 3))){


                //四者均不在同一行上,但是如果y相同，说明是一列
                if (y.get(i).equals(y.get(i + 1))  && y.get(i + 1).equals(y.get(i + 2))
                        && y.get(i+ 2).equals(y.get(i + 3)) ) {
                    flag = true;
                    break;
                }
                if (y.get(i).equals(y.get(i + 1)+1)  && y.get(i + 1).equals(y.get(i + 2)+1)
                        && y.get(i+ 2).equals(y.get(i + 3)+1) ) {
                    flag = true;
                    break;
                }
            }

        }
        if(flag){
            checkResult.errorReason.add("密码不应包含键盘上横向或者纵向任意连续的四个字符或shift转换字符");
        }
        return checkResult;

    }
}
