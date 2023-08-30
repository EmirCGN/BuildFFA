package de.emir.utils.forcemap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.emir.main.BuildFFA;
import de.emir.main.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ForceMapFile {
    public static void updateForceMapInvFile() {
        File FILE = new File(BuildFFA.getFilePath + "Inventory/", "forcemap.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(FILE);
        yamlConfiguration.addDefault("inv.settings.name1", "&8» &eForceMap/Kit");
        yamlConfiguration.addDefault("inv.settings.name2", "&8» &eForceMap");
        yamlConfiguration.addDefault("inv.settings.name3", "&8» &eForceKit");
        yamlConfiguration.addDefault("inv.settings.animation", Boolean.valueOf(true));
        yamlConfiguration.addDefault("inv.settings.glascolorPRIM", Integer.valueOf(0));
        yamlConfiguration.addDefault("inv.settings.glascolorSEC", Integer.valueOf(7));
        List<String> lore = new ArrayList<String>();
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.defaultmap", 0, 358, 0, "&8&e&6%NAME%", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        yamlConfiguration.set("inv.items.defaultmap.pos", null);
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.defaultkit", 0, 311, 0, "&8&e&6%NAME%", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        yamlConfiguration.set("inv.items.defaultkit.pos", null);
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.map", 11, 358, 0, "&8&e&6ForceMap", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.itemFile((FileConfiguration)yamlConfiguration, "inv.items.kit", 15, 311, 0, "&8&e&6ForceKit", lore, Boolean.valueOf(false), Boolean.valueOf(true));
        FileManager.saveFILE((FileConfiguration)yamlConfiguration, FILE);
    }
}
