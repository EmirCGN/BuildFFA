package de.emir.utils.cycle;

import de.emir.main.BuildFFA;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Actionbar {
    private String timeFormat = BuildFFA.instance.getConfig().getString("Actionbar.timeformat").replaceAll("&", "§");

    private Kit kit = new Kit(BuildFFA.instance);

    private String s = BuildFFA.instance.getConfig().getString("Actionbar.format");

    private String getRemainingTime(int i) {
        String s = "";
        int inte = i * 1000;
        if (i > 0) {
            int sec = 0;
            int min = 0;
            while (inte >= 1000) {
                inte -= 1000;
                sec++;
            }
            while (sec >= 60) {
                sec -= 60;
                min++;
            }
            if (sec >= 10) {
                s = this.timeFormat.replace("%MIN%", min + "").replace("%SEC%", sec + "");
            } else {
                s = this.timeFormat.replace("%MIN%", min + "").replace("%SEC%", "0" + sec + "");
            }
        }
        return s;
    }

    public void sendActionBar(Player p, String msg) {
        CraftPlayer cp = (CraftPlayer)p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(cbc, (byte)2);
        (cp.getHandle()).playerConnection.sendPacket((Packet)packet);
    }

    public void sendActionBarBroadCaster(int time) {
        String timeValue = getRemainingTime(time);
        String kitValue = this.kit.getActualKitName();
        for (Player all : Bukkit.getOnlinePlayers())
            sendActionBar(all, this.s.replace("%TIME%", timeValue).replace("%KIT%", kitValue));
    }
}