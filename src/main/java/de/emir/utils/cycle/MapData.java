package de.emir.utils.cycle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.emir.main.BuildFFA;
import de.emir.main.FileManager;
import de.emir.utils.DataSaver;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MapData {
    public static HashMap<String, Object> getObject_Map = new HashMap<String, Object>();

    public static HashMap<String, HashMap<String, Object>> getObject_Map_values = new HashMap<String, HashMap<String, Object>>();

    public void loadData_MapSettings() {
        File file = new File(BuildFFA.getFilePath + "Maps", "settings.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        List<String> getSectionsItems = new ArrayList<String>();
        if (yamlConfiguration.getConfigurationSection("") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("").getKeys(false)) {
                getSectionsItems.add(key);
                getObject_Map.put(key, "XXX");
            }
            for (String key : getSectionsItems) {
                for (String key1 : yamlConfiguration.getConfigurationSection(key).getKeys(false))
                    getObject_Map.put(key + "." + key1, yamlConfiguration.get(key + "." + key1));
            }
            getSectionsItems.clear();
        }
        try {
            yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData_Map_value() {
        DataSaver ds = new DataSaver(getObject_Map);
        List<String> list = ds.getStringList("List.enabled");
        for (String s : list) {
            HashMap<String, Object> list1 = new HashMap<String, Object>();
            File file = new File(BuildFFA.getFilePath + "Maps", s + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            if (yamlConfiguration.contains(s + ".Spawn"))
                list1.put(s + ".Spawn", FileManager.getDataLocation((FileConfiguration)yamlConfiguration, s + ".Spawn"));
            if (yamlConfiguration.contains(s + ".mob"))
                list1.put(s + ".mob", FileManager.getDataLocation((FileConfiguration)yamlConfiguration, s + ".mob"));
            if (yamlConfiguration.getConfigurationSection("High") != null)
                for (String key : yamlConfiguration.getConfigurationSection("High").getKeys(false))
                    list1.put("High." + key, yamlConfiguration.get("High." + key));
            List<Location> randomList = new ArrayList<Location>();
            if (yamlConfiguration.getConfigurationSection(s + ".Randoms") != null)
                for (String key : yamlConfiguration.getConfigurationSection(s + ".Randoms").getKeys(false))
                    randomList.add(FileManager.getDataLocation((FileConfiguration)yamlConfiguration, s + ".Randoms." + key));
            list1.put(s + ".randomlist", randomList);
            try {
                yamlConfiguration.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getObject_Map_values.put(s, list1);
        }
    }
}