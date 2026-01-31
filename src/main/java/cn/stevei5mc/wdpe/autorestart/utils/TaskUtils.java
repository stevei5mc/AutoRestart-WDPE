package cn.stevei5mc.wdpe.autorestart.utils;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import cn.stevei5mc.wdpe.autorestart.tasks.RestartTask;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskState;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskType;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.scheduler.TaskHandler;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TaskUtils {

    private static final AutoRestartMain main = AutoRestartMain.getInstance();
    @Getter
    private static TaskHandler<RestartTask> restartTask;
    @Getter
    private static RestartTaskState restartTaskState = RestartTaskState.STOP;
    @Getter
    private static RestartTaskType restartTaskType = RestartTaskType.NO_RESTART_TASK;
    private static boolean autoTaskState = false;
    private static ZonedDateTime targetRestartTime = null;


    /**
     * 运行重启任务
     * @param taskType 任务类型
     */
    public static void runRestartTask(RestartTaskType taskType) {
        runRestartTask(taskType, 30, TimeUnit.SECONDS);
    }

    /**
     * 运行重启任务
     * @param taskType 任务类型
     * @param restartTime 目标重启时间
     */
    public static void runRestartTask(RestartTaskType taskType, ZonedDateTime restartTime) {
        ZonedDateTime currentTime = ZonedDateTime.now();
        long time = ChronoUnit.SECONDS.between(currentTime, restartTime);
        targetRestartTime = restartTime;
        runRestartTask(taskType, (int) time, TimeUnit.SECONDS);
    }
    /**
     * 运行重启任务
     * @param taskType 任务类型
     * @param time 定时时间
     * @param timeUnit 时间单位
     */
    public static void runRestartTask(RestartTaskType taskType, int time, TimeUnit timeUnit) {
        boolean autoTask = taskType.equals(RestartTaskType.AUTO_CYCLE) || taskType.equals(RestartTaskType.AUTO_CRON);
        if (taskType.equals(RestartTaskType.NO_RESTART_TASK)) {
            main.getLogger().error("§c重启任务类型不能为：§eNO_RESTART_TASK§c，该类型是没有在没有运行重启任务时使用的！", new IllegalArgumentException());
        }else if (autoTask && autoTaskState) {
            String msg = String.format("§c重启任务类型不能为：§e%s§c ，该类型仅由插件启动时调用一次！", taskType);
            main.getLogger().error(msg, new IllegalArgumentException());
        }else {
            if (!getRestartTaskState().equals(RestartTaskState.STOP)) {
                cancelRestartTask();
            }
            if(autoTask) {
                autoTaskState = true;
            }
            time = TimeUtils.convertTime(time, timeUnit);
            restartTaskState = RestartTaskState.RUNNING;
            restartTaskType = taskType;
            restartTask = main.getProxy().getScheduler().scheduleRepeating(new RestartTask(time), taskType.getTaskTick(), true);
            if (taskType.equals(RestartTaskType.AUTO_CRON)) {
                main.getLogger().info(main.getLanguage().getString("restart-taskState-restartCron").replace("%1%", targetRestartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            }else {
                main.getLogger().info(main.getLanguage().getString("restart-taskState-restartCycle")
                        .replace("%1%", String.valueOf(time)).replace("%2%", main.getLanguage().getString(BaseUtils.getTimeUnitLanguageKey(timeUnit))));
            }
            if (!autoTask) {
                for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                    player.sendMessage(main.getMessagePrefix() + main.getLanguage().getString("restart-taskState-restartCycle")
                            .replace("%1%", String.valueOf(time)).replace("%2%", main.getLanguage().getString(BaseUtils.getTimeUnitLanguageKey(timeUnit))));
                }
            }
        }
    }

    /**
     * 取消重启任务
     */
    public static void cancelRestartTask() {
        getRestartTask().cancel();
        restartTaskState = RestartTaskState.STOP;
        restartTaskType = RestartTaskType.NO_RESTART_TASK;
        for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
            player.sendMessage(main.getMessagePrefix() + main.getLanguage().getString("restart-taskState-cancel"));
        }
    }

    /**
     * 暂停或继续运行重启任务
     */
    public static void pauseOrContinueRestartTask() {
        if (!getRestartTaskState().equals(RestartTaskState.STOP)) {
            if (getRestartTaskState().equals(RestartTaskState.PAUSE)) {
                restartTask = main.getProxy().getScheduler().scheduleRepeating(new RestartTask(RestartTask.getRestartReminderTime()), getRestartTaskType().getTaskTick(), true);
                restartTaskState = RestartTaskState.RUNNING;
                for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                    player.sendMessage(main.getMessagePrefix() + main.getLanguage().getString("restart-message-taskContinue"));
                }
            }else {
                restartTask.cancel();
                restartTaskState = RestartTaskState.PAUSE;
                for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                    player.sendMessage(main.getMessagePrefix() + main.getLanguage().getString("restart-message-taskPause"));
                }
            }
        }
    }
}