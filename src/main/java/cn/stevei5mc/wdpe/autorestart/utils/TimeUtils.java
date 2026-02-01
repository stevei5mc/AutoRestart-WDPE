package cn.stevei5mc.wdpe.autorestart.utils;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    private static final AutoRestartMain main = AutoRestartMain.getInstance();

    /**
     * 将时间转换成秒
     * @param time 需要转换的时间
     * @param unit 原始时间单位（ 时 = HOURS 、分 = MINUTES 、秒 = SECONDS ）
     * @return 转换后的时间（单位：秒）
     */
    public static int convertTime(int time, TimeUnit unit) {
        return switch (unit) {
            case HOURS -> time * 3600;
            case MINUTES -> time * 60;
            default -> time;
        };
    }

    /**
     * 获取最近的重启时间段
     * @return 目标重启时段
     */
    public static ZonedDateTime getRestartCronTime() {
        ArrayList<String[]> timeList = new ArrayList<>();

        // 检查时间的范围是否正常
        for (String time: main.getConfig().getStringList("restart_time.cron_time")) {
            String[] timeKey = time.split("&");
            int hour = Integer.parseInt(timeKey[0]);
            int min = Integer.parseInt(timeKey[1]);
            if (hour < 0 || hour > 23 || min < 0 || min > 59) {
                main.getLogger().warn("设定的重启时间不在范围：" + time);
                continue;
            }
            if (timeKey.length >=3) {
                int weekDay = Integer.parseInt(timeKey[2]);
                if (weekDay < 0 || weekDay > 8) {
                    main.getLogger().warn("星期超出范围，范围(1-7)，输入的范围为： " + weekDay);
                    continue;
                }
            }
            timeList.add(timeKey);
        }

        // 判断重启时间列表是否为空，如果为空就返回 null
        if (timeList.isEmpty()) {
            return null;
        }

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime restartTime = null;
        long minDiff = Long.MAX_VALUE;

        // 寻找最近的时间点
        for (String[] timeKey: timeList) {
            ZonedDateTime nextTime;
            ZonedDateTime targetTime = now.withHour(Integer.parseInt(timeKey[0])).withMinute(Integer.parseInt(timeKey[1])).withSecond(0).withNano(0);

            if (timeKey.length >= 3) {
                DayOfWeek week = DayOfWeek.of(Integer.parseInt(timeKey[2]));
                ZonedDateTime result = targetTime.with(week);
                if (result.isBefore(now) || result.isEqual(now)) {
                    result = result.plusWeeks(1);
                }
                nextTime = result;
            }else {
                if (targetTime.isBefore(now) || targetTime.isEqual(now)) {
                    targetTime = targetTime.plusDays(1);
                }
                nextTime = targetTime;
            }

            long diff = ChronoUnit.SECONDS.between(now, nextTime);

            if (diff < 0) {
                continue;
            }else if (diff < minDiff) {
                minDiff = diff;
                restartTime = nextTime;
            }

            main.sendDebugLog("Time diff=" + diff);
            main.sendDebugLog("Target time=" + nextTime);
        }
        return restartTime;
    }
}
