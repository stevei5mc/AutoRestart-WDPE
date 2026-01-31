package cn.stevei5mc.wdpe.autorestart.utils.restart;

import lombok.Getter;

/**
 * 重启任务的类型
 */
public enum RestartTaskType {

    /**
     * 重启任务的默认类型，如果没有运行重启任务就为该类型<br>
     * 如果在运行重启任务时填写该类型，重启任务并不会正常的运行！！！
     */
    NO_RESTART_TASK("restart-task-type-noRestartTask", 0),
    /**
     * 类型：自动重启 （倒计时模式）
     * <br>
     * 注：该类型仅在插件启动时使用一次，之后不能再此使用！
     */
    AUTO_CYCLE("restart-task-type-auto", 20),
    /**
     * 类型：自动重启 （定时模式）
     * <br>
     * 注：该类型仅在插件启动时使用一次，之后不能再此使用！
     */
    AUTO_CRON("restart-task-type-auto", 20),
    /**
     * 类型：手动重启
     */
    MANUAL("restart-taskType-manual", 20),
    /**
     * 类型：无人时重启
     */
    NO_PLAYER("restart-task-type-noPlayer", 100);

    @Getter
    private final String languageKey;
    @Getter
    private final int taskTick;


    RestartTaskType(String languageKey, int taskTick) {
        this.languageKey = languageKey;
        this.taskTick = taskTick;
    }
}