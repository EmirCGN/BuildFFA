package de.emir.utils.nick;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.sql.MySQLEnum;
import de.emir.utils.DataSaver;
import de.emir.utils.InvAnimation;
import de.emir.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NickInv {
    private DataSaver ds_nicksettings = new DataSaver(NickData.getObject_nicksettings);
    private ItemBuilder ib = new ItemBuilder();

    public void createNickNameSettingsInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, this.ds_nicksettings.getInt("inv.settings.size").intValue(), this.ds_nicksettings.getString("inv.settings.name").replaceAll("&", "§"));
        if (!this.ds_nicksettings.getBoolean("inv.settings.animation").booleanValue()) {
            BuildFFACore.instance.setGlasInInventory(inv, this.ds_nicksettings.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_nicksettings.getInt("inv.settings.glascolorSEC").intValue());
        }
        String normal = this.ds_nicksettings.getString("settings.normalstate").replaceAll("&", "§");
        String premium = this.ds_nicksettings.getString("settings.premiumstate").replaceAll("&", "§");
        String act = this.ds_nicksettings.getString("settings.activated").replaceAll("&", "§");
        String deact = this.ds_nicksettings.getString("settings.deactivated").replaceAll("&", "§");

        if (BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "autonick").intValue() == 0) {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.autonickitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.autonickitem", 1, "%STATE%", deact));
        } else {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.autonickitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.autonickitem", 1, "%STATE%", act));
        }

        if (BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "premiumnick").intValue() == 0) {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.premiumitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.premiumitem", 1, "%STATE%", normal));
        } else {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.premiumitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.premiumitem", 1, "%STATE%", premium));
        }

        if (this.ds_nicksettings.getBoolean("inv.settings.animation").booleanValue()) {
            new InvAnimation(p, inv, this.ds_nicksettings.getInt("inv.settings.glascolorPRIM").intValue(), this.ds_nicksettings.getInt("inv.settings.glascolorSEC").intValue());
        } else {
            p.openInventory(inv);
        }
    }

    public void updateAutonick(Inventory inv, Player p, int i) {
        String act = this.ds_nicksettings.getString("settings.activated").replaceAll("&", "§");
        String deact = this.ds_nicksettings.getString("settings.deactivated").replaceAll("&", "§");

        if (i == 0) {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.autonickitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.autonickitem", 1, "%STATE%", deact));
        } else {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.autonickitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.autonickitem", 1, "%STATE%", act));
        }
    }

    public void updatePremiumnick(Inventory inv, int i) {
        String normal = this.ds_nicksettings.getString("settings.normalstate").replaceAll("&", "§");
        String premium = this.ds_nicksettings.getString("settings.premiumstate").replaceAll("&", "§");

        if (i == 0) {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.premiumitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.premiumitem", 1, "%STATE%", normal));
        } else {
            inv.setItem(this.ds_nicksettings.getInt("inv.items.premiumitem.pos").intValue(), this.ib.createItemWithReplacement(this.ds_nicksettings, "inv.items.premiumitem", 1, "%STATE%", premium));
        }
    }
}
