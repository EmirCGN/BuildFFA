package de.emir.utils.nick;

import de.emir.main.BuildFFA;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class NickData {
    public static HashMap<String, Object> getObject_nicknames = new HashMap<String, Object>();

    public static HashMap<String, Object> getObject_nicksettings = new HashMap<String, Object>();

    public void loadData_NickNames() {
        File file = new File(BuildFFA.getFilePath + "/Inventory/", "nicknames.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (yamlConfiguration.getConfigurationSection("Nickname") != null)
            for (String key : yamlConfiguration.getConfigurationSection("Nickname").getKeys(false))
                getObject_nicknames.put("Nickname." + key, yamlConfiguration.get("Nickname." + key));
        try {
            yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
