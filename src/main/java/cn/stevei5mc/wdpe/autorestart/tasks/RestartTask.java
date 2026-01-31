package cn.stevei5mc.wdpe.autorestart.tasks;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import cn.stevei5mc.wdpe.autorestart.utils.BaseUtils;
import cn.stevei5mc.wdpe.autorestart.utils.TaskUtils;
import cn.stevei5mc.wdpe.autorestart.utils.TimeUtils;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskType;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.scheduler.Task;
import dev.waterdog.waterdogpe.utils.types.TextContainer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RestartTask extends Task {

    private static final AutoRestartMain main = AutoRestartMain.getInstance();
    private static int restartReminderTime;
    private int broadcastCycle;

    public RestartTask(int time) {
        super();
        restartReminderTime = time;
        this.broadcastCycle = time;
    }

    @Override
    public void onRun(int i) {
        if (TaskUtils.getRestartTaskType() != RestartTaskType.NO_RESTART_TASK ) {
            if (!TaskUtils.getRestartTaskType().equals(RestartTaskType.NO_PLAYER)) {
                if (main.getConfig().getBoolean("broadcast_message.reminder_time.enable", true) && restartReminderTime == broadcastCycle && broadcastCycle > 0) {
                    broadcastCycle = broadcastCycle - TimeUtils.convertTime(main.getConfig().getInt("broadcast_message.reminder_time.cycle", 30), TimeUnit.MINUTES);
                    for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                        player.sendMessage(main.getLanguage().getString("broadcast-restart-reminderTime").replace("%1%", getRestartTime()));
                    }
                }
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
                    runCommand();
                    if (main.getProxy().getPlayers().size() >= 1 && main.getConfig().getBoolean("kick_player", true)) {
                        for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                            player.disconnect(new TextContainer(main.getLanguage().getString("kickPlayer-message")));
                        }
                    }
                    main.getProxy().shutdown();
                }
                restartReminderTime--;
            }else if (main.getProxy().getPlayers().size() == 0) {
                main.getProxy().shutdown();
            }
        }else {
            TaskUtils.cancelRestartTask();
        }
    }

    @Override
    public void onCancel() {}

    private String replaceTimeValue(String languageKey) {
        return languageKey.replace("%1%", String.valueOf(restartReminderTime)).replace("%2%", main.getLanguage().getString("time-unit-seconds"));
    }

    /**
     * 获取剩余时间 （单位：秒）
     * <br>
     * 注：如果需要通过判断剩余时间执行一下操作之类的用途请使用该方法
     * @return 剩余时间
     */
    public static int getRestartReminderTime() {
        return restartReminderTime;
    }

    /**
     * 获取剩余时间<br>该方法用于显示剩余时间给玩家，如果想要对接其他插件使用的话请使用 getRestartReminderTime()
     * @return 剩余时间
     */
    public static StringBuilder getRestartTime() {
        int time = getRestartReminderTime();
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
        StringBuilder timeTxt = new StringBuilder();
        if (hours > 0) {
            timeTxt.append(hours).append(main.getLanguage().getString("time-unit-hour"));
        }
        if(minutes > 0) {
            timeTxt.append(minutes).append(main.getLanguage().getString("time-unit-minutes"));
        }
        timeTxt.append(seconds).append(main.getLanguage().getString("time-unit-seconds"));
        return timeTxt;
    }

    private void runCommand() {
        if (main.getConfig().getBoolean("runCommand", true)) {
            ArrayList<String> globalCommands = new ArrayList<>(main.getConfig().getStringList("commands.global", new ArrayList<>()));
            for (String cmd: globalCommands) {
                String[] s = cmd.split("&");
                main.getProxy().dispatchCommand(main.getProxy().getConsoleSender(), s[0]);
            }
            if(main.getProxy().getPlayers().size() >= 1) {
                ArrayList<String> playerCommands = new ArrayList<>(main.getConfig().getStringList("commands.player", new ArrayList<>()));
                for (ProxiedPlayer player: main.getProxy().getPlayers().values()) {
                    for (String cmd: playerCommands) {
                        String[] s = cmd.split("&");
                        if (s.length > 1 && s[1].equals("con")) {
                            main.getProxy().dispatchCommand(main.getProxy().getConsoleSender(), s[0]);
                        }else {
                            main.getProxy().dispatchCommand(player, s[0].replace("@p", player.getName()));
                        }
                    }
                }
            }
        }
    }
}