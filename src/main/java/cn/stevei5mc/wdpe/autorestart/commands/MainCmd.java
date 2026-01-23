package cn.stevei5mc.wdpe.autorestart.commands;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import cn.stevei5mc.wdpe.autorestart.utils.TaskUtils;
import dev.waterdog.waterdogpe.command.Command;
import dev.waterdog.waterdogpe.command.CommandSender;
import dev.waterdog.waterdogpe.command.CommandSettings;
import org.cloudburstmc.protocol.bedrock.data.command.*;

import java.util.*;

public class MainCmd extends Command {
    private final AutoRestartMain main = AutoRestartMain.getInstance();

    public MainCmd() {
        super("autorestart-wdpe", CommandSettings.builder().setDescription(AutoRestartMain.getInstance().getLanguage().getString("command-tip-autoRestart"))
                .setPermission("autorestart.admin").setPermissionMessage(AutoRestartMain.getInstance().getLanguage().getString("command-message-notPermission")).build());
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
                        sender.sendMessage(main.getLanguage().getString("reload-success"));
                    }
                }
                case "pause" -> {
                    if (checkPermission(sender, "autorestart.admin.pause")) {
                        TaskUtils.pauseOrContinueRestartTask();
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
        List<CommandOverloadData> overloadData = new ArrayList<>();
        String[] subCommands = {"cancel", "pause", "reload"};
        for (String subCmd: subCommands) {
            Map<String, Set<CommandEnumConstraint>> map = new LinkedHashMap<>();
            map.put(subCmd.toLowerCase(), EnumSet.of(CommandEnumConstraint.ALLOW_ALIASES));

            CommandParamData baseParam = new CommandParamData();
            baseParam.setName(subCmd.toLowerCase());
            baseParam.setOptional(false);
            baseParam.setType(CommandParam.TEXT);
            baseParam.setEnumData(new CommandEnumData(subCmd.toLowerCase(), map, false));

            LinkedList<CommandParamData> paramData = new LinkedList<>();
            paramData.add(baseParam);

            overloadData.add(new CommandOverloadData(false, paramData.toArray(new CommandParamData[0])));
        }
        return overloadData.toArray(new CommandOverloadData[0]);
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§3===== AutoRestart for WDPE =====");
        sender.sendMessage("§a/autorestart-wdpe cancel " + main.getLanguage().getString("command-help-restartTask-cancel"));
        sender.sendMessage("§a/autorestart-wdpe restart " + main.getLanguage().getString("command-help-restartTask-run"));
        sender.sendMessage("§a/autorestart-wdpe reload " + main.getLanguage().getString("command-help-reload"));
        sender.sendMessage("§a/autorestart-wdpe pause " + main.getLanguage().getString("command-help-restartTask-pause"));
    }

    public boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sender.sendMessage(main.getLanguage().getString("command-message-notPermission"));
        return false;
    }

    private void runRestartTask(){

    }
}