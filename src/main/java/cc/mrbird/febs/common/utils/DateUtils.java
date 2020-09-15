package cc.mrbird.febs.common.utils;

import org.springframework.util.StringUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        if (!StringUtils.isEmpty(date)){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(date);
        }else {
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


}
