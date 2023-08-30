package de.emir.utils.stats;

import java.util.List;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.sql.MySQLEnum;
import de.emir.sql.MySQLMethods;
import de.emir.utils.DataSaver;
import de.emir.utils.InvAnimation;
import de.emir.utils.ItemBuilder;
import de.emir.utils.nick.Nick;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class StatsInv {
    private DataSaver ds_stats = new DataSaver(StatsData.getObject_stats);

    private ItemBuilder ib = new ItemBuilder();

    public void createStatsInv(Player p, String name) {
        int size = this.ds_stats.getInt("inv.settings.size1").intValue();
        if (Nick.isNicked.containsKey(p)) {
            if (name != Nick.getOldName.get(p.getUniqueId().toString()))
                size = 27;
        } else if (name != p.getName()) {
            size = 27;
        }
        Inventory inv = Bukkit.createInventory(null, size, this.ds_stats.getString("inv.settings.name1"));
        if (!this.ds_stats.getBoolean("inv.settings.animation").booleanValue())
            BuildFFACore.instance.setGlasInInventory(inv, this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue());
        if (name.equals(p.getName())) {
            inv.setItem(this.ds_stats.getInt("inv.items.death.pos").intValue(), this.ib.createItemWithReplacement(this.ds_stats, "inv.items.death", 1, "%VALUE%", BuildFFA.mysqlMethods
                    .loadIntFromCache(p.getUniqueId().toString(), BuildFFA.getMySQLTable, "death") + ""));
            inv.setItem(this.ds_stats.getInt("inv.items.kills.pos").intValue(), this.ib.createItemWithReplacement(this.ds_stats, "inv.items.kills", 1, "%VALUE%", BuildFFA.mysqlMethods
                    .loadIntFromCache(p.getUniqueId().toString(), BuildFFA.getMySQLTable, "kills") + ""));
            inv.setItem(this.ds_stats.getInt("inv.items.points.pos").intValue(), this.ib.createItemWithReplacement(this.ds_stats, "inv.items.points", 1, "%VALUE%", BuildFFA.mysqlMethods
                    .loadIntFromCache(p.getUniqueId().toString(), BuildFFA.getMySQLTable, "points") + ""));
            inv.setItem(this.ds_stats.getInt("inv.items.kd.pos").intValue(), this.ib
                    .createItemWithReplacement(this.ds_stats, "inv.items.kd", 1, "%VALUE%", BuildFFACore.instance.getKDFromUUID(p.getUniqueId().toString()) + ""));
            if (this.ds_stats.getInt("inv.items.reset.pos").intValue() < size)
                inv.setItem(this.ds_stats.getInt("inv.items.reset.pos").intValue(), this.ib.createItemWithLore(this.ds_stats, "inv.items.reset", BuildFFA.mysqlMethods
                        .loadIntFromCache(p.getUniqueId().toString(), "playerTable", "srtokens").intValue()));
        } else {
            inv.setItem(this.ds_stats.getInt("inv.items.death.pos").intValue(), this.ib.createItemWithReplacement(this.ds_stats, "inv.items.death", 1, "%VALUE%", BuildFFA.mysqlMethods
                    .loadIntFromMySQL(MySQLEnum.NAME, name, BuildFFA.getMySQLTable, "death") + ""));
            inv.setItem(this.ds_stats.getInt("inv.items.kills.pos").intValue(), this.ib.createItemWithReplacement(this.ds_stats, "inv.items.kills", 1, "%VALUE%", BuildFFA.mysqlMethods
                    .loadIntFromMySQL(MySQLEnum.NAME, name, BuildFFA.getMySQLTable, "kills") + ""));
            inv.setItem(this.ds_stats.getInt("inv.items.points.pos").intValue(), this.ib.createItemWithReplacement(this.ds_stats, "inv.items.points", 1, "%VALUE%", BuildFFA.mysqlMethods
                    .loadIntFromMySQL(MySQLEnum.NAME, name, BuildFFA.getMySQLTable, "points") + ""));
            inv.setItem(this.ds_stats.getInt("inv.items.kd.pos").intValue(), this.ib
                    .createItemWithReplacement(this.ds_stats, "inv.items.kd", 1, "%VALUE%", BuildFFACore.instance.getKD(name) + ""));
            if (this.ds_stats.getInt("inv.items.reset.pos").intValue() < size)
                inv.setItem(this.ds_stats.getInt("inv.items.reset.pos").intValue(), this.ib.createItemWithLore(this.ds_stats, "inv.items.reset", BuildFFA.mysqlMethods
                        .loadIntFromCache(p.getUniqueId().toString(), "playerTable", "srtokens").intValue()));
        }
        if (this.ds_stats.getInt("inv.items.top.pos").intValue() < size) {
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
            SkullMeta meta = (SkullMeta)item.getItemMeta();
            meta.setDisplayName(this.ds_stats.getString("inv.items.top.name"));
            List<String> lore = this.ds_stats.getStringList("inv.items.top.lore");
            meta.setLore(lore);
            List<String> list = (new MySQLMethods()).getTopValues(10, "name", BuildFFA.getMySQLTable, "points");
            List<String> metaList = meta.getLore();
            for (int i = 0; i < 10; i++) {
                if (list.size() > i)
                    metaList.add(this.ds_stats.getString("inv.items.top.template")
                            .replace("%ID%", "" + Integer.valueOf(i + 1))
                            .replace("%NAME%", list.get(i)));
            }
            meta.setLore(metaList);
            String playerName = p.getName();
            if (Nick.isNicked.containsKey(p))
                playerName = (String) Nick.getOldName.get(p.getUniqueId().toString());
            meta.setOwner(playerName);
            item.setItemMeta((ItemMeta)meta);
            inv.setItem(this.ds_stats.getInt("inv.items.top.pos").intValue(), item);
        }
        if (this.ds_stats.getInt("inv.items.buyreset.pos").intValue() < size)
            inv.setItem(this.ds_stats.getInt("inv.items.buyreset.pos").intValue(), this.ib
                    .createItemWithLore(this.ds_stats, "inv.items.buyreset", 1));
        if (this.ds_stats.getBoolean("inv.settings.animation").booleanValue()) {
            new InvAnimation(p, inv, this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_stats.getInt("inv.settings.glascolorSEC").intValue());
        } else {
            p.openInventory(inv);
        }
    }

    public void createStatsResetInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, this.ds_stats.getInt("inv.settings.size2").intValue(), this.ds_stats
                .getString("inv.settings.name2"));
        if (!this.ds_stats.getBoolean("inv.settings.animation").booleanValue())
            BuildFFACore.instance.setGlasInInventory(inv, this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue());
        inv.setItem(this.ds_stats.getInt("inv.items.execute.pos").intValue(), this.ib.createItemWithLore(this.ds_stats, "inv.items.execute", 1));
        inv.setItem(this.ds_stats.getInt("inv.items.cancel.pos").intValue(), this.ib.createItemWithLore(this.ds_stats, "inv.items.cancel", 1));
        if (this.ds_stats.getBoolean("inv.settings.animation").booleanValue()) {
            new InvAnimation(p, inv, this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_stats.getInt("inv.settings.glascolorSEC").intValue());
        } else {
            p.openInventory(inv);
        }
    }

    public void createBuyTokensInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, this.ds_stats.getInt("inv.settings.size3").intValue(), this.ds_stats
                .getString("inv.settings.name3"));
        if (!this.ds_stats.getBoolean("inv.settings.animation").booleanValue())
            BuildFFACore.instance.setGlasInInventory(inv, this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue());
        inv.setItem(this.ds_stats.getInt("inv.items.buy.pos").intValue(), this.ib
                .createItemWithReplacement(this.ds_stats, "inv.items.buy", 1, "", ""));
        inv.setItem(this.ds_stats.getInt("inv.items.cancel.pos").intValue(), this.ib.createItemWithLore(this.ds_stats, "inv.items.cancel", 1));
        if (this.ds_stats.getBoolean("inv.settings.animation").booleanValue()) {
            new InvAnimation(p, inv, this.ds_stats.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_stats.getInt("inv.settings.glascolorSEC").intValue());
        } else {
            p.openInventory(inv);
        }
    }
}