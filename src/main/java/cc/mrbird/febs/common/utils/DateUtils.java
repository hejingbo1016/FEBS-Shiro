package cc.mrbird.febs.common.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    /**
     * 获取当前时间 yyyy-MM-dd
     *
     * @param currentTime
     */
    public static Date getNowDateShort(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(dateString, pos);
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }


    /**
     * 获取当前时间 yyyy-MM-dd
     *
     * @param time
     */
    public static Date getTimeDate(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String dateString = formatter.format(time);
        ParsePosition pos;
        pos = new ParsePosition(0);
        return formatter.parse(dateString, pos);
    }


    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getDateString() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return formatter.format(currentTime);
    }


    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDates(Date date) {
        if (!StringUtils.isEmpty(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);
        } else {
            return null;
        }

    }


    /**
     * 两个date时间相减获取时间差小时
     *
     * @param endTime
     * @param startTime
     */
    public static Long getTimeHour(Date endTime, Date startTime) {
        Long hour = null;
        if (!StringUtils.isEmpty(endTime) && !StringUtils.isEmpty(startTime)) {
            hour = (endTime.getTime() - startTime.getTime()) / 60 * 60 * 1000;
        }
        return hour;
    }


    /**
     * 获取结束时间 yyyy-MM-dd 23:59:59
     *
     * @param currentTime
     */
    public static Date getEndsTime(Date currentTime) {
        if (!StringUtils.isEmpty(currentTime)) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(currentTime);
            calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
            Date endOfDate = calendar2.getTime();
            return endOfDate;
        } else {
            return null;
        }

    }


    /**
     * 根据时间范围获取该范围内的所有日期集
     *
     * @param dBegin
     * @param dEnd
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public static Set<String> findDates(String dBegin, String dEnd) throws ParseException, java.text.ParseException {
        //日期工具类准备
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //设置开始时间
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(dBegin));

        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(dEnd));

        //装返回的日期集合容器
        Set<String> Datelist = new HashSet<String>();
        //将第一个月添加里面去
        Datelist.add(format.format(calBegin.getTime()));
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(dEnd).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        Datelist.add(format.format(calEnd.getTime()));

        return Datelist;
    }


    /**
     * 根据初始日期推算期望（向前/向后）日期
     *
     * @param initTime 初始日期（initTime可以是null、Date、String数据类型）
     * @param n        向前/向后推算n天（n可以是正整数、0、负整数）
     * @return 推算后的日期字符串
     */
    public static String getCalculateDay(Object initTime, int n) {
        // 返回推算后的日期
        String calculateDay = "";
        try {
            // 实例化日历类Calendar
            Calendar calendar = Calendar.getInstance();
            // 定义日期格式化样式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 初始日期
            Date initDate = null;
            // 判断参数类型
            if (null == initTime) {
                // 取系统当前时间
                initDate = new Date();
            } else if (initTime instanceof Date) {
                initDate = (Date) initTime;
            } else {
                // 日期格式字符串转换成日期类Date
                initDate = sdf.parse((String) initTime);
            }

            // 设置日历时间
            calendar.setTime(initDate);
            // 设置推算后的日历时间
            calendar.add(Calendar.DATE, n);
            // 获取推算后的日期
            Date calculateDate = calendar.getTime();
            // 进行格式化
            calculateDay = sdf.format(calculateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calculateDay;
    }


    /**
     * 根据时间字符串转换成Date类型
     *
     * @param s
     * @return
     */
    public static Date getDateByString(String s) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
