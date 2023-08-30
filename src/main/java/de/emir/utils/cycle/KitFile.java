package de.emir.utils.cycle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.emir.main.BuildFFA;
import org.bukkit.configuration.file.YamlConfiguration;

public class KitFile {
    public static void updateKitFile() {
        File File = new File(BuildFFA.getFilePath + "Kits", "settings.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(File);
        List<String> list = new ArrayList<String>();
        yamlConfiguration.addDefault("Kits.KitSystem", Integer.valueOf(0));
        yamlConfiguration.addDefault("Kits.Defaultkit", "default");
        yamlConfiguration.addDefault("Kits.time", Integer.valueOf(120));
        yamlConfiguration.addDefault("Kits.sandstonedelay", Integer.valueOf(5));
        yamlConfiguration.addDefault("Kits.cobwebdelay", Integer.valueOf(5));
        yamlConfiguration.addDefault("Kits.sandstonereplaceID", Integer.valueOf(152));
        yamlConfiguration.addDefault("Kits.sandstonereplaceSubID", Integer.valueOf(0));
        File dir = new File(BuildFFA.getFilePath + "Kits");
        if (dir.isDirectory() && (dir.listFiles()).length != 0)
            for (File f : dir.listFiles()) {
                if ((dir.listFiles()).length != 0 && !f.getName().equals("settings.yml"))
                    list.add(f.getName().replace(".yml", ""));
            }
        yamlConfiguration.addDefault("List.enabled", list);
        yamlConfiguration.options().copyDefaults(true);
        try {
            yamlConfiguration.save(File);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
