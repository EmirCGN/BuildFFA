package de.emir.utils;

import java.util.ArrayList;
import java.util.List;

import de.emir.main.BuildFFA;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
    BuildFFA plugin = BuildFFA.instance;

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

    public ItemStack createItemWithLore(DataSaver ds, String path, int amount) {
        ItemStack i = new ItemStack(ds.getInt(path + ".id").intValue(), amount, ds.getShort(path + ".subid"));
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(ds.getString(path + ".name") + "");
        List<String> lore = ds.getStringList(path + ".lore");
        m.setLore(lore);
        i.setItemMeta(m);
        return i;
    }

    public ItemStack createItemWithReplacement(DataSaver ds, String path, int amount, String oldR, String newR) {
        ItemStack i = new ItemStack(ds.getInt(path + ".id").intValue(), amount, ds.getShort(path + ".subid"));
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(ds.getString(path + ".name").replace(oldR, newR));
        List<String> lore = ds.getStringList(path + ".lore");
        List<String> list = new ArrayList<String>();
        for (String s : lore)
            list.add(s.replace(oldR, newR));
        m.setLore(list);
        i.setItemMeta(m);
        return i;
    }

    public ItemStack createItemI(Material mat, int anzahl, int subid, String displayname) {
        short neuesubid = (short)subid;
        ItemStack i = new ItemStack(mat, anzahl, neuesubid);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(displayname);
        i.setItemMeta(m);
        return i;
    }

    public ItemStack createItemII(int id, int anzahl, int subid, String displayname) {
        short neuesubid = (short)subid;
        ItemStack i = new ItemStack(id, anzahl, neuesubid);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(displayname);
        i.setItemMeta(m);
        return i;
    }
}
