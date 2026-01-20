package cn.stevei5mc.wdpe.autorestart.tasks;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import cn.stevei5mc.wdpe.autorestart.utils.BaseUtils;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskType;
import cn.stevei5mc.wdpe.autorestart.utils.TaskUtils;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.scheduler.Task;
import dev.waterdog.waterdogpe.utils.types.TextContainer;
import lombok.Getter;

public class RestartTask extends Task {

    private final AutoRestartMain main = AutoRestartMain.getInstance();
    @Getter
    private int restartReminderTime;

    public RestartTask(int restartReminderTime) {
        super();
        this.restartReminderTime = restartReminderTime;
    }

    @Override
    public void onRun(int i) {
        if (TaskUtils.getRestartTaskType() != RestartTaskType.NO_RESTART_TASK ) {
            main.getLogger().info("time=" + restartReminderTime);
            if (!TaskUtils.getRestartTaskType().equals(RestartTaskType.NO_PLAYER)) {
                if (main.getProxy().getPlayers().size() >= 1 && restartReminderTime <= BaseUtils.getRestartTipTime()) {
                    for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                        if (main.getConfig().getBoolean("show.title", true)) {
                            player.sendTitle(replaceTimeValue(main.getLanguage().getString("restart-message-title")), replaceTimeValue(main.getLanguage().getString("restart-message-subtitle")),
                            0, 60, 30);
                        }
                        if (main.getConfig().getBoolean("show.tip", true)) {
                            player.sendTip(replaceTimeValue(main.getLanguage().getString("restart-message-tip")));
                        }
                    }
                }
                if (restartReminderTime <= 0) {
                    if (main.getProxy().getPlayers().size() >= 1 && main.getConfig().getBoolean("kick_player", true)) {
                        for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                            player.disconnect(new TextContainer(main.getLanguage().getString("kickPlayer-message")));
                        }
                    }
                    main.getProxy().shutdown();
                }
                restartReminderTime--;
            }
        }else {
            TaskUtils.cancelRestartTask();
        }
    }

    @Override
    public void onCancel() {

    }

    private String replaceTimeValue(String languageKey) {
        return languageKey.replace("%1%", String.valueOf(restartReminderTime)).replace("%2%", main.getLanguage().getString("time-unit-seconds"));
    }
}