package com.example.library.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间工具类
 *
 * @author 冯名豪
 * @date 2022-09-21
 * @since 1.0
 */
@Slf4j
public final class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_V2 = "yyyyMMdd";
    public static final String CN_DATE_FORMAT = "yyyy年MM月dd日";
    public static final String CN_DATE_TIME_FORMAT = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_HM_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_TIME_YM_FORMAT = "yyyy-MM";
    public static final String CORN_FORMAT = "ss mm HH dd MM ? yyyy";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final DateTimeFormatter DATE_FORMATTER_V2 = DateTimeFormatter.ofPattern(DATE_FORMAT_V2);
    public static final DateTimeFormatter CN_DATE_FORMATTER = DateTimeFormatter.ofPattern(CN_DATE_FORMAT);
    public static final DateTimeFormatter CN_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(CN_DATE_TIME_FORMAT);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    public static final DateTimeFormatter DATE_HM_FORMATTER = DateTimeFormatter.ofPattern(DATE_HM_FORMAT);
    public static final DateTimeFormatter DATE_TIME_T_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_T_FORMAT);
    public static final DateTimeFormatter DATE_TIME_YM_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_YM_FORMAT);
    public static final DateTimeFormatter CORN_FORMATTER = DateTimeFormatter.ofPattern(CORN_FORMAT);

    private static final Map<String, DateTimeFormatter> DATE_FORMATTER_MAP = new HashMap<>();
    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTER_MAP = new HashMap<>();

    static {
        DATE_FORMATTER_MAP.put(DATE_FORMAT, DATE_FORMATTER);
        DATE_FORMATTER_MAP.put(DATE_FORMAT_V2, DATE_FORMATTER_V2);

        DATE_TIME_FORMATTER_MAP.put(DATE_TIME_FORMAT, DATE_TIME_FORMATTER);
        DATE_TIME_FORMATTER_MAP.put(DATE_HM_FORMAT, DATE_HM_FORMATTER);
        DATE_TIME_FORMATTER_MAP.put(CN_DATE_TIME_FORMAT, CN_DATE_TIME_FORMATTER);
    }

    private DateUtils() {

    }

    public static OffsetDateTime strToDateTime(String dateTime, String format) {
        DateTimeFormatter formatter = DATE_FORMATTER_MAP.get(format);
        if (formatter != null) {
            return strToDate(dateTime, formatter);
        }
        formatter = DATE_TIME_FORMATTER_MAP.get(format);
        return strToDateTime(dateTime, formatter);
    }

    /**
     * 字符串转时间格式
     *
     * @param dateTime
     * @param formatter
     * @return
     */
    public static OffsetDateTime strToDateTime(String dateTime, DateTimeFormatter formatter) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return OffsetDateTime.of(localDateTime, ZoneId.systemDefault().getRules().getOffset(localDateTime));
    }

    /**
     * 字符串转时间格式
     *
     * @param date
     * @param formatter
     * @return
     */
    public static OffsetDateTime strToDate(String date, DateTimeFormatter formatter) {
        if (!StringUtils.hasText(date)) {
            return null;
        }

        LocalDate parse = LocalDate.parse(date, formatter);
        return ZonedDateTime.of(LocalDateTime.of(parse, LocalTime.MIN), ZoneId.systemDefault()).toOffsetDateTime();
    }

    public static String dateToString(LocalDate date, DateTimeFormatter formatter) {
        return date.format(formatter);
    }

    public static String dateToString(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    /**
     * 日期转字符串
     *
     * @param dateTime
     * @param formatter
     * @return
     */
    public static String dateToString(OffsetDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) {
            return null;
        }
        return dateToString(dateTime.toLocalDateTime(), formatter);
    }

    public static String toCronExpression(OffsetDateTime dateTime) {
        return dateTime.format(CORN_FORMATTER);
    }

    /**
     * Date转OffsetDateTime
     *
     * @param date
     * @return
     */
    public static OffsetDateTime dataToDateTime(Date date) {
        if (date == null) {
            return null;
        }

        Instant instant = date.toInstant();
        return instant.atOffset(ZoneOffset.ofHours(8));
    }

    /**
     * 格式化时间
     *
     * @param dateTime
     * @param patten
     * @return
     */
    public static OffsetDateTime formatDateTime(OffsetDateTime dateTime, String patten) {
        if (dateTime == null) {
            return null;
        }
        if (StringUtils.isEmpty(patten)) {
            return null;
        }

        String timeStr = dateTime.format(DateTimeFormatter.ofPattern(patten));
        return strToDateTime(timeStr, patten);
    }

    /**
     * 时间转时间戳
     *
     * @param time 时间
     * @return 时间戳
     */
    public static Long dateToLong(OffsetDateTime time, String patten) {
        if (time == null) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
        try {
            return simpleDateFormat.parse(DateUtils.dateToString(time, DateTimeFormatter.ofPattern(patten))).getTime();
        } catch (ParseException e) {
            log.warn("转换时间戳失败", e);
            return null;
        }
    }

    /**
     * 时间戳转时间
     *
     * @param time 13位毫秒级时间戳
     * @return 时间
     */
    public static OffsetDateTime toOffsetDateTime(long time) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    /**
     * 时间戳转时间
     *
     * @param time 10位秒级时间戳
     * @return 时间
     */
    public static OffsetDateTime toOffsetDateTimeSec(long time) {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault());
    }

    public static void main(String[] args) throws ParseException {

        /*String s = "2021-04-23T16:37:27";
        OffsetDateTime date = strToDateTime(s, DATE_TIME_T_FORMATTER);
        System.out.println(date);*/   // 2021-04-23T16:37:27+08:00

        String s2 = "1993-11";
        Date date2 = new SimpleDateFormat("yyyy-MM").parse(s2);
        System.out.println(date2);

    }

}
