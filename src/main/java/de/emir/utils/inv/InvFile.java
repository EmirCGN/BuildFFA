package de.emir.utils.inv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class InvFile {
    public static void updateInvFile() {
        File FILE = new File(BuildFFA.getFilePath + "Inventory/", "main.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(FILE);
        List<String> lore = new ArrayList<String>();
        lore.add("&8&m--------------------");
        lore.add("&7View your stats here.");
        BuildFFACore.saveItemInFile((FileConfiguration) yamlConfiguration, "inv.items.stats", 3, 340, 0, "&8&6Stats &8", lore, Boolean.valueOf(false));
        yamlConfiguration.addDefault("inv.items.stats.enabled", Boolean.valueOf(true));
        List<String> lore2 = new ArrayList<String>();
        lore2.add("&8&m--------------------");
        lore2.add("&7Teleport to a random Location.");
        BuildFFACore.saveItemInFile((FileConfiguration) yamlConfiguration, "inv.items.random", 5, 402, 0, "&8&6Random Spawner &8", lore2, Boolean.valueOf(false));
        yamlConfiguration.addDefault("inv.items.random.enabled", Boolean.valueOf(true));
        List<String> lore3 = new ArrayList<String>();
        lore3.add("&8&m--------------------");
        lore3.add("&7Change to a new map.");
        BuildFFACore.saveItemInFile((FileConfiguration) yamlConfiguration, "inv.items.forcemap", 7, 331, 0, "&8&6ForceMap &8", lore3, Boolean.valueOf(false));
        yamlConfiguration.addDefault("inv.items.forcemap.enabled", Boolean.valueOf(true));
        List<String> lore4 = new ArrayList<String>();
        lore4.add("&8&m--------------------");
        lore4.add("&7Nick your name here.");
        BuildFFACore.saveItemInFile((FileConfiguration) yamlConfiguration, "inv.items.nick", 1, 421, 0, "&8&6Nick-Tool &8", lore4, Boolean.valueOf(false));
                yamlConfiguration.addDefault("inv.items.nick.enabled", Boolean.valueOf(true));
        BuildFFACore.saveFile((FileConfiguration) yamlConfiguration, FILE);
    }
}
