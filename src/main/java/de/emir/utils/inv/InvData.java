package de.emir.utils.inv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.emir.main.BuildFFA;
import org.bukkit.configuration.file.YamlConfiguration;

public class InvData {
    public static HashMap<String, Object> getObject_Inv = new HashMap<String, Object>();

    public void loadData_Inv() {
        File file = new File(BuildFFA.getFilePath + "Inventory/", "main.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        List<String> getSectionsItems = new ArrayList<String>();

        if (yamlConfiguration.getConfigurationSection("inv.items") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("inv.items").getKeys(false)) {
                getSectionsItems.add(key);
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
                            List<String> list2 = new ArrayList<String>();
                            for (String s : list1) {
                                list2.add(s.replaceAll("&", "§"));
                            }
                            o = list2;
                        }
                    }

                    getObject_Inv.put("inv.items." + key + "." + key1, o);
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