package de.emir.utils.cycle;

import java.util.ArrayList;
import java.util.List;

import de.emir.main.BuildFFA;
import de.emir.utils.DataSaver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class CycleManager {
    private DataSaver ds_map;

    private DataSaver ds_kit;

    private Integer getMapSetting;

    private Integer getKitSetting;

    private Integer getMapTime;

    private Integer getKitTime;

    private Map map;

    private Kit kit;

    private Actionbar ab;

    public CycleManager() {
        this.ds_map = new DataSaver(MapData.getObject_Map);
        this.ds_kit = new DataSaver(KitData.getObject_Kit);
        this.getMapSetting = this.ds_map.getInt("Map.MapSystem");
        this.getKitSetting = this.ds_kit.getInt("Kits.KitSystem");
        this.getMapTime = this.ds_map.getInt("Map.time");
        this.getKitTime = this.ds_kit.getInt("Kits.time");
        this.map = new Map();
        this.kit = new Kit(BuildFFA.instance);
        this.ab = new Actionbar();
        startCycleManager();
    }

    private void startCycleManager() {
        final List<String> getMaps = this.ds_map.getStringList("List.enabled");
        final List<String> getKits = this.ds_kit.getStringList("List.enabled");
        if (getMaps.isEmpty() || getKits.isEmpty())
            return;
        if (this.getMapSetting.intValue() == 0) {
            if (getMaps.size() > 1) {
                this.map.switchMap(this.map.getRandomMap(getMaps));
                Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.getPlugin(BuildFFA.class), new Runnable() {
                    int timer = CycleManager.this.getMapTime.intValue();

                    public void run() {
                        if (this.timer == 0) {
                            List<String> last = new ArrayList<String>();
                            for (String s : getMaps) {
                                if (s != CycleManager.this.map.getActualMapName())
                                    last.add(s);
                            }
                            CycleManager.this.map.switchMap(CycleManager.this.map.getRandomMap(last));
                            this.timer = CycleManager.this.getMapTime.intValue();
                        } else {
                            this.timer--;
                            CycleManager.this.ab.sendActionBarBroadCaster(this.timer);
                        }
                    }
                },  0L, 20L);
            } else {
                this.map.switchMap(this.map.getRandomMap(getMaps));
            }
        } else if (this.getMapSetting.intValue() == 1) {
            this.map.switchMap(this.map.getRandomMap(getMaps));
        } else if (this.getMapSetting.intValue() == 2) {
            this.map.switchMap(this.ds_map.getString("Map.default"));
        }
        if (BuildFFA.invsort);
        if (this.getKitSetting.intValue() == 0) {
            if (getKits.size() > 1) {
                this.kit.switchKit(this.kit.getRandomKit(getKits));
                Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.getPlugin(BuildFFA.class), new Runnable() {
                    int timer = CycleManager.this.getKitTime.intValue();

                    public void run() {
                        if (this.timer == 0) {
                            List<String> last = new ArrayList<String>();
                            for (String s : getKits) {
                                if (s != CycleManager.this.kit.getActualKitName())
                                    last.add(s);
                            }
                            CycleManager.this.kit.switchKit(CycleManager.this.kit.getRandomKit(last));
                            this.timer = CycleManager.this.getKitTime.intValue();
                        } else {
                            this.timer--;
                        }
                    }
                },  0L, 20L);
            } else {
                this.kit.switchKit(this.kit.getRandomKit(getKits));
            }
        } else if (this.getKitSetting.intValue() == 1) {
            this.kit.switchKit(this.kit.getRandomKit(getKits));
        } else if (this.getKitSetting.intValue() == 2) {
            this.kit.switchKit(this.ds_kit.getString("Kits.Defaultkit"));
        }
    }
}