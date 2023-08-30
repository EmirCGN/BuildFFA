package de.emir.utils.forcemap;

import de.emir.main.BuildFFACore;
import de.emir.utils.DataSaver;
import de.emir.utils.InvAnimation;
import de.emir.utils.ItemBuilder;
import de.emir.utils.cycle.KitData;
import de.emir.utils.cycle.MapData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ForceMapInv {
    private DataSaver ds_forcemap = new DataSaver(ForceMapData.getObject_ForceMap);

    private DataSaver ds_map = new DataSaver(MapData.getObject_Map);

    private DataSaver ds_kit = new DataSaver(KitData.getObject_Kit);

    private ItemBuilder ib = new ItemBuilder();

    public void createForceMainInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, this.ds_forcemap.getString("inv.settings.name1"));
        if (this.ds_forcemap.getBoolean("inv.settings.animation").booleanValue() == true)
            BuildFFACore.instance.setGlasInInventory(inv, this.ds_forcemap.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_forcemap.getInt("inv.settings.glascolorSEC").intValue());
        if (this.ds_forcemap.getBoolean("inv.items.kit.enabled").booleanValue())
            inv.setItem(this.ds_forcemap.getInt("inv.items.kit.pos").intValue(), this.ib.createItemWithLore(this.ds_forcemap, "inv.items.kit", 1));
        if (this.ds_forcemap.getBoolean("inv.items.map.enabled").booleanValue())
            inv.setItem(this.ds_forcemap.getInt("inv.items.map.pos").intValue(), this.ib.createItemWithLore(this.ds_forcemap, "inv.items.map", 1));
        if (this.ds_forcemap.getBoolean("inv.settings.animation").booleanValue()) {
            new InvAnimation(p, inv, this.ds_forcemap.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_forcemap.getInt("inv.settings.glascolorSEC").intValue());
        } else {
            p.openInventory(inv);
        }
    }

    public void createForceMapInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, this.ds_forcemap.getString("inv.settings.name2"));
        BuildFFACore.instance.setGlasInInventory(inv, this.ds_forcemap.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_forcemap.getInt("inv.settings.glascolorSEC").intValue());
        for (int i = 0; i < this.ds_map.getStringList("List.enabled").size(); i++)
            inv.setItem(i, this.ib.createItemWithReplacement(this.ds_forcemap, "inv.items.defaultmap", 1, "%NAME%", this.ds_map
                    .getStringList("List.enabled").get(i)));
        p.openInventory(inv);
    }

    public void createForceKitInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, this.ds_forcemap.getString("inv.settings.name3"));
        BuildFFACore.instance.setGlasInInventory(inv, this.ds_forcemap.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_forcemap.getInt("inv.settings.glascolorSEC").intValue());
        for (int i = 0; i < this.ds_kit.getStringList("List.enabled").size(); i++)
            inv.setItem(i, this.ib.createItemWithReplacement(this.ds_forcemap, "inv.items.defaultkit", 1, "%NAME%", this.ds_kit
                    .getStringList("List.enabled").get(i)));
        p.openInventory(inv);
    }
}