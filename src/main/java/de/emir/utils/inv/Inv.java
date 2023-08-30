package de.emir.utils.inv;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.utils.DataSaver;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.emir.utils.ItemBuilder;
import de.emir.utils.cycle.Kit;
import de.emir.utils.cycle.Map;
import de.emir.utils.cycle.MapData;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Inv {
    private DataSaver ds_inv;

    private ItemBuilder ib;

    public static Inv instance;

    public Inv() {
        this.ds_inv = new DataSaver(InvData.getObject_Inv);
        this.ib = new ItemBuilder();
        instance = this;
    }

    public void setInventory(Player p) {
        PlayerInventory playerInventory = p.getInventory();
        playerInventory.clear();
        p.getInventory().setArmorContents(null);
        if (this.ds_inv.getBoolean("inv.items.stats.enabled").booleanValue())
            playerInventory.setItem(this.ds_inv.getInt("inv.items.stats.pos").intValue(), this.ib.createItemWithLore(this.ds_inv, "inv.items.stats", 1));
        if (this.ds_inv.getBoolean("inv.items.random.enabled").booleanValue())
            playerInventory.setItem(this.ds_inv.getInt("inv.items.random.pos").intValue(), this.ib.createItemWithLore(this.ds_inv, "inv.items.random", 1));
        if (p.hasPermission(BuildFFA.getPremissionPrefix + ".forcemap") && this.ds_inv.getBoolean("inv.items.forcemap.enabled").booleanValue())
            playerInventory.setItem(this.ds_inv.getInt("inv.items.forcemap.pos").intValue(), this.ib
                    .createItemWithLore(this.ds_inv, "inv.items.forcemap", 1));
        if (p.hasPermission(BuildFFA.getPremissionPrefix + ".nick") && this.ds_inv.getBoolean("inv.items.nick.enabled").booleanValue())
            playerInventory.setItem(this.ds_inv.getInt("inv.items.nick.pos").intValue(), this.ib.createItemWithLore(this.ds_inv, "inv.items.nick", 1));
        for (PotionEffect effect : p.getActivePotionEffects()) {
            PotionEffectType type = effect.getType();
            p.removePotionEffect(type);
        }
        p.setFireTicks(0);
    }

    public void executeTeleportPlayerToRndLocation(Player p) {
        Map map = new Map();
        Kit kit = new Kit(BuildFFA.instance);
        if (map.getActualMapName() == null)
            return;
        if (!MapData.getObject_Map_values.containsKey(map.getActualMapName()))
            return;
        DataSaver ds = new DataSaver((HashMap) MapData.getObject_Map_values.get(map.getActualMapName()));
        if (!ds.contains(map.getActualMapName() + ".randomlist"))
            return;
        if (ds.contains(map.getActualMapName() + ".randomlist")) {
            List<Location> list = ds.getLocationList(map.getActualMapName() + ".randomlist");
            if (list.size() != 0) {
                Random r = new Random();
                int random = r.nextInt(list.size());
                Location loc = list.get(random);
                p.teleport(loc);
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                p.spigot().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1, 1, 0.0F, 0.0F, 0.0F, 0.1F, 4, 8);
                if (BuildFFA.invsort) {
                    kit.setSortedKit(p);
                } else {
                    kit.setKit(p, kit.getActualKitName());
                }
                BuildFFACore.getPlayerInGame.add(p);
            }
        }
    }
}
