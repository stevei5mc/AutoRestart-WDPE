package cn.stevei5mc.wdpe.autorestart.utils;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import cn.stevei5mc.wdpe.autorestart.tasks.RestartTask;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskState;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskType;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.scheduler.TaskHandler;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class TaskUtils {

    private static final AutoRestartMain main = AutoRestartMain.getInstance();
    @Getter
    private static TaskHandler<RestartTask> restartTask;
    @Getter
    private static RestartTaskState restartTaskState = RestartTaskState.STOP;
    @Getter
    private static RestartTaskType restartTaskType = RestartTaskType.NO_RESTART_TASK;


    /**
     * 运行重启任务
     * @param type 任务类型
     */
    public static void runRestartTask(RestartTaskType type) {
        runRestartTask(type, 30, TimeUnit.SECONDS);
    }

    /**
     * 运行重启任务
     * @param type 任务类型
     * @param time 定时时间
     * @param timeUnit 时间单位
     */
    public static void runRestartTask(RestartTaskType type, int time, TimeUnit timeUnit) {
        if (!getRestartTaskState().equals(RestartTaskState.STOP)) {
            cancelRestartTask();
        }
        time = BaseUtils.convertTime(time, timeUnit);
        restartTaskState = RestartTaskState.RUNNING;
        restartTaskType = type;
        restartTask = main.getProxy().getScheduler().scheduleRepeating(new RestartTask(time), type.getTaskTick(), true);
        for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
            player.sendMessage(main.getLanguage().getString(""));
        }
    }

    /**
     * 取消( 停止 )重启任务
     */
    public static void cancelRestartTask() {
        getRestartTask().cancel();
        restartTaskState = RestartTaskState.STOP;
        restartTaskType = RestartTaskType.NO_RESTART_TASK;
        for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
            player.sendMessage(main.getLanguage().getString("restart-taskState-cancel"));
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
                    player.sendMessage(main.getLanguage().getString("restart-message-taskContinue"));
                }
            }else {
                restartTask.cancel();
                restartTaskState = RestartTaskState.PAUSE;
                for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                    player.sendMessage(main.getLanguage().getString("restart-message-taskPause"));
                }
            }
        }
    }
}