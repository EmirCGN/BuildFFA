package de.emir.utils.nick;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.sql.MySQLEnum;
import de.emir.utils.DataSaver;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.emir.utils.scoreboard.CachePlayer;
import de.emir.utils.scoreboard.CoreScoreBoard;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Nick {
    private BuildFFACore core;

    private DataSaver ds_nicknames;

    private DataSaver ds_nicksettings;

    public static Nick instance;

    public static Field name1;

    public Nick() {
        instance = this;
        this.core = BuildFFACore.instance;
        this.ds_nicknames = new DataSaver(NickData.getObject_nicknames);
        this.ds_nicksettings = new DataSaver(NickData.getObject_nicksettings);
        makeList();
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.hasPermission(BuildFFA.getPremissionPrefix + ".nick") &&
                    BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, all
                            .getUniqueId().toString(), "nickTable", "autonick").intValue() == 1)
                instance.nickPlayer(all);
        }
    }

    private static ArrayList<String> getNickNameList = new ArrayList<String>();

    public static HashMap<Player, String> isNicked = new HashMap<Player, String>();

    public static HashMap<String, String> getOldName = new HashMap<String, String>();

    public static HashMap<String, String> getPlayerBehindName = new HashMap<String, String>();

    private static HashMap<String, String> getPlayerBehindUUID = new HashMap<String, String>();

    private void makeList() {
        List<String> list = this.ds_nicknames.getStringList("Nickname.list");
        getNickNameList.addAll(list);
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (getNickNameList.contains(all.getName()))
                getNickNameList.remove(all.getName());
        }
    }

    private String getRandomNickName() {
        Random r = new Random();
        if (getNickNameList.size() == 0)
            return null;
        int i = r.nextInt(getNickNameList.size() - 1);
        String name = getNickNameList.get(i);
        getNickNameList.remove(name);
        return name;
    }

    private static String getUUID(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replaceAll("-", "");
    }

    public void nickPlayer(final Player p) {
        final String s = getRandomNickName();
        if (s == null)
            return;
        if (isNicked.containsKey(p)) {
            unnickPlayer(p, false);
            (new BukkitRunnable() {
                public void run() {
                    String oldnickname = Nick.isNicked.get(p);
                    Nick.getPlayerBehindUUID.remove(oldnickname);
                    Nick.getPlayerBehindName.remove(oldnickname);
                    Nick.isNicked.remove(p);
                    Nick.isNicked.put(p, s);
                    Nick.getPlayerBehindUUID.put(s, p.getUniqueId().toString());
                    Nick.getPlayerBehindName.put(s, Nick.getOldName.get(p.getUniqueId().toString()));
                    Nick.getNickNameList.add(oldnickname);
                    Nick.getNickNameList.remove(s);
                }
            }).runTaskLater((Plugin)BuildFFA.instance, 8L);
        } else {
            isNicked.put(p, s);
            getOldName.put(p.getUniqueId().toString(), BuildFFA.mysqlMethods.loadStringFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "coinsTable", "name"));
            getPlayerBehindUUID.put(s, p.getUniqueId().toString());
            getPlayerBehindName.put(s, getOldName.get(p.getUniqueId().toString()));
            getNickNameList.remove(s);
            p.sendMessage(this.core.translateString(this.ds_nicksettings.getString("settings.nick").replace("%NAME%", s)));
        }
        setName(p, s);
        ((CoreScoreBoard) CachePlayer.getBoard.get(p.getUniqueId().toString())).nickPlayer();
    }

    public void unnickPlayer(Player p, boolean quit) {
        String uuid = p.getUniqueId().toString();
        getNickNameList.add(isNicked.get(p));
        String name = getOldName.get(uuid);
        setName(p, name);
        String oldnickname = isNicked.get(p);
        getPlayerBehindUUID.remove(oldnickname);
        getPlayerBehindName.remove(oldnickname);
        isNicked.remove(p);
        getOldName.remove(uuid);
        if (!quit) {
            ((CoreScoreBoard)CachePlayer.getBoard.get(p.getUniqueId().toString())).unnickPlayer();
            p.sendMessage(this.core.translateString(this.ds_nicksettings.getString("settings.unnick")));
        }
        if (p.hasMetadata("loading"))
            p.removeMetadata("loading", (Plugin)BuildFFA.instance);
    }

    public void unnickPlayerOnReload() {
        for (Player p : isNicked.keySet()) {
            String name = getOldName.get(p.getUniqueId().toString());
            setNameQuick(p, name);
            destroyPlayer(p);
            p.sendMessage(BuildFFACore.instance.translateString(this.ds_nicksettings.getString("settings.unnick")));
        }
    }

    public void destroyPlayer(Player p) {
        if (p.hasMetadata("loading"))
            p.removeMetadata("loading", (Plugin)BuildFFA.instance);
        String uuid = p.getUniqueId().toString();
        CraftPlayer cp = (CraftPlayer)p;
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[] { cp.getEntityId() });
        sendPacket((Packet<?>)destroy);
        PacketPlayOutPlayerInfo removeTab = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { cp.getHandle() });
        sendPacket((Packet<?>)removeTab);
        CachePlayer.getBoard.remove(uuid);
        CachePlayer.getTeamName.remove(uuid);
    }

    private void setName(final Player p, String nick) {
        final CraftPlayer cp = (CraftPlayer) p;
        try {
            name1.set(cp.getProfile(), nick);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[]{cp.getEntityId()});
        sendPacket((Packet<?>) destroy);
        PacketPlayOutPlayerInfo removeTab = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{cp.getHandle()});
        sendPacket((Packet<?>) removeTab);
        (new BukkitRunnable() {
            public void run() {
                PacketPlayOutPlayerInfo addTab = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{cp.getHandle()});
                Nick.sendPacket((Packet<?>) addTab);
                PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn((EntityHuman) cp.getHandle());
                Nick.sendPacket((Packet<?>) spawn, p);
            }
        }).runTaskLater((Plugin) BuildFFA.instance, 2L);
        setSkin(cp, nick);
    }

    private void setNameQuick(Player p, String nick) {
        CraftPlayer cp = (CraftPlayer) p;
        try {
            name1.set(cp.getProfile(), nick);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[]{cp.getEntityId()});
        sendPacket((Packet<?>) destroy);
        PacketPlayOutPlayerInfo removeTab = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{cp.getHandle()});
        sendPacket((Packet<?>) removeTab);
        PacketPlayOutPlayerInfo addTab = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{cp.getHandle()});
        sendPacket((Packet<?>) addTab);
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn((EntityHuman) cp.getHandle());
        sendPacket((Packet<?>) spawn, p);
        setSkinQuick(cp, nick);
    }

    private void setSkin(final CraftPlayer p, String nick) {
        try {
            GameProfile gp = p.getProfile();
            gp.getProperties().clear();
            Skin skin = new Skin(getUUID(nick));
            if (skin.getSkinName() != null)
                gp.getProperties().put(skin.getSkinName(), new Property(skin
                        .getSkinName(), skin.getSkinValue(), skin.getSkinSignatur()));
            (new BukkitRunnable() {
                public void run() {
                    for (Player all : Bukkit.getOnlinePlayers())
                        all.hidePlayer((Player)p);
                }
            }).runTaskLater((Plugin)BuildFFA.instance, 1L);
            (new BukkitRunnable() {
                public void run() {
                    for (Player all : Bukkit.getOnlinePlayers())
                        all.showPlayer((Player)p);
                }
            }).runTaskLater((Plugin)BuildFFA.instance, 5L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSkinQuick(CraftPlayer p, String nick) {
        try {
            GameProfile gp = p.getProfile();
            gp.getProperties().clear();
            getUUID(nick);
            Skin skin = new Skin(getUUID(nick));
            if (skin.getSkinName() != null)
                gp.getProperties().put(skin.getSkinName(), new Property(skin
                        .getSkinName(), skin.getSkinValue(), skin.getSkinSignatur()));
            for (Player all : Bukkit.getOnlinePlayers())
                all.hidePlayer((Player)p);
            for (Player all : Bukkit.getOnlinePlayers())
                all.showPlayer((Player)p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Packet<?> packet) {
        for (Player all : Bukkit.getOnlinePlayers())
            (((CraftPlayer)all).getHandle()).playerConnection.sendPacket(packet);
    }

    private static void sendPacket(Packet<?> packet, Player p) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all != p)
                (((CraftPlayer)all).getHandle()).playerConnection.sendPacket(packet);
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            return null;
        }
    }
}