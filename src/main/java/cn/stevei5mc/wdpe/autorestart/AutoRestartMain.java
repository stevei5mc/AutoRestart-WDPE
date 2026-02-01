package cn.stevei5mc.wdpe.autorestart;

import cn.stevei5mc.wdpe.autorestart.commands.MainCmd;
import cn.stevei5mc.wdpe.autorestart.utils.BaseUtils;
import cn.stevei5mc.wdpe.autorestart.utils.TimeUtils;
import cn.stevei5mc.wdpe.autorestart.utils.restart.RestartTaskType;
import cn.stevei5mc.wdpe.autorestart.utils.TaskUtils;
import dev.waterdog.waterdogpe.plugin.Plugin;
import dev.waterdog.waterdogpe.utils.config.Configuration;
import dev.waterdog.waterdogpe.utils.config.YamlConfig;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class AutoRestartMain extends Plugin {

    @Getter
    private static AutoRestartMain instance;
    @Getter
    private Configuration config;
    @Getter
    private Configuration language;


    @Override
    public void onEnable() {
        instance = this;
        saveConfig();
        this.getProxy().getCommandMap().registerCommand(new MainCmd());
        if (config.getBoolean("restart_time.time_mode", true)) {
            TaskUtils.runRestartTask(RestartTaskType.AUTO_CRON, TimeUtils.getRestartCronTime());
        }else {
            TaskUtils.runRestartTask(RestartTaskType.AUTO_CYCLE, BaseUtils.getRestartUseTime(), TimeUnit.MINUTES);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info("已停止运行，感谢你的使用");
    }

    public void saveConfig() {
        this.saveResource("config.yml");
        this.saveResource("language.yml");
        loadConfig();
    }

    @Override
    public void loadConfig() {
        config = new YamlConfig(this.getDataFolder() + "/config.yml");
        language = new YamlConfig(this.getDataFolder() + "/language.yml");
    }

    public String getMessagePrefix() {
        return config.getString("message_prefix", "§l§bAutoRestart §r§7>> ");
    }

    public void sendDebugLog(String message) {
        if (config.getBoolean("debug", false)) {
            this.getLogger().debug(message);
        }
    }
}