package de.emir.main;

import de.emir.sql.MySQLEnum;
import de.emir.utils.InvBackground;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuildFFACore {
    private FileConfiguration cfg = BuildFFA.instance.getConfig();
    private String prefix;
    public static BuildFFACore instance;
    public BuildFFACore() {
        try {
            this.prefix = this.cfg.getString("messages.prefix").replaceAll("&", "$");
        } catch (Exception e) {
            this.prefix = "";
        }
        instance = this;
    }
    public static ArrayList<Player> getPlayerInGame = new ArrayList<Player>();

    public static ArrayList<Player> getPlayerInEditMode = new ArrayList<Player>();

    public static ArrayList<Block> getBlocksPlaced = new ArrayList<Block>();

    public String translateString(String s) {
        return this.prefix + s;
    }

    public void setGlasInInventory(Inventory inv, int prim, int sec) {
        ItemStack itemPrim = createItem(Material.STAINED_GLASS_PANE, 1, prim, " ");
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, itemPrim);
        int[] array = null;
        if (inv.getSize() == 9) {
            array = InvBackground.arraySecOne;
        } else if (inv.getSize() == 18) {
            array = InvBackground.arraySecTwo;
        } else if (inv.getSize() == 27) {
            array = InvBackground.arraySecThree;
        } else if (inv.getSize() == 36) {
            array = InvBackground.arraySecFour;
        } else if (inv.getSize() == 45) {
            array = InvBackground.arraySecFive;
        } else if (inv.getSize() == 54) {
            array = InvBackground.arraySecSix;
        }
        ItemStack itemSec = createItem(Material.STAINED_GLASS_PANE, 1, sec, " ");
        for (int j = 0; j < array.length; j++)
            inv.setItem(array[j], itemSec);
    }

    public ItemStack createItem(Material material, int anzahl, int subid, String displayname) {
        short neuesubid = (short)subid;
        ItemStack i = new ItemStack(material, anzahl, neuesubid);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(displayname);
        m.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        m.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        m.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        m.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
        i.setItemMeta(m);
        return i;
    }

    public static void saveFile(FileConfiguration file, File FILE) {
        file.options().copyDefaults(true);
        try {
            file.save(FILE);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static void saveItemInFile(FileConfiguration file, String path, int pos, int id, int subid, String name, List<String> lore, Boolean tru) {
        file.addDefault(path + ".name", name);
        file.addDefault(path + ".pos", Integer.valueOf(pos));
        file.addDefault(path + ".id", Integer.valueOf(id));
        file.addDefault(path + ".subid", Integer.valueOf(subid));
        file.addDefault(path + ".lore", lore);
        file.addDefault(path + ".enchant", tru);
    }

    public static void saveItemInFileLess(FileConfiguration file, String path, int pos, String name, List<String> lore, Boolean tru) {
        file.addDefault(path + ".name", name);
        file.addDefault(path + ".pos", Integer.valueOf(pos));
        file.addDefault(path + ".lore", lore);
        file.addDefault(path + ".enchant", tru);
    }

    public static void saveHeadInFiles(FileConfiguration file, String path, int pos, String name, List<String> lore, Boolean tru, String skullname) {
        file.addDefault(path + ".name", name);
        file.addDefault(path + ".pos", Integer.valueOf(pos));
        file.addDefault(path + ".skullname", skullname);
        file.addDefault(path + ".lore", lore);
        file.addDefault(path + ".enchant", tru);
    }

    public static void saveLocationInFiles(FileConfiguration file, String path, int pos, String name, List<String> lore, Boolean tru, String skullname) {
        file.addDefault(path + ".world", name);
        file.addDefault(path + ".x", Integer.valueOf(pos));
        file.addDefault(path + ".y", skullname);
        file.addDefault(path + ".lore", lore);
        file.addDefault(path + ".enchant", tru);
    }

    public String getKD(String name) {
        double d;
        int kills, death;
        try {
            kills = BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.NAME, name, BuildFFA.getMySQLTable, "kills").intValue();
            death = BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.NAME, name, BuildFFA.getMySQLTable, "death").intValue();
        } catch (Exception e) {
            kills = 0;
            death = 0;
        }
        if (kills == 0 && death == 0) {
            d = 0.0D;
        } else if (kills == 0) {
            d = 0.0D;
        } else if (death == 0) {
            d = kills / 1.0D;
        } else {
            d = kills / death;
        }
        double round = Math.round(d * 100.0D) / 100.0D;
        String kd = "" + round;
        if (kills == 0 && death == 0)
            return d + "";
        return kd;
    }

    public String getKDFromUUID(String uuid) {
        double d;
        int kills, death;
        try {
            kills = BuildFFA.mysqlMethods.loadIntFromCache(uuid, BuildFFA.getMySQLTable, "kills").intValue();
            death = BuildFFA.mysqlMethods.loadIntFromCache(uuid, BuildFFA.getMySQLTable, "death").intValue();
        } catch (Exception e) {
            kills = 0;
            death = 0;
        }
        if (kills == 0 && death == 0) {
            d = 0.0D;
        } else if (kills == 0) {
            d = 0.0D;
        } else if (death == 0) {
            d = kills / 1.0D;
        } else {
            d = kills / death;
        }
        double round = Math.round(d * 100.0D) / 100.0D;
        String kd = "" + round;
        if (kills == 0 && death == 0)
            return d + "";
        return kd;
    }

    public String getDate() {
        Date datetoday = new Date();
        String date = (new SimpleDateFormat("dd/MM/yyyy")).format(datetoday);
        return date;
    }
}


