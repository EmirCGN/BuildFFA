package de.emir.utils.inv;

import java.util.ArrayList;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.utils.DataSaver;
import de.emir.utils.forcemap.ForceMapInv;
import de.emir.utils.nick.Nick;
import de.emir.utils.nick.NickData;
import de.emir.utils.nick.NickInv;
import de.emir.utils.stats.StatsInv;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InvManager implements Listener {
    private DataSaver ds_join = new DataSaver(InvData.getObject_Inv);

    private DataSaver ds_nick = new DataSaver(NickData.getObject_nicksettings);

    private StatsInv inv_stats = new StatsInv();

    private ForceMapInv inv_forcemap = new ForceMapInv();

    public static ArrayList<Player> delay = new ArrayList<Player>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (BuildFFACore.getPlayerInEditMode.contains(p))
            return;
        if (e.getAction() == InventoryAction.NOTHING)
            return;
        if (e.getCurrentItem() == null)
            return;
        if (e.getClickedInventory() == p.getInventory() &&
                !BuildFFACore.getPlayerInGame.contains(p)) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (p.getItemInHand() == null)
            return;
        if (!p.getItemInHand().hasItemMeta())
            return;
        if (p.getItemInHand().getItemMeta().getDisplayName() == null)
            return;
        String stats = this.ds_join.getString("inv.items.stats.name").replaceAll("&", "§");
                String random = this.ds_join.getString("inv.items.random.name").replaceAll("&", "§");
                        String forcemap = this.ds_join.getString("inv.items.forcemap.name").replaceAll("&", "§");
                                String nicktool = this.ds_join.getString("inv.items.nick.name").replaceAll("&", "§");
        if (p.getItemInHand() != null && (
                e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e
                        .getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK))
            if (p.getItemInHand().getItemMeta().getDisplayName().equals(stats)) {
                if (Nick.isNicked.containsKey(p)) {
                    this.inv_stats.createStatsInv(p, (String) Nick.getOldName.get(p.getUniqueId().toString()));
                } else {
                    this.inv_stats.createStatsInv(p, p.getName());
                }
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                e.setCancelled(true);
            } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(random)) {
                e.setCancelled(true);
                Inv.instance.executeTeleportPlayerToRndLocation(p);
            } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(forcemap)) {
                e.setCancelled(true);
                this.inv_forcemap.createForceMainInv(p);
                p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
            } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(nicktool)) {
                e.setCancelled(true);
                if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (!delay.contains(p)) {
                        if (Nick.isNicked.containsKey(p)) {
                            p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                            Nick.instance.unnickPlayer(p, false);
                        } else {
                            p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                            Nick.instance.nickPlayer(p);
                        }
                        delay.add(p);
                        (new BukkitRunnable() {
                            public void run() {
                                InvManager.delay.remove(p);
                            }
                        }).runTaskLater((Plugin) BuildFFA.instance, 100L);
                    } else {
                        p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
                        p.sendMessage(BuildFFACore.instance.translateString(this.ds_nick.getString("settings.waitfornick")));
                    }
                } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    (new NickInv()).createNickNameSettingsInv(p);
                    p.playSound(p.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                }
            } else {
                return;
            }
    }
}