package com.zzyy.crm.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    /**
     * 时间格式常量
     */
    public static final String COMMON_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String COMMON_PATTERN_TYPE2 = "yyyy/MM/dd HH:mm:ss";
    public static final String SHORT_PATTERN = "yyyy-MM-dd";
    public static final String SHORT_PATTERN_TYPE2 = "yyyy/MM/dd";
    public static final String LONG_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String SUP_SHORT_PATTERN = "yyyyMMdd";
    public static final String SUP_LONG_PATTERN = "yyyyMMddHHmmss";
    public static final String YEAR_MONTH = "yyyyMM";
    public static final String CN_SHORT_PATTERN = "yyyy年MM月dd日";
    public static final String DDMM_PATTERN = "ddMM";


    /**
     * 获取日期
     *
     * @param time
     * @return
     */
    public static String formatDate(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_PATTERN);
        return sdf.format(time);
    }

    /**
     * 获取时间
     *
     * @param time
     * @return
     */
    public static String formatDateTime(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat(COMMON_PATTERN);
        return sdf.format(time);
    }


    /**
     * 获取日期
     *
     * @param time
     * @return
     */
    public static String format(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static Date parseShortDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_PATTERN);
        return sdf.parse(dateStr);
    }

}
