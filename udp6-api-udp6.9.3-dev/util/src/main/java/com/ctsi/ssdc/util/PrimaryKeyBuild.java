package com.ctsi.ssdc.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主键生成工具类
 * @author ctsi
 *
 */
public class PrimaryKeyBuild {
    private static Integer loopNum = Integer.valueOf("0");

    private static final int MAX_LOOPNUM = 999;

    private static final int MAX_REQNUM = 1000;

    private static int LEFT_MOVE_BIT = 1000;

    private static long ID_BAS = 100000000000000L;

    /**
     * 最大限制数量100
     * @param reqNum
     * @return
     */
    

    public static long[] createId(int reqNum) {
        int count = 0;
        try {
            count = reqNum;
        } catch (Exception e) {
            System.out.print("生成id的个数参数有问题，变量值为：" + reqNum);
            e.printStackTrace();
        }
        int result = count / MAX_REQNUM;
        int remainder = count % MAX_REQNUM;
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < result; i++) {
            list.addAll(createIdAtMostOneHundred(1000));
        }
        list.addAll(createIdAtMostOneHundred(remainder));

        Collections.sort(list, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                long poor = o1.longValue() - o2.longValue();
                return (int)poor;
            }
        });

        long[] ids = new long[count];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).longValue();
        }
        return ids;
    }

    /***
     * 最多生成100个ID
     * @param reqNum
     * @return
     */
    private static List<Long> createIdAtMostOneHundred(int reqNum) {
        List<Long> list = new ArrayList<Long>();
        if (reqNum > 0) {
            int runNum = 1;
            if (reqNum > MAX_REQNUM) {
                if (reqNum % 100 == 0) {
                    runNum = reqNum / MAX_REQNUM;
                } else {
                    runNum = reqNum / MAX_REQNUM + 1;
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int tempnum = MAX_REQNUM < reqNum ? MAX_REQNUM : reqNum;
            int count = 0;
            int temploopNum = loopNum.intValue();
            AtomicInteger atomicTempNum = new AtomicInteger(temploopNum);
            for (int j = 0; j < runNum; j++) {
                for (int i = 0; i < tempnum; i++) {
                    if (atomicTempNum.get() > MAX_LOOPNUM) {
                        atomicTempNum.set(1);
                    } else {
                        atomicTempNum.getAndIncrement();
                    }
                    temploopNum = atomicTempNum.get();
                    loopNum = Integer.valueOf(String.valueOf(temploopNum));
                    Date now = new Date();
                    long time = now.getTime();
                    String timeStr = String.valueOf(time);
                    timeStr = timeStr.substring(1, timeStr.length());
                    list.add(Long.parseLong(timeStr) * LEFT_MOVE_BIT + ID_BAS
                            + loopNum.longValue());
                    count++;

                }
                tempnum = reqNum - count;
                tempnum = MAX_REQNUM < tempnum ? MAX_REQNUM : tempnum;
            }
        }
        return list;
    }


    /**
     * 生成ID
     * @return
     */
    

    public static long createId() {
        int count = 1;
        int result = count / MAX_REQNUM;
        int remainder = count % MAX_REQNUM;
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < result; i++) {
            list.addAll(createIdAtMostOneHundred(1000));
        }
        list.addAll(createIdAtMostOneHundred(remainder));

        Collections.sort(list, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                long poor = o1.longValue() - o2.longValue();
                return (int)poor;
            }
        });

        return list.get(0).longValue();
    }
    
    

    public static String randomFileNameWithDoubleLongNum(){
        Random random = new Random();
        String name = String.valueOf( random.nextLong() ) +"_" +String.valueOf( random.nextLong() );
        return name;
    }
}

