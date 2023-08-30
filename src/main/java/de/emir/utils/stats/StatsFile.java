package de.emir.utils.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.emir.main.BuildFFA;
import de.emir.main.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StatsFile {
    public static void updateStatsInvFile() {
        File FILE = new File(BuildFFA.getFilePath + "Inventory/", "stats.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(FILE);
        yamlConfiguration.addDefault("settings.allowbuytokens", Boolean.valueOf(true));
        yamlConfiguration.addDefault("settings.tokenprice", Integer.valueOf(2500));
        yamlConfiguration.addDefault("inv.settings.name1", "&8&eStats");
        yamlConfiguration.addDefault("inv.settings.name2", "&8&eStatsreset");
        yamlConfiguration.addDefault("inv.settings.name3", "&8&eBuy Tokens");
        yamlConfiguration.addDefault("inv.settings.size1", Integer.valueOf(45));
        yamlConfiguration.addDefault("inv.settings.size2", Integer.valueOf(27));
        yamlConfiguration.addDefault("inv.settings.size3", Integer.valueOf(27));
        yamlConfiguration.addDefault("inv.settings.animation", Boolean.valueOf(true));
        yamlConfiguration.addDefault("inv.settings.glascolorPRIM", Integer.valueOf(0));
        yamlConfiguration.addDefault("inv.settings.glascolorSEC", Integer.valueOf(7));
        List<String> lore = new ArrayList<String>();
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.death", 10, 311, 0, "&8&7Death&8: &e&6%VALUE%", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.kills", 12, 331, 0, "&8&7Kills&8: &e&6%VALUE%", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.points", 14, 351, 13, "&8&7Points&8: &e&6%VALUE%", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.kd", 16, 319, 0, "&8&7KD&8: &6%VALUE%", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.buyreset", 31, 329, 0, "&8&6Buy Tokens", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.reset", 33, 339, 0, "&8&6Statsreset", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        List<String> lore1 = new ArrayList<String>();
        lore1.add("&8&m--------------------");
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.top", 29, 0, 0, "&8&e&6Top10", lore1, Boolean.valueOf(false), Boolean.valueOf(true));
        yamlConfiguration.set("inv.items.top.id", null);
        yamlConfiguration.set("inv.items.top.subid", null);
        yamlConfiguration.set("inv.items.top.template", "&8&7#&e%ID% &8- &7%NAME%");
        List<String> lore2 = new ArrayList<String>();
        lore2.add("&8&m--------------------");
        lore2.add("&7Price&8: &e2500");
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.buy", 11, 266, 0, "&8&6Buy one Token", lore2, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.execute", 11, 351, 10, "&8&6Execute", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.cancel", 15, 351, 1, "&8&6Cancel", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.saveFILE((FileConfiguration)yamlConfiguration, FILE);
    }
}