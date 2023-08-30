package de.emir.utils.stats;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.sql.MySQLbuildffaTable;
import de.emir.sql.MySQLcoinsTable;
import de.emir.utils.DataSaver;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatsManager implements Listener {
    BuildFFA plugin;

    private BuildFFACore core;

    private DataSaver ds_stats = new DataSaver(StatsData.getObject_stats);

    private StatsInv inv = new StatsInv();

    private MySQLbuildffaTable buildffaTable = new MySQLbuildffaTable();

    private MySQLcoinsTable coinsTable = new MySQLcoinsTable();
}
