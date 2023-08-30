package de.emir.main;


import de.emir.utils.cycle.KitData;
import de.emir.utils.cycle.MapData;
import de.emir.utils.forcemap.ForceMapData;
import de.emir.utils.inv.InvData;
import de.emir.utils.nick.NickData;
import de.emir.utils.sort.SortData;
import de.emir.utils.stats.StatsData;

public class Loader {
    public void loadFileData() {
        (new InvData()).loadData_Inv();
        (new NickData()).loadData_NickNames();
        (new NickData()).loadData_NickSettings(); //Bugs
        (new FileManager()).loadData_Chat();
        (new FileManager()).loadData_Tab();
        (new StatsData()).loadData_stats();
        (new ForceMapData()).loadData_ForceMap();
        (new MapData()).loadData_MapSettings();
        (new MapData()).loadData_Map_value();
        (new KitData()).loadData_Kit();
        (new KitData()).loadData_Kit_value();
        (new FileManager()).loadDefaultMySQLFile();
        (new SortData()).loadData_sort();
    }
}
