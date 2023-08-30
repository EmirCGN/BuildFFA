package de.emir.utils.sort;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.sql.MySQLEnum;

import java.util.HashMap;

import de.emir.utils.DataSaver;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class SortManager implements Listener {
    public static DataSaver ds_sort;

    public SortManager() {
        ds_sort = new DataSaver(SortData.getObject_sort);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        try {
            Player p = (Player)e.getWhoClicked();
            if (p.hasMetadata("sort")) {
                if (e.getClickedInventory().getType() == InventoryType.PLAYER || e
                        .getClickedInventory() == p.getInventory()) {
                    e.setCancelled(true);
                    return;
                }
                if (!e.getClickedInventory().getName().equals(ds_sort.getString("inv.settings.name")))
                    p.removeMetadata("sort", (Plugin)BuildFFA.instance);
                if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e
                        .getAction() == InventoryAction.HOTBAR_SWAP)
                    e.setCancelled(true);
            }
        } catch (Exception exception) {}
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (p.hasMetadata("sort"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        try {
            Player p = (Player)event.getPlayer();
            if (p.hasMetadata("sort") && event.getInventory().getName().equals(ds_sort.getString("inv.settings.name"))) {
                if (p.getItemOnCursor() != null)
                    p.setItemOnCursor(null);
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                p.sendMessage(BuildFFACore.instance.translateString(ds_sort.getString("settings.message")));
                int a = -1;
                int b = -1;
                int c = -1;
                int d = -1;
                int e = -1;
                int f = -1;
                int g = -1;
                int h = -1;
                int i = -1;
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                for (int i1 = 0; i1 < 9; i1++) {
                    if (event.getInventory().getItem(i1) != null) {
                        Material mat = event.getInventory().getItem(i1).getType();
                        int id = event.getInventory().getItem(i1).getTypeId();
                        if (id == ds_sort.getInt("settings.sword_id").intValue()) {
                            a = i1;
                            map.put("a", Integer.valueOf(i1));
                        } else if (mat == Material.STICK) {
                            b = i1;
                            map.put("b", Integer.valueOf(i1));
                        } else if (id == ds_sort.getInt("settings.blocks_id").intValue()) {
                            c = i1;
                            map.put("c", Integer.valueOf(i1));
                        } else if (mat == Material.WEB) {
                            d = i1;
                            map.put("d", Integer.valueOf(i1));
                        } else if (mat == Material.GOLDEN_APPLE) {
                            e = i1;
                            map.put("e", Integer.valueOf(i1));
                        } else if (mat == Material.BOW) {
                            f = i1;
                            map.put("f", Integer.valueOf(i1));
                        } else if (mat == Material.ARROW) {
                            g = i1;
                            map.put("g", Integer.valueOf(i1));
                        } else if (id == ds_sort.getInt("settings.extra1_id").intValue()) {
                            h = i1;
                            map.put("h", Integer.valueOf(i1));
                        } else if (id == ds_sort.getInt("settings.extra2_id").intValue()) {
                            i = i1;
                            map.put("i", Integer.valueOf(i1));
                        }
                    }
                }
                for (int i2 = 0; i2 < 9; i2++) {
                    if (!map.containsValue(Integer.valueOf(i2)))
                        if (a == -1) {
                            a = i2;
                        } else if (b == -1) {
                            b = i2;
                        } else if (c == -1) {
                            c = i2;
                        } else if (d == -1) {
                            d = i2;
                        } else if (e == -1) {
                            e = i2;
                        } else if (f == -1) {
                            f = i2;
                        } else if (g == -1) {
                            g = i2;
                        } else if (h == -1) {
                            h = i2;
                        } else if (i == -1) {
                            i = i2;
                        }
                }
                p.removeMetadata("sort", (Plugin) BuildFFA.instance);
                String s = "1:" + a + ";2:" + b + ";3:" + c + ";4:" + d + ";5:" + e + ";6:" + f + ";7:" + g + ";8:" + h + ";9:" + i;
                BuildFFA.mysqlMethods.setStringFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "invsortTable", "buildffa", s);
            }
        } catch (Exception exception) {}
    }
}