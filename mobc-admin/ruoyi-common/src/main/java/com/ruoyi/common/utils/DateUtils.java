package com.ruoyi.common.utils;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
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
        } catch (ParseException e) {
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
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
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

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /***
     *  获取明天日期
     */
    public static String getTomorrowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, +1);
        String date = sdf.format(calendar.getTime());
        return date;

    }

    /***
     *  获取...天后日期
     */
    public static String getSomeDayLaterDate(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, +day);
        String date = sdf.format(calendar.getTime());
        return date;

    }

    public static String getSomeDayLaterDateByToday(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, +day-1);
        String date = sdf.format(calendar.getTime());
        return date;

    }


    /***
     *  获取...天后时间
     */
    public static String getSomeDayLaterDateTime(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, +day);
        String date = sdf.format(calendar.getTime());
        return date;

    }

    /**
     * 生成订单编号
     *
     * @return
     */
    public static String createOrderId(String transType) {
        SimpleDateFormat dmDate = new SimpleDateFormat("yyyyMMddHHmmss");
        String randata = getRandom(10);
        Date date = new Date();
        String dateran = dmDate.format(date);
        String Xsode = transType + dateran + randata;
        if (Xsode.length() < 24) {
            Xsode = Xsode + 0;
        }
        return Xsode;
    }

    public static String getRandom(int len) {
        Random r = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(r.nextInt(10));
        }
        return rs.toString();
    }

    /**
     * 获取两个日期相隔天数
     *
     * @param firstDay
     * @param secondDay
     * @return
     */
    public static int differenceDay(String firstDay, String secondDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        int i = 0;
        try {
            Date star = dft.parse(firstDay);
            Date endDay = dft.parse(secondDay);
            Date nextDay = star;
            //当明天不在结束时间之前是终止循环
            while (nextDay.before(endDay)) {
                Calendar cld = Calendar.getInstance();
                cld.setTime(star);
                cld.add(Calendar.DATE, 1);
                star = cld.getTime();
                nextDay = star;
                i++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 判断是否小于当前时间
     *
     * @param pastDate
     * @return
     */
    public static boolean isPastDate(Date pastDate) {
        boolean flag = false;
        Date nowDate = new Date();
        try {
            flag = pastDate.before(nowDate);
            if (flag) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 判断日期是不是今天
     *
     * @param str
     * @return
     */
    public static boolean isToday(String str) {
        boolean flag = false;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(str);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date);
            int year1 = c1.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH) + 1;
            int day1 = c1.get(Calendar.DAY_OF_MONTH);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date());
            int year2 = c2.get(Calendar.YEAR);
            int month2 = c2.get(Calendar.MONTH) + 1;
            int day2 = c2.get(Calendar.DAY_OF_MONTH);
            if (year1 == year2 && month1 == month2 && day1 == day2) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static int getProportionRandom(int min, int max,int rete) {
        int currMax = max*rete/100;
        int ax = (int) (min + Math.random() * ((currMax-min)+1));
        return ax;
    }

    public static int getluckyAmtRandom(int min, int max) {
        int ax = (int) (min + Math.random() * ((max-min)+1));
        return ax;
    }


    /**
     * 根据概率生成随机奖品
     */
    public static String getRandomForLottery(Map<String, Double> prizes ) {
        List<Double> ranges = new ArrayList<>();
        double sum = 0.0;
        for (Double probability : prizes.values()) {
            sum += probability;
            ranges.add(sum);
        }
        Random random = new Random();
        double randomNumber = random.nextDouble();
        System.out.println("随机数："+randomNumber);
        String prize = "";

        for (int i = 0; i < ranges.size(); i++) {
            if (randomNumber <= ranges.get(i)) {
                prize = (String) prizes.keySet().toArray()[i];
                break;
            }
        }
        return prize;
    }

    public static void main(String[] args) {

        Map<String, Double> numbers = new HashMap<>();
//        for (ProbabilityEntity entity : probabilityEntityList) {
//            numbers.put(entity.getId(), entity.getProbabilityValue());
//        }
        Map<String, Double> prizes = new HashMap<>();
        prizes.put("奖品A", 0.0);
        prizes.put("奖品B", 0.0);
        prizes.put("奖品C", 0.0);
        prizes.put("奖品E", 0.05);
        prizes.put("奖品F", 0.1);
        prizes.put("奖品G", 0.15);
        prizes.put("奖品H", 0.25);
        prizes.put("奖品I", 0.45);

        System.out.println(getRandomForLottery(prizes));
    }
}
