package de.emir.main;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.emir.utils.DataSaver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FileManager {
    public static ItemStack createItem(Material mat, int amount, int subid, String name) {
        ItemStack i = new ItemStack(mat, amount, (short)subid);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(name);
        i.setItemMeta(m);
        return i;
    }

    public static void saveFILE(FileConfiguration file, File FILE) {
        file.options().copyDefaults(true);
        try {
            file.save(FILE);
        } catch (IOException e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
    }

    public static void itemFile(FileConfiguration file, String path, int pos, int id, int subid, String name, List<String> lore, Boolean enchant, Boolean enabled) {
        file.addDefault(path + ".name", name);
        file.addDefault(path + ".pos", Integer.valueOf(pos));
        file.addDefault(path + ".id", Integer.valueOf(id));
        file.addDefault(path + ".subid", Integer.valueOf(subid));
        file.addDefault(path + ".lore", lore);
        file.addDefault(path + ".enchant", enchant);
        file.addDefault(path + ".enabled", enabled);
    }

    public static void addDataLocation(File file, FileConfiguration cfg, Location loc, String path) {
        cfg.set(path + ".world", loc.getWorld().getName());
        cfg.set(path + ".x", Double.valueOf(loc.getX()));
        cfg.set(path + ".y", Double.valueOf(loc.getY()));
        cfg.set(path + ".z", Double.valueOf(loc.getZ()));
        cfg.set(path + ".yaw", Float.valueOf(loc.getYaw()));
        cfg.set(path + ".pitch", Float.valueOf(loc.getPitch()));
        saveFILE(cfg, file);
    }

    public static Location getDataLocation(FileConfiguration cfg, String path) {
        Location loc = null;
        String world = cfg.getString(path + ".world");
        double x = cfg.getDouble(path + ".x");
        double y = cfg.getDouble(path + ".y");
        double z = cfg.getDouble(path + ".z");
        float yaw = (float)cfg.getDouble(path + ".yaw");
        float pitch = (float)cfg.getDouble(path + ".pitch");
        loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        return loc;
    }

    public static Location getDataLocationDataSaver(DataSaver ds, String path) {
        Location loc = null;
        String world = ds.getString(path + ".world");
        double x = ds.getDouble(path + ".x").doubleValue();
        double y = ds.getDouble(path + ".y").doubleValue();
        double z = ds.getDouble(path + ".z").doubleValue();
        float yaw = ds.getFloat(path + ".yaw").floatValue();
        float pitch = ds.getFloat(path + ".pitch").floatValue();
        loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        return loc;
    }

    public static HashMap<String, Object> getObject_MySQL = new HashMap<String, Object>();

    public void createDefaultMySQLFile() {
        try {
            File File = new File(BuildFFA.getFilePath, "MySQL.yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(File);
            yamlConfiguration.addDefault("MySQL.Host", "localhost");
            yamlConfiguration.addDefault("MySQL.Port", Integer.valueOf(3306));
            yamlConfiguration.addDefault("MySQL.Database", "database");
            yamlConfiguration.addDefault("MySQL.User", "root");
            yamlConfiguration.addDefault("MySQL.Password", "password");
            yamlConfiguration.options().copyDefaults(true);
            yamlConfiguration.save(File);
        } catch (Exception e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
    }

    public void loadDefaultMySQLFile() {
        try {
            File File = new File(BuildFFA.getFilePath, "MySQL.yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(File);
            getObject_MySQL.put("Host", yamlConfiguration.get("MySQL.Host"));
            getObject_MySQL.put("Port", yamlConfiguration.get("MySQL.Port"));
            getObject_MySQL.put("Database", yamlConfiguration.get("MySQL.Database"));
            getObject_MySQL.put("User", yamlConfiguration.get("MySQL.User"));
            getObject_MySQL.put("Password", yamlConfiguration.get("MySQL.Password"));
            yamlConfiguration.save(File);
        } catch (Exception e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
    }

    public static void createtabfile() {
        File FILE = new File(BuildFFA.getFilePath + "Utils/", "tab.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(FILE);
        yamlConfiguration.addDefault("1.prefix", "&4&lAdmin &7| &4");
        yamlConfiguration.addDefault("1.perm", "tab.admin");
        yamlConfiguration.addDefault("2.prefix", "&4&lYouTuber+ &7| &4");
        yamlConfiguration.addDefault("2.perm", "tab.youtuberplus");
        yamlConfiguration.addDefault("3.prefix", "&bDev &7| &4");
        yamlConfiguration.addDefault("2.perm", "tab.dev");

    }
    public static void createchatfile() {
        File FILE = new File(BuildFFA.getFilePath + "Utils/", "chat.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(FILE);
       yamlConfiguration.addDefault("1.format","&4&lAdmin &7• &4%PLAYER% &7→ &f%MESSAGE%");
       yamlConfiguration.addDefault("1.perm","chat.admin");
        saveFILE((FileConfiguration)yamlConfiguration, FILE);
    }

    public static HashMap<String, Object> getObject_Chat = new HashMap<String, Object>();
    public static HashMap<String, Object> getObject_Tab = new HashMap<String, Object>();

    public void loadData_Chat() {
        File file = new File(BuildFFA.getFilePath + "Utils/", "chat.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> getObject_Chat = new HashMap<String, Object>();
        List<String> getSectionsItems = new ArrayList<String>();

        if (yamlConfiguration.getConfigurationSection("default") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("default").getKeys(false)) {
                Object o = yamlConfiguration.get("default." + key);
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
                getObject_Chat.put("default." + key, o);
            }
        }

        if (yamlConfiguration.getConfigurationSection("nick") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("nick").getKeys(false)) {
                getObject_Chat.put("nick." + key, yamlConfiguration.get("nick." + key));
            }
        }

        for (int i = 1; i < 33; i++) {
            if (yamlConfiguration.getConfigurationSection(String.valueOf(i)) != null) {
                for (String key : yamlConfiguration.getConfigurationSection(String.valueOf(i)).getKeys(false)) {
                    Object o = yamlConfiguration.get(i + "." + key);
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
                    getObject_Chat.put(i + "." + key, o);
                }
                getSectionsItems.clear();
            }
        }

        try {
            yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData_Tab() {
        File file = new File(BuildFFA.getFilePath + "Utils/", "tab.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> getObject_Tab = new HashMap<String, Object>();
        List<String> getSectionsItems = new ArrayList<String>();

        if (yamlConfiguration.getConfigurationSection("default") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("default").getKeys(false)) {
                Object o = yamlConfiguration.get("default." + key);
                if (o instanceof String) {
                    String s = (String) o;
                    o = s.replaceAll("&", "");
                } else if (o instanceof List) {
                    List<?> list = (List<?>) o;
                    if (!list.isEmpty() && list.get(0) instanceof String) {
                        List<String> list1 = (List<String>) list;
                        List<String> list2 = new ArrayList<String>();
                        for (String s : list1) {
                            list2.add(s.replaceAll("&", ""));
                        }
                        o = list2;
                    }
                }
                getObject_Tab.put("default." + key, o);
            }
        }

        if (yamlConfiguration.getConfigurationSection("nick") != null) {
            for (String key : yamlConfiguration.getConfigurationSection("nick").getKeys(false)) {
                Object o = yamlConfiguration.get("nick." + key);
                if (o instanceof String) {
                    String s = (String) o;
                    o = s.replaceAll("&", "");
                } else if (o instanceof List) {
                    List<?> list = (List<?>) o;
                    if (!list.isEmpty() && list.get(0) instanceof String) {
                        List<String> list1 = (List<String>) list;
                        List<String> list2 = new ArrayList<String>();
                        for (String s : list1) {
                            list2.add(s.replaceAll("&", ""));
                        }
                        o = list2;
                    }
                }
                getObject_Tab.put("nick." + key, o);
            }
        }

        for (int i = 1; i < 33; i++) {
            if (yamlConfiguration.getConfigurationSection(String.valueOf(i)) != null) {
                for (String key : yamlConfiguration.getConfigurationSection(String.valueOf(i)).getKeys(false)) {
                    Object o = yamlConfiguration.get(i + "." + key);
                    if (o instanceof String) {
                        String s = (String) o;
                        o = s.replaceAll("&", "");
                    } else if (o instanceof List) {
                        List<?> list = (List<?>) o;
                        if (!list.isEmpty() && list.get(0) instanceof String) {
                            List<String> list1 = (List<String>) list;
                            List<String> list2 = new ArrayList<String>();
                            for (String s : list1) {
                                list2.add(s.replaceAll("&", ""));
                            }
                            o = list2;
                        }
                    }
                    getObject_Tab.put(i + "." + key, o);
                }
                getTeamSize++;
                getSectionsItems.clear();
            }
        }

        try {
            yamlConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getTeamSize = 0;
}
