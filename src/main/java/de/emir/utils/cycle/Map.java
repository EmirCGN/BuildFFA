package de.emir.utils.cycle;

import de.emir.main.BuildFFACore;
import de.emir.utils.DataSaver;
import de.emir.utils.RegisterMob;
import de.emir.utils.TitleAPI;
import de.emir.utils.inv.Inv;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Map {
    public DataSaver ds_map = new DataSaver(MapData.getObject_Map);

    public static HashMap<GetEnumCycleSystem, String> getMap = new HashMap<GetEnumCycleSystem, String>();

    public void switchMap(String name) {
        if (MapData.getObject_Map_values.containsKey(name)) {
            DataSaver ds = new DataSaver(MapData.getObject_Map_values.get(name));
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (!BuildFFACore.getPlayerInEditMode.contains(all)) {
                    try {
                        all.teleport(ds.getLocation(name + ".Spawn"));
                    } catch (Exception exception) {}
                    all.playSound(all.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                    if (!all.hasMetadata("spec"))
                        Inv.instance.setInventory(all);
                    TitleAPI.sendTitle(all, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), this.ds_map.getString("Title.header").replaceAll("&", "§").replace("%NAME%", name), this.ds_map
                            .getString("Title.footer").replaceAll("&", "§").replace("%NAME%", name));
                                    BuildFFACore.getPlayerInGame.remove(all);
                    all.setHealth(all.getMaxHealth());
                    all.setFoodLevel(20);
                    all.setFireTicks(0);
                }
            }
            if (getMap.containsKey(GetEnumCycleSystem.MAP)) {
                getMap.remove(GetEnumCycleSystem.MAP);
                getMap.put(GetEnumCycleSystem.MAP, name);
            } else {
                getMap.put(GetEnumCycleSystem.MAP, name);
            }
            new RegisterMob();
        }
    }

    public String getRandomMap(List<String> list) {
        Random r = new Random();
        int random = r.nextInt(list.size());
        String s = list.get(random);
        return s;
    }

    public String getActualMapName() {
        return getMap.get(GetEnumCycleSystem.MAP);
    }

    public void tpPlayerToActualSpawnLocation(Player p) {
        String name = getActualMapName();
        if (MapData.getObject_Map_values.containsKey(name)) {
            DataSaver ds = new DataSaver(MapData.getObject_Map_values.get(name));
            p.teleport(ds.getLocation(name + ".Spawn"));
        }
    }
}
