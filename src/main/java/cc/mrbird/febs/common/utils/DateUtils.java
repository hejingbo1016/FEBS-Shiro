package cc.mrbird.febs.common.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public static List<String> findDates(String dBegin, String dEnd) throws ParseException, java.text.ParseException {
        //日期工具类准备
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //设置开始时间
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(dBegin));

        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(dEnd));

        //装返回的日期集合容器
        List<String> Datelist = new ArrayList<String>();
        //将第一个月添加里面去
        Datelist.add(format.format(calBegin.getTime()));
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(dEnd).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        return Datelist;
    }


}
