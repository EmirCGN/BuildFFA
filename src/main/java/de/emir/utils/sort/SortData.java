package de.emir.utils.sort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.emir.main.BuildFFA;
import org.bukkit.configuration.file.YamlConfiguration;

public class SortData {
    public static HashMap<String, Object> getObject_sort = new HashMap<>();

    public void loadData_sort() {
        File file = new File(BuildFFA.getFilePath + "Inventory/", "sort.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        List<String> getSectionsItems = new ArrayList<>();
        if (yamlConfiguration.getConfigurationSection("settings") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("settings").getKeys(false)) {
                Object o = yamlConfiguration.get("settings." + key);
                if (o instanceof String) {
                    String s = (String) o;
                    o = s.replaceAll("&", "§");
                } else if (o instanceof List) {
                    List<?> list = (List<?>) o;
                    if (!list.isEmpty() && list.get(0) instanceof String) {
                        List<String> list1 = (List<String>) list;
                        List<String> list2 = new ArrayList<>();
                        for (String s : list1)
                            list2.add(s.replaceAll("&", "§"));
                        o = list2;
                    }
                }
                getObject_sort.put("settings." + key, o);
            }
        }
        if (yamlConfiguration.getConfigurationSection("inv.settings") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("inv.settings").getKeys(false)) {
                Object o = yamlConfiguration.get("inv.settings." + key);
                if (o instanceof String) {
                    String s = (String) o;
                    o = s.replaceAll("&", "§");
                } else if (o instanceof List) {
                    List<?> list = (List<?>) o;
                    if (!list.isEmpty() && list.get(0) instanceof String) {
                        List<String> list1 = (List<String>) list;
                        List<String> list2 = new ArrayList<>();
                        for (String s : list1)
                            list2.add(s.replaceAll("&", "§"));
                        o = list2;
                    }
                }
                getObject_sort.put("inv.settings." + key, o);
            }
        }
        if (yamlConfiguration.getConfigurationSection("inv.items") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("inv.items").getKeys(false)) {
                getSectionsItems.add(key);
                getObject_sort.put("inv.items." + key, "XXX");
            }
            for (String key : getSectionsItems) {
                for (String key1 : yamlConfiguration.getConfigurationSection("inv.items." + key).getKeys(false)) {
                    Object o = yamlConfiguration.get("inv.items." + key + "." + key1);
                    if (o instanceof String) {
                        String s = (String) o;
                        o = s.replaceAll("&", "§");
                    } else if (o instanceof List) {
                        List<?> list = (List<?>) o;
                        if (!list.isEmpty() && list.get(0) instanceof String) {
                            List<String> list1 = (List<String>) list;
                            List<String> list2 = new ArrayList<>();
                            for (String s : list1)
                                list2.add(s.replaceAll("&", "§"));
                            o = list2;
                        }
                    }
                    getObject_sort.put("inv.items." + key + "." + key1, o);
                }
            }
            getSectionsItems.clear();
        }
        try {
            yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
