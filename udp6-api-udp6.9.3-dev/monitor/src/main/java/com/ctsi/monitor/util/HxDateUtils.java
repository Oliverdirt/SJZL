package com.ctsi.monitor.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HxDateUtils extends org.apache.commons.lang3.time.DateUtils {
        public static final String YYYY = "yyyy";

        public static final String YYYY_MM = "yyyy-MM";

        public static final String YYYY_MM_DD = "yyyy-MM-dd";

        public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

        private static String[] parsePatterns = {
                "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
                "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
                "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

        /**
         * 获取当前Date型日期
         *
         * @return Date() 当前日期
         */
        public static Date getNowDate()
        {
            return new Date();
        }

        /**
         * 获取当前日期, 默认格式为yyyy-MM-dd
         *
         * @return String
         */
        public static String getDate()
        {
            return dateTimeNow(YYYY_MM_DD);
        }

        public static final String getTime()
        {
            return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
        }

        public static final String dateTimeNow()
        {
            return dateTimeNow(YYYYMMDDHHMMSS);
        }

        public static final String dateTimeNow(final String format)
        {
            return parseDateToStr(format, new Date());
        }

        public static final String dateTime(final Date date)
        {
            return parseDateToStr(YYYY_MM_DD, date);
        }

        public static final String parseDateToStr(final String format, final Date date) {
            return new SimpleDateFormat(format).format(date);
        }

        public static final Date dateTime(final String format, final String ts) {
            try {
                return new SimpleDateFormat(format).parse(ts);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 日期路径 即年/月/日 如2018/08/08
         */
        public static final String datePath() {
            Date now = new Date();
            return DateFormatUtils.format(now, "yyyy/MM/dd");
        }

        /**
         * 日期路径 即年/月/日 如20180808
         */
        public static final String dateTime() {
            Date now = new Date();
            return DateFormatUtils.format(now, "yyyyMMdd");
        }

        /**
         * 日期型字符串转化为日期 格式
         */
        public static Date parseDate(Object str) {
            if (str == null) {
                return null;
            }
            try {
                return parseDate(str.toString(), parsePatterns);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 获取服务器启动时间
         */
        public static Date getServerStartDate() {
            long time = ManagementFactory.getRuntimeMXBean().getStartTime();
            return new Date(time);
        }

        /**
         * 计算两个时间差
         */
        public static String getDatePoor(Date endDate, Date nowDate) {
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = endDate.getTime() - nowDate.getTime();
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            // long sec = diff % nd % nh % nm / ns;
            return day + "天" + hour + "小时" + min + "分钟";
        }

        //获取近七天日期
        public static List<String> getSevenDate() {
            List<String> dateList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < 7; i++) {
                Date date = HxDateUtils.addDays(new Date(), -i);
                String formatDate = sdf.format(date);
                dateList.add(formatDate);
            }
            return dateList;
        }

        //获取近六月日期
        public static List<String> getLastSixMonthDate() {
            List<String> dateList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 6; i++) {
                Date date = HxDateUtils.addMonths(new Date(), -i);
                String formatDate = sdf.format(date);
                dateList.add(formatDate);
            }
            return dateList;
        }

        /**
         * @Author chy
         * @Description 获取近六个月的月份数据
         * @Date
         * @Param
         * @return
         **/
        public static List getLastSixMonthData(int year,int month){
            List resultList = new ArrayList();
            Calendar cal = Calendar.getInstance();
            //要先+1,才能把本月的算进去
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);

            for(int i=0; i<6; i++) {
                //逐次往前推1个月
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
                resultList.add(String.valueOf(cal.get(Calendar.YEAR)) + (cal.get(Calendar.MONTH) + 1 < 10 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)));
            }
            return resultList;
        }

        //获取近六年日期
        public static List<String> getLastSixYearDate() {
            List<String> dateList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            for (int i = 0; i < 6; i++) {
                Date date = HxDateUtils.addYears(new Date(), -i);
                String formatDate = sdf.format(date);
                dateList.add(formatDate);
            }
            return dateList;
        }
    }
