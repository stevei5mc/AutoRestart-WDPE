package cn.stevei5mc.wdpe.autorestart.commands;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import cn.stevei5mc.wdpe.autorestart.utils.TaskUtils;
import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOverloadData;

public class MainCmd extends Command {
    private final AutoRestartMain main = AutoRestartMain.getInstance();

    public MainCmd() {
        super("autorestart-wdpe", CommandSettings.builder().setDescription("AutoRestart 插件主命令").setPermission("autorestart.admin").
                setUsageMessage("autorestart-wdpe help").setPermissionMessage("§c你没有权限使用该命令！").build());
    }


    @Override
    public boolean onExecute(CommandSender sender, String s, String[] strings) {
        if (strings.length >= 1) {
            String subCmd = strings[0];
            switch (subCmd) {
                case "cancel" -> {
                    if (checkPermission(sender, "autorestart.admin.cancel")) {
                        TaskUtils.cancelRestartTask();
                    }
                }
                case "restart" -> runRestartTask();
                case "reload" -> {
                    if (checkPermission(sender, "autorestart.admin.reload")) {
                        main.loadConfig();
                        sender.sendMessage("配置文件已重载！");
                    }
                }
                default -> sendHelp(sender);
            }
        }else {
            sendHelp(sender);
        }
        return true;
    }

    @Override
    protected CommandOverloadData[] buildCommandOverloads() {
        return super.buildCommandOverloads();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§3===== AutoRestart for WDPE =====");
        sender.sendMessage("§a/autorestart-wdpe cancel §e取消重启任务");
        sender.sendMessage("§a/autorestart-wdpe restart §e运行重启任务");
        sender.sendMessage("§a/autorestart-wdpe reload §e重载配置文件");
    }

    public boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sender.sendMessage("§c你没有权限使用该命令！");
        return false;
    }

    private void runRestartTask(){

    }
}