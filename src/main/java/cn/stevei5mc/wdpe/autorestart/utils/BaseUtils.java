package cn.stevei5mc.wdpe.autorestart.utils;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;

import java.util.concurrent.TimeUnit;

public class BaseUtils {

    private static final AutoRestartMain main = AutoRestartMain.getInstance();

    /**
     * 获取在重启前的开始提示的时间
     * @return 开始提示的时间
     */
    public static int getRestartTipTime() {
        int time = main.getConfig().getInt("restart_tip_time", 30);
        if (time < 1) {
            time = 30;
        }
        return time;
    }

    /**
     * 获取在重启服务器需要的时间
     * @return 重启服务器需要的时间
     */
    public static int getRestartUseTime() {
        int time  = main.getConfig().getInt("restart_time.cycle_time", 2);
        if (time  < 1) {
            time = 30;
        }
        return time;
    }

    /**
     * 播放音效
     * @param player 目标玩家
     * @param name 音效名
     * @param pitch 音高
     * @param volume 音量
     */
    public static void playSound(ProxiedPlayer player, String name, float pitch, float volume) {
        PlaySoundPacket packet = new PlaySoundPacket();
        packet.setSound(name);
        packet.setPitch(pitch);
        packet.setVolume(volume);
        packet.setPosition(player.getRewriteData().getSpawnPosition());
        player.sendPacketImmediately(packet);
    }

    /**
     *
     * @param timeUnit 时间单位
     * @return 时间单位的语言 key
     */
    public static String getTimeUnitLanguageKey(TimeUnit timeUnit) {
        return switch (timeUnit) {
            case HOURS -> "time-unit-hour";
            case MINUTES -> "time-unit-minutes";
            case SECONDS -> "time-unit-seconds";
            default -> "unknown";
        };
    }
}