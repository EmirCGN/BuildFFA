package de.emir.utils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import de.emir.main.BuildFFA;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class InvAnimation {
    private Player p;

    private Inventory inv;

    private HashMap<Integer, ItemStack> content;

    private ItemBuilder ib;

    private int colorPrim = 0;

    private int colorSec = 7;

    public InvAnimation(Player p, Inventory inv, int prim, int sec) {
        this.p = p;
        this.inv = inv;
        this.ib = new ItemBuilder();
        this.colorPrim = prim;
        this.colorSec = sec;
        createInvContent();
        runAnimation();
    }

    private void runAnimation() {
        if (this.p.hasMetadata("animation")) {
            this.p.playSound(this.p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
            return;
        }
        this.p.setMetadata("animation", (MetadataValue)new FixedMetadataValue((Plugin) BuildFFA.instance, Boolean.valueOf(true)));
        Inventory inv = Bukkit.createInventory(null, this.inv.getSize(), this.inv.getTitle());
        this.p.openInventory(inv);
        if (inv.getSize() == 9) {
            startAnimationForOne(inv);
        } else if (inv.getSize() == 18) {
            startAnimationForTwo(inv);
        } else if (inv.getSize() == 27) {
            startAnimationForThree(inv);
        } else if (inv.getSize() == 36) {
            startAnimationForFour(inv);
        } else if (inv.getSize() == 45) {
            startAnimationForFive(inv);
        } else if (inv.getSize() == 54) {
            startAnimationForSix(inv);
        }
    }

    private void createInvContent() {
        HashMap<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < (this.inv.getContents()).length; i++) {
            if (this.inv.getContents()[i] != null && this.inv.getContents()[i].getType() != Material.STAINED_GLASS_PANE)
                map.put(Integer.valueOf(i), this.inv.getContents()[i]);
        }
        this.content = map;
    }

    private void startAnimationForOne(final Inventory inv) {
        final AtomicInteger count2 = new AtomicInteger();
        count2.set(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.instance, new Runnable() {
            int time = 7;

            ItemStack loading = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorSec, " ");

            ItemStack loading1 = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorPrim, " ");

            public void run() {
                this.time--;
                if (this.time == 5) {
                    inv.setItem(0, this.loading);
                    inv.setItem(1, this.loading1);
                    inv.setItem(7, this.loading1);
                    inv.setItem(8, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 3) {
                    inv.setItem(2, this.loading1);
                    inv.setItem(3, this.loading1);
                    inv.setItem(4, this.loading);
                    inv.setItem(5, this.loading1);
                    inv.setItem(6, this.loading1);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 0) {
                    for (Integer i : InvAnimation.this.content.keySet())
                        inv.setItem(i.intValue(), (ItemStack)InvAnimation.this.content.get(i));
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    InvAnimation.this.p.removeMetadata("animation", (Plugin)BuildFFA.instance);
                    Bukkit.getScheduler().cancelTask(count2.get());
                }
            }
        },0L, 2L));
    }

    private void startAnimationForTwo(final Inventory inv) {
        final AtomicInteger count2 = new AtomicInteger();
        count2.set(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.instance, new Runnable() {
            int time = 7;

            ItemStack loading = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorSec, " ");

            ItemStack loading1 = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorPrim, " ");

            public void run() {
                this.time--;
                if (this.time == 5) {
                    inv.setItem(0, this.loading);
                    inv.setItem(1, this.loading1);
                    inv.setItem(2, this.loading1);
                    inv.setItem(6, this.loading1);
                    inv.setItem(7, this.loading1);
                    inv.setItem(8, this.loading);
                    inv.setItem(9, this.loading);
                    inv.setItem(10, this.loading1);
                    inv.setItem(16, this.loading1);
                    inv.setItem(17, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 3) {
                    inv.setItem(3, this.loading1);
                    inv.setItem(4, this.loading);
                    inv.setItem(5, this.loading1);
                    inv.setItem(11, this.loading1);
                    inv.setItem(12, this.loading1);
                    inv.setItem(13, this.loading);
                    inv.setItem(14, this.loading1);
                    inv.setItem(15, this.loading1);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 0) {
                    for (Integer i : InvAnimation.this.content.keySet())
                        inv.setItem(i.intValue(), (ItemStack)InvAnimation.this.content.get(i));
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    InvAnimation.this.p.removeMetadata("animation", (Plugin)BuildFFA.instance);
                    Bukkit.getScheduler().cancelTask(count2.get());
                }
            }
        },0L, 2L));
    }

    private void startAnimationForThree(final Inventory inv) {
        final AtomicInteger count2 = new AtomicInteger();
        count2.set(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.instance, new Runnable() {
            int time = 7;

            ItemStack loading = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorSec, " ");

            ItemStack loading1 = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorPrim, " ");

            public void run() {
                this.time--;
                if (this.time == 5) {
                    inv.setItem(0, this.loading);
                    inv.setItem(1, this.loading1);
                    inv.setItem(2, this.loading1);
                    inv.setItem(3, this.loading1);
                    inv.setItem(4, this.loading1);
                    inv.setItem(5, this.loading1);
                    inv.setItem(6, this.loading1);
                    inv.setItem(7, this.loading1);
                    inv.setItem(8, this.loading);
                    inv.setItem(9, this.loading);
                    inv.setItem(17, this.loading);
                    inv.setItem(18, this.loading);
                    inv.setItem(19, this.loading1);
                    inv.setItem(20, this.loading1);
                    inv.setItem(21, this.loading1);
                    inv.setItem(22, this.loading1);
                    inv.setItem(23, this.loading1);
                    inv.setItem(24, this.loading1);
                    inv.setItem(25, this.loading1);
                    inv.setItem(26, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 3) {
                    inv.setItem(10, this.loading);
                    inv.setItem(11, this.loading1);
                    inv.setItem(12, this.loading1);
                    inv.setItem(13, this.loading);
                    inv.setItem(14, this.loading1);
                    inv.setItem(15, this.loading1);
                    inv.setItem(16, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 0) {
                    for (Integer i : InvAnimation.this.content.keySet())
                        inv.setItem(i.intValue(), (ItemStack)InvAnimation.this.content.get(i));
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    InvAnimation.this.p.removeMetadata("animation", (Plugin)BuildFFA.instance);
                    Bukkit.getScheduler().cancelTask(count2.get());
                }
            }
        },0L, 2L));
    }

    private void startAnimationForFour(final Inventory inv) {
        final AtomicInteger count2 = new AtomicInteger();
        count2.set(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.instance, new Runnable() {
            int time = 7;

            ItemStack loading = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorSec, " ");

            ItemStack loading1 = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorPrim, " ");

            public void run() {
                this.time--;
                if (this.time == 5) {
                    inv.setItem(0, this.loading);
                    inv.setItem(1, this.loading1);
                    inv.setItem(2, this.loading1);
                    inv.setItem(3, this.loading);
                    inv.setItem(4, this.loading1);
                    inv.setItem(5, this.loading);
                    inv.setItem(6, this.loading1);
                    inv.setItem(7, this.loading1);
                    inv.setItem(8, this.loading);
                    inv.setItem(9, this.loading1);
                    inv.setItem(18, this.loading1);
                    inv.setItem(17, this.loading1);
                    inv.setItem(26, this.loading1);
                    inv.setItem(27, this.loading);
                    inv.setItem(28, this.loading1);
                    inv.setItem(29, this.loading1);
                    inv.setItem(30, this.loading);
                    inv.setItem(31, this.loading1);
                    inv.setItem(32, this.loading);
                    inv.setItem(33, this.loading1);
                    inv.setItem(34, this.loading1);
                    inv.setItem(35, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 3) {
                    inv.setItem(10, this.loading);
                    inv.setItem(11, this.loading1);
                    inv.setItem(12, this.loading1);
                    inv.setItem(13, this.loading);
                    inv.setItem(14, this.loading1);
                    inv.setItem(15, this.loading1);
                    inv.setItem(16, this.loading);
                    inv.setItem(19, this.loading);
                    inv.setItem(20, this.loading1);
                    inv.setItem(21, this.loading1);
                    inv.setItem(22, this.loading);
                    inv.setItem(23, this.loading1);
                    inv.setItem(24, this.loading1);
                    inv.setItem(25, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 0) {
                    for (Integer i : InvAnimation.this.content.keySet())
                        inv.setItem(i.intValue(), (ItemStack)InvAnimation.this.content.get(i));
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    InvAnimation.this.p.removeMetadata("animation", (Plugin)BuildFFA.instance);
                    Bukkit.getScheduler().cancelTask(count2.get());
                }
            }
        },0L, 2L));
    }

    private void startAnimationForFive(final Inventory inv) {
        final AtomicInteger count2 = new AtomicInteger();
        count2.set(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.instance, new Runnable() {
            int time = 9;

            ItemStack loading = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorSec, " ");

            ItemStack loading1 = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorPrim, " ");

            public void run() {
                this.time--;
                if (this.time == 7) {
                    inv.setItem(0, this.loading);
                    inv.setItem(1, this.loading1);
                    inv.setItem(2, this.loading1);
                    inv.setItem(3, this.loading1);
                    inv.setItem(4, this.loading);
                    inv.setItem(5, this.loading1);
                    inv.setItem(6, this.loading1);
                    inv.setItem(7, this.loading1);
                    inv.setItem(8, this.loading);
                    inv.setItem(36, this.loading);
                    inv.setItem(37, this.loading1);
                    inv.setItem(38, this.loading1);
                    inv.setItem(39, this.loading1);
                    inv.setItem(40, this.loading);
                    inv.setItem(41, this.loading1);
                    inv.setItem(42, this.loading1);
                    inv.setItem(43, this.loading1);
                    inv.setItem(44, this.loading);
                    inv.setItem(9, this.loading1);
                    inv.setItem(18, this.loading1);
                    inv.setItem(27, this.loading1);
                    inv.setItem(17, this.loading1);
                    inv.setItem(26, this.loading1);
                    inv.setItem(35, this.loading1);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 5) {
                    inv.setItem(10, this.loading);
                    inv.setItem(11, this.loading1);
                    inv.setItem(12, this.loading1);
                    inv.setItem(13, this.loading);
                    inv.setItem(14, this.loading1);
                    inv.setItem(15, this.loading1);
                    inv.setItem(16, this.loading);
                    inv.setItem(28, this.loading);
                    inv.setItem(29, this.loading1);
                    inv.setItem(30, this.loading1);
                    inv.setItem(31, this.loading);
                    inv.setItem(32, this.loading1);
                    inv.setItem(33, this.loading1);
                    inv.setItem(34, this.loading);
                    inv.setItem(19, this.loading);
                    inv.setItem(25, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 3) {
                    inv.setItem(20, this.loading);
                    inv.setItem(21, this.loading);
                    inv.setItem(22, this.loading1);
                    inv.setItem(23, this.loading);
                    inv.setItem(24, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 0) {
                    for (Integer i : InvAnimation.this.content.keySet())
                        inv.setItem(i.intValue(), (ItemStack)InvAnimation.this.content.get(i));
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    InvAnimation.this.p.removeMetadata("animation", (Plugin)BuildFFA.instance);
                    Bukkit.getScheduler().cancelTask(count2.get());
                }
            }
        },0L, 2L));
    }

    private void startAnimationForSix(final Inventory inv) {
        final AtomicInteger count2 = new AtomicInteger();
        count2.set(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BuildFFA.instance, new Runnable() {
            int time = 9;

            ItemStack loading = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorSec, " ");

            ItemStack loading1 = InvAnimation.this.ib.createItem(Material.STAINED_GLASS_PANE, 1, InvAnimation.this.colorPrim, " ");

            public void run() {
                this.time--;
                if (this.time == 7) {
                    inv.setItem(0, this.loading);
                    inv.setItem(1, this.loading1);
                    inv.setItem(2, this.loading1);
                    inv.setItem(3, this.loading);
                    inv.setItem(4, this.loading);
                    inv.setItem(5, this.loading);
                    inv.setItem(6, this.loading1);
                    inv.setItem(7, this.loading1);
                    inv.setItem(8, this.loading);
                    inv.setItem(9, this.loading1);
                    inv.setItem(18, this.loading1);
                    inv.setItem(27, this.loading1);
                    inv.setItem(36, this.loading1);
                    inv.setItem(17, this.loading1);
                    inv.setItem(26, this.loading1);
                    inv.setItem(35, this.loading1);
                    inv.setItem(44, this.loading1);
                    inv.setItem(45, this.loading);
                    inv.setItem(46, this.loading1);
                    inv.setItem(47, this.loading1);
                    inv.setItem(48, this.loading);
                    inv.setItem(49, this.loading);
                    inv.setItem(50, this.loading);
                    inv.setItem(51, this.loading1);
                    inv.setItem(52, this.loading1);
                    inv.setItem(53, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 5) {
                    inv.setItem(10, this.loading);
                    inv.setItem(11, this.loading1);
                    inv.setItem(12, this.loading1);
                    inv.setItem(13, this.loading1);
                    inv.setItem(14, this.loading1);
                    inv.setItem(15, this.loading1);
                    inv.setItem(16, this.loading);
                    inv.setItem(19, this.loading);
                    inv.setItem(28, this.loading);
                    inv.setItem(25, this.loading);
                    inv.setItem(34, this.loading);
                    inv.setItem(37, this.loading);
                    inv.setItem(38, this.loading1);
                    inv.setItem(39, this.loading1);
                    inv.setItem(40, this.loading1);
                    inv.setItem(41, this.loading1);
                    inv.setItem(42, this.loading1);
                    inv.setItem(43, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 3) {
                    inv.setItem(20, this.loading);
                    inv.setItem(21, this.loading1);
                    inv.setItem(22, this.loading);
                    inv.setItem(23, this.loading1);
                    inv.setItem(24, this.loading);
                    inv.setItem(29, this.loading);
                    inv.setItem(30, this.loading1);
                    inv.setItem(31, this.loading);
                    inv.setItem(32, this.loading1);
                    inv.setItem(33, this.loading);
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                } else if (this.time == 0) {
                    for (Integer i : InvAnimation.this.content.keySet())
                        inv.setItem(i.intValue(), (ItemStack)InvAnimation.this.content.get(i));
                    InvAnimation.this.p.playSound(InvAnimation.this.p.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    InvAnimation.this.p.removeMetadata("animation", (Plugin)BuildFFA.instance);
                    Bukkit.getScheduler().cancelTask(count2.get());
                }
            }
        },0L, 2L));
    }
}
