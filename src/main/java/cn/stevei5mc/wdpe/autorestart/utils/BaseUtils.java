package cn.stevei5mc.wdpe.autorestart.utils;

import cn.stevei5mc.wdpe.autorestart.AutoRestartMain;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;

import java.util.concurrent.TimeUnit;

public class BaseUtils {

    private static final AutoRestartMain main = AutoRestartMain.getInstance();

    /**
     * 将时间转换成秒
     * @param time 需要转换的时间
     * @param unit 原始时间单位（ 时 = HOURS 、分 = MINUTES 、秒 = SECONDS ）
     * @return 转换后的时间（单位：秒）
     */
    public static int convertTime(int time, TimeUnit unit) {
        return switch (unit) {
            case HOURS -> time * 3600;
            case MINUTES -> time * 60;
            default -> time;
        };
    }

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
        int time  = main.getConfig().getInt("restart_time", 2);
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
}