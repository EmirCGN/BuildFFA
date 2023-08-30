package de.emir.utils.nick;

import de.emir.main.BuildFFA;
import de.emir.sql.MySQLEnum;
import de.emir.utils.DataSaver;
import org.bukkit.event.Listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NickManager implements Listener {
    private NickInv inv = new NickInv();

    private DataSaver ds_nicksettings = new DataSaver(NickData.getObject_nicksettings);

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (e.getAction() == InventoryAction.NOTHING)
            return;
        if (e.getCurrentItem() == null)
            return;
        if (e.getClickedInventory().getName()
                .equals(this.ds_nicksettings.getString("inv.settings.name"))) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName()
                    .equals(this.ds_nicksettings.getString("inv.items.autonickitem.name"))) {
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                if (BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "autonick").intValue() == 0) {
                    BuildFFA.mysqlMethods.setIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "autonick", 1);
                    this.inv.updateAutonick(e.getClickedInventory(), p, 1);
                } else {
                    BuildFFA.mysqlMethods.setIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "autonick", 0);
                    this.inv.updateAutonick(e.getClickedInventory(), p, 0);
                }
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName()
                    .equals(this.ds_nicksettings.getString("inv.items.premiumitem.name"))) {
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                if (BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "premiumnick").intValue() == 0) {
                    BuildFFA.mysqlMethods.setIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "premiumnick", 1);
                    this.inv.updatePremiumnick(e.getClickedInventory(), 1);
                } else {
                    BuildFFA.mysqlMethods.setIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "premiumnick", 0);
                    this.inv.updatePremiumnick(e.getClickedInventory(), 0);
                }
                return;
            }
        }
    }
}
