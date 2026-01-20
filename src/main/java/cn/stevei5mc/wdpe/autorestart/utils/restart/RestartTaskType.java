package cn.stevei5mc.wdpe.autorestart.utils.restart;

import lombok.Getter;

public enum RestartTaskType {

    NO_RESTART_TASK("restart-task-type-noRestartTask"),
    AUTO("restart-task-type-auto"),
    NO_PLAYER("restart-task-type-noPlayer");

    @Getter
    private final String languageKey;


    RestartTaskType(String languageKey) {
        this.languageKey = languageKey;
    }
}