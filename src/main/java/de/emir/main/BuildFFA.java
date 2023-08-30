package de.emir.main;

import de.emir.sql.MySQL;
import de.emir.sql.MySQLMethods;
import de.emir.utils.*;
import de.emir.utils.cycle.CycleManager;
import de.emir.utils.cycle.KitData;
import de.emir.utils.cycle.KitFile;
import de.emir.utils.cycle.MapData;
import de.emir.utils.forcemap.ForceMapFile;
import de.emir.utils.inv.Inv;
import de.emir.utils.inv.InvFile;
import de.emir.utils.nick.Nick;
import de.emir.utils.nick.NickFile;
import de.emir.utils.scoreboard.CachePlayer;
import de.emir.utils.scoreboard.CacheScoreboard;
import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.List;

import de.emir.utils.scoreboard.CoreScoreBoard;
import de.emir.utils.stats.StatsFile;
import org.apache.logging.log4j.core.helpers.Loader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildFFA extends JavaPlugin {
    public static boolean debug;
    public static BuildFFA instance;
    public static MySQLMethods mysqlMethods;
    public static boolean BungeeSystem;

    public static boolean Clans;

    public static int cp_killer;

    public static int cp_player;

    public static boolean invsort;

    public static String invsort_name;

    public static String invsort_type;

    public static boolean Maxplayers;

    public static String Maxplayers_msg;
    public static String getMySQLTable = "buildffaTable";

    public static String getFilePath = "plugins/BuildFFA/";

    public static String getPremissionPrefix = "buildffa";

    public static boolean xperms;

    public static boolean endlessblocks;

    public static int xperms_premium;

    public static String spec_suffix;

    public void onEnable() {
        this; //Bugs
        debug = true;
        this; //Bugs
        instance = this;
        new WorldSaver(); //Noch nicht fertig
        loadconfig();
        registerFiles();
        (new CacheScoreboard()).loadCache();
        (new Loader()).loadFileData(); //Noch nicht fertig
        registerCommands();
        registerListeners();
        BungeeSystem = getConfig().getBoolean("BungeeSystem");
        xperms = getConfig().getBoolean("xperms.enabled");
        xperms_premium = getConfig().getInt("xperms.premiumid");
        Clans = getConfig().getBoolean("Clan.enabled");
        cp_killer = getConfig().getInt("Clan.points_killer");
        cp_player = getConfig().getInt("Clan.points_lose");
        Maxplayers = getConfig().getBoolean("Maxplayers.enabled");
        Maxplayers_msg = getConfig().getString("Maxplayers.kickmsg").replaceAll("&", ");
                spec_suffix = getConfig().getString("specmode.suffix");
        invsort = getConfig().getBoolean("invsort.enabled");
        invsort_name = getConfig().getString("invsort.mobname");
        invsort_type = getConfig().getString("invsort.mobtype");
        endlessblocks = getConfig().getBoolean("endlessblocks.enabled");
        run();
    }

    public void onDisable() {
        MySQL.instance.disconnect();
        Nick.instance.unnickPlayerOnReload();
        for (Block b : BuildFFACore.getBlocksPlaced)
            b.setType(Material.AIR);
        removeEntity();
    }

    private void run() {
        new BuildFFACore();
        new MySQL();
        mysqlMethods = new MySQLMethods();
        new PlayerSaver();
        new Inv();
        new CycleManager();
        new CachePlayer();
        new CacheScoreboard();
        sendScoreboard();
        new DeathZone();
        new SpecMode();
        getServer().getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
        Nick.name1 = Nick.getField(GameProfile.class, "name");
        getAllErrorManager();
        new Nick();
    }

    private void registerFiles() {
        (new FileManager()).createDefaultMySQLFile();
        FileManager.createtabfile();
        FileManager.createchatfile();
        InvFile.updateInvFile();
        NickFile.updateNickNameFile();
        NickFile.updateNickSettingsInvFile();
        StatsFile.updateStatsInvFile();
        ForceMapFile.updateForceMapInvFile();
        KitFile.updateKitFile();
        MapFile.updateMapFile(); //Noch nicht fertig
        SortFile.updateSortInvFile(); //Noch nicht fertig
    }

    private void sendScoreboard() {
        for (Player all : Bukkit.getOnlinePlayers())
            new CoreScoreBoard(all);
    }

    private void registerCommands() {
        // Kommt bald
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        // Kommt bald
    }

    public void loadconfig() {
        getConfig().addDefault("messages.prefix", "");
        getConfig().addDefault("messages.noPerm", "You do have permission to use this feature!");
        getConfig().addDefault("messages.fSyntax", "Wrong syntax. Please try this&8: &c/");
        getConfig().addDefault("messages.noCoins", "You do not have enough coins. You are still missing ");
        getConfig().addDefault("messages.noPlayer", "This Player &cdoesn't &7exists.");
        getConfig().addDefault("messages.notOnline", "&7This Player &cisn't &7online right now.");
        getConfig().addDefault("BungeeSystem", Boolean.valueOf(false));
        getConfig().addDefault("xperms.enabled", Boolean.valueOf(false));
        getConfig().addDefault("xperms.premiumid", Integer.valueOf(1));
        getConfig().addDefault("invsort.enabled", Boolean.valueOf(false));
        getConfig().addDefault("invsort.mobname", "&8&cInventory sort &8");
        getConfig().addDefault("invsort.mobtype", "pigzombie");
        getConfig().addDefault("specmode.suffix", " ");
        getConfig().addDefault("endlessblocks.enabled", Boolean.valueOf(false));
        getConfig().addDefault("Maxplayers.enabled", Boolean.valueOf(true));
        getConfig().addDefault("Maxplayers.kickmsg", "&8&7&oThe maxmimum amount of &825 &7&oplayers has been reached. &7Please try again later. &8");
        getConfig().addDefault("Scoreboard.name", "&6&lBuild&e&lFFA");
        List<String> list02 = new ArrayList<String>();
        list02.add("&8&m-------------");
        list02.add("");
        list02.add("&8&l× &7&lCoins");
        list02.add("&8» &e%COINS%");
        list02.add("");
        list02.add("&8&l× &7&lKills");
        list02.add("&8» &e%KILLS%");
        list02.add("");
        list02.add("&8&l× &7&lDeath");
        list02.add("&8» &e%DEATH%");
        list02.add("");
        list02.add("&8&l× &7&lRanking");
        list02.add("&8» &7#&e%RANKING%");
        list02.add("");
        getConfig().addDefault("Scoreboard.list", list02);
        getConfig().addDefault("Scoreboard.clantags", Boolean.valueOf(false));
        getConfig().addDefault("Clan.enabled", Boolean.valueOf(true));
        getConfig().addDefault("Clan.points_killer", Integer.valueOf(2));
        getConfig().addDefault("Clan.points_lose", Integer.valueOf(1));
        getConfig().addDefault("Join.enabled", Boolean.valueOf(true));
        getConfig().addDefault("Join.message", "The Player &6%PLAYER% &7joined the Game.");
        List<Integer> list = new ArrayList<Integer>();
        list.add(Integer.valueOf(3));
        list.add(Integer.valueOf(5));
        list.add(Integer.valueOf(7));
        list.add(Integer.valueOf(9));
        list.add(Integer.valueOf(11));
        list.add(Integer.valueOf(15));
        list.add(Integer.valueOf(17));
        list.add(Integer.valueOf(21));
        list.add(Integer.valueOf(24));
        list.add(Integer.valueOf(27));
        list.add(Integer.valueOf(30));
        list.add(Integer.valueOf(35));
        list.add(Integer.valueOf(40));
        list.add(Integer.valueOf(45));
        list.add(Integer.valueOf(50));
        list.add(Integer.valueOf(60));
        list.add(Integer.valueOf(70));
        list.add(Integer.valueOf(80));
        list.add(Integer.valueOf(90));
        list.add(Integer.valueOf(100));
        getConfig().addDefault("Death.messages.killstreak.list", list);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void getAllErrorManager() {
        DataSaver ds_map = new DataSaver(MapData.getObject_Map);
        DataSaver ds_kit = new DataSaver(KitData.getObject_Kit);
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "_________________________________________________________________________________________");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + " ");
        System.out.println("This Plugin is made by EmirCGN");
        System.out.println("> https://github.com/EmirCGN");
        System.out.println("> Version: normal 1.0.0");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + " ");
        String s = " .";
        ChatColor cc = null;
        if (ds_map.getStringList("List.enabled").size() == 0) {
            s = "ERROR : There are no maps enabled! >>> /editmap <name> create - to create one";
            cc = ChatColor.DARK_RED;
        } else if (ds_map.getStringList("List.enabled").size() == 1) {
            if (ds_map.getInt("Map.MapSystem").intValue() == 0) {
                s = "ERROR : You need more than one map for the 'MapChange' [0] System! >>> /editmap <name> create - to create one";
                cc = ChatColor.DARK_RED;
            } else if (ds_map.getInt("Map.MapSystem").intValue() == 1) {
                s = "SUCCSES : Succsesfully started 'RandomMap' [1] with " + ds_map.getStringList("List.enabled").size() + " Maps!";
                cc = ChatColor.DARK_GREEN;
            } else if (ds_map.getInt("Map.MapSystem").intValue() == 2) {
                s = "SUCCSES : Succsesfully started 'Just one map' [2] with " + ds_map.getStringList("List.enabled").size() + " Maps!";
                cc = ChatColor.DARK_GREEN;
            } else {
                s = "ERROR : There is no opinion called " + ds_map.getInt("Map.MapSystem") + "! There are these options: [0] MapChange; [1] RandomMap; [2] Just one Map";
                cc = ChatColor.DARK_RED;
            }
        } else if (ds_map.getStringList("List.enabled").size() > 1) {
            if (ds_map.getInt("Map.MapSystem").intValue() == 0) {
                s = "SUCCSES : Succsesfully started 'MapChange' [0] with " + ds_map.getStringList("List.enabled").size() + " Maps!";
                cc = ChatColor.DARK_GREEN;
            } else if (ds_map.getInt("Map.MapSystem").intValue() == 1) {
                s = "SUCCSES : Succsesfully started 'RandomMap' [1] with " + ds_map.getStringList("List.enabled").size() + " Maps!";
                cc = ChatColor.DARK_GREEN;
            } else if (ds_map.getInt("Map.MapSystem").intValue() == 2) {
                s = "SUCCSES : Succsesfully started 'Just one map' [2] with " + ds_map.getStringList("List.enabled").size() + " Maps!";
                cc = ChatColor.DARK_GREEN;
            } else {
                s = "ERROR : There is no opinion called " + ds_map.getInt("Map.MapSystem") + "! There are these options: [0] MapChange; [1] RandomMap; [2] Just one Map";
                cc = ChatColor.DARK_RED;
            }
        }
        getServer().getConsoleSender().sendMessage(cc + s);
        String s1 = ".";
        ChatColor cc1 = null;
        if (ds_kit.getStringList("List.enabled").size() == 0) {
            s1 = "ERROR: There are no kits enabled! >>> /editkit <name> create - to create one";
            cc1 = ChatColor.DARK_RED;
        } else if (ds_kit.getStringList("List.enabled").size() == 1) {
            if (ds_kit.getInt("Kits.KitSystem").intValue() == 0) {
                s1 = "ERROR: You need more than one kit for the 'KitChange' [0] System! >>> /editkit <name> create - to create one";
                cc1 = ChatColor.DARK_RED;
            } else if (ds_kit.getInt("Kits.KitSystem").intValue() == 1) {
                s1 = "SUCCSES: Succsesfully started 'RandomKit' [1] with " + ds_kit.getStringList("List.enabled").size() + " Kits!";
                cc1 = ChatColor.DARK_GREEN;
            } else if (ds_kit.getInt("Kits.KitSystem").intValue() == 2) {
                s1 = "SUCCSES: Succsesfully started 'Just one kit' [2] with " + ds_kit.getStringList("List.enabled").size() + " Kits!";
                cc1 = ChatColor.DARK_GREEN;
            } else {
                s1 = "ERROR: There is no opinion called " + ds_kit.getInt("Kits.KitSystem") + "! There are these options: [0] KitChange; [1] RandomKit; [2] Just one Kit";
                cc1 = ChatColor.DARK_RED;
            }
        } else if (ds_kit.getStringList("List.enabled").size() > 1) {
            if (ds_kit.getInt("Kits.KitSystem").intValue() == 0) {
                s1 = "SUCCSES: Succsesfully started 'KitChange' [0] with " + ds_kit.getStringList("List.enabled").size() + " Kits!";
                cc1 = ChatColor.DARK_GREEN;
            } else if (ds_kit.getInt("Kits.KitSystem").intValue() == 1) {
                s1 = "SUCCSES: Succsesfully started 'RandomKit' [1] with " + ds_kit.getStringList("List.enabled").size() + " Kits!";
                cc1 = ChatColor.DARK_GREEN;
            } else if (ds_kit.getInt("Kits.KitSystem").intValue() == 2) {
                s1 = "SUCCSES: Succsesfully started 'Just one kit' [2] with " + ds_kit.getStringList("List.enabled").size() + " Kits!";
                cc1 = ChatColor.DARK_GREEN;
            } else {
                s1 = "ERROR: There is no opinion called " + ds_kit.getInt("Kits.KitSystem") + "! There are these options: [0] KitChange; [1] RandomKit; [2] Just one Kit";
                cc1 = ChatColor.DARK_RED;
            }
        }
        getServer().getConsoleSender().sendMessage(cc1 + s1);
        String s2 = ".";
        ChatColor cc2 = null;
        if (MySQL.instance.isConnected()) {
            s2 = "SUCCSES: The MySQL Database is succsesfully connected";
            cc2 = ChatColor.DARK_GREEN;
        } else {
            s2 = "ERROR: The MySQL Database cannot connect to the server";
            cc2 = ChatColor.DARK_RED;
        }
        getServer().getConsoleSender().sendMessage(cc2 + s2);
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + " ");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "_________________________________________________________________________________________");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + " ");
    }

    public void removeEntity() {
        for (Entity entity : RegisterMob.getEntity)
            entity.remove();
        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e.isCustomNameVisible() && e.getCustomName() != null && e.getCustomName().equals(invsort_name.replaceAll("&", "§")))
                        e.remove();
            }
        }
    }
}
