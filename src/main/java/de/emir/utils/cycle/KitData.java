package de.emir.utils.cycle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.emir.main.BuildFFA;
import de.emir.utils.DataSaver;
import org.bukkit.configuration.file.YamlConfiguration;

public class KitData {
    public static HashMap<String, Object> getObject_Kit = new HashMap<String, Object>();

    public static HashMap<String, HashMap<String, Object>> getObject_Kit_values = new HashMap<String, HashMap<String, Object>>();

    public void loadData_Kit() {
        File file = new File(BuildFFA.getFilePath + "Kits", "settings.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        List<String> getSectionsItems = new ArrayList<String>();
        if (yamlConfiguration.getConfigurationSection("") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("").getKeys(false))
                getSectionsItems.add(key);
            for (String key : getSectionsItems) {
                for (String key1 : yamlConfiguration.getConfigurationSection(key).getKeys(false))
                    getObject_Kit.put(key + "." + key1, yamlConfiguration.get(key + "." + key1));
            }
            getSectionsItems.clear();
        }
        try {
            yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData_Kit_value() {
        DataSaver ds = new DataSaver(getObject_Kit);
        List<String> list = ds.getStringList("List.enabled");
        for (String s : list) {
            HashMap<String, Object> list1 = new HashMap<String, Object>();
            File file = new File(BuildFFA.getFilePath + "Kits", s + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            if (yamlConfiguration.getConfigurationSection("Kits.items.content") != null)
                for (String key : yamlConfiguration.getConfigurationSection("Kits.items.content").getKeys(false))
                    list1.put("Kits.items.content." + key, yamlConfiguration.get("Kits.items.content." + key));
            if (yamlConfiguration.getConfigurationSection("Kits.items") != null)
                for (String key : yamlConfiguration.getConfigurationSection("Kits.items").getKeys(false)) {
                    if (!key.equals("content"))
                        list1.put("Kits.items." + key, yamlConfiguration.get("Kits.items." + key));
                }
            try {
                yamlConfiguration.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getObject_Kit_values.put(s, list1);
        }
    }
}