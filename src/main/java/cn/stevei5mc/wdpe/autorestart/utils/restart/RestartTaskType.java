package cn.stevei5mc.wdpe.autorestart.utils.restart;

import lombok.Getter;

/**
 * 重启任务的类型
 */
public enum RestartTaskType {

    /**
     * 重启任务的默认类型，如果没有运行重启任务就为该类型
     */
    NO_RESTART_TASK("restart-task-type-noRestartTask", 0),
    /**
     * 类型：自动重启
     */
    AUTO("restart-task-type-auto", 20),
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