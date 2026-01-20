package cn.stevei5mc.wdpe.autorestart.utils;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import cn.stevei5mc.wdpe.autorestart.tasks.RestartTask;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskState;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskType;
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


    public static void runRestartTask(RestartTaskType type, int time, TimeUnit timeUnit) {
        time = BaseUtils.convertTime(time, timeUnit);
        restartTaskState = RestartTaskState.RUNNING;
        restartTaskType = type;
        restartTask = main.getProxy().getScheduler().scheduleRepeating(new RestartTask(time), 20, true);
    }

    public static void cancelRestartTask() {
        getRestartTask().cancel();
        restartTaskState = RestartTaskState.STOP;
        restartTaskType = RestartTaskType.NO_RESTART_TASK;
    }
}