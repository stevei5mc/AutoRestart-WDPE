package cn.stevei5mc.wdpe.autorestart;

import cn.stevei5mc.wdpe.autorestart.commands.MainCmd;
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
        TaskUtils.runRestartTask(RestartTaskType.AUTO, config.getInt("restart_time", 180), TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {

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
}