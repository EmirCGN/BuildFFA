package de.emir.utils.cycle;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.utils.DataSaver;
import de.emir.utils.sort.SortManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kit {
    BuildFFA plugin;

    public Kit(BuildFFA plugin) {
        this.plugin = plugin;
    }

    public static HashMap<GetEnumCycleSystem, String> getKit = new HashMap<GetEnumCycleSystem, String>();

    public void switchKit(String name) {
        if (BuildFFA.invsort)
            return;
        if (KitData.getObject_Kit_values.containsKey(name)) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (!BuildFFACore.getPlayerInEditMode.contains(all)) {
                    all.playSound(all.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                    if (BuildFFACore.getPlayerInGame.contains(all) &&
                            !all.hasMetadata("spec"))
                        setKit(all, name);
                    all.sendMessage(this.plugin.getConfig().getString("messages.prefix").replaceAll("&", "§") + this.plugin
                            .getConfig().getString("Kitchange.message").replaceAll("&", "§").replace("%KIT%", name));
                }
            }
            if (getKit.containsKey(GetEnumCycleSystem.KIT)) {
                getKit.remove(GetEnumCycleSystem.KIT);
                getKit.put(GetEnumCycleSystem.KIT, name);
            } else {
                getKit.put(GetEnumCycleSystem.KIT, name);
            }
        }
    }

    public void setKit(Player p, String name) {
        DataSaver ds = new DataSaver(KitData.getObject_Kit_values.get(name));
        p.getInventory().clear();
        p.getInventory().setHelmet(ds.getItemStack("Kits.items.helmet"));
        p.getInventory().setChestplate(ds.getItemStack("Kits.items.chestplate"));
        p.getInventory().setLeggings(ds.getItemStack("Kits.items.leggings"));
        p.getInventory().setBoots(ds.getItemStack("Kits.items.boots"));
        for (int i = 0; i < 36; i++)
            p.getInventory().setItem(i, ds.getItemStack("Kits.items.content." + i));
        for (PotionEffect effect : p.getActivePotionEffects()) {
            PotionEffectType type = effect.getType();
            p.removePotionEffect(type);
        }
        p.setFireTicks(0);
    }

    public void setSortedKit(Player p) {
        p.getInventory().clear();
        String value = BuildFFA.mysqlMethods.loadStringFromCache(p.getUniqueId().toString(), "invsortTable", "buildffa");
        String[] array = value.split(";");
        for (String s : array) {
            String[] array1 = s.split(":");
            int count = Integer.parseInt(array1[0]);
            int pos = Integer.parseInt(array1[1]);
            p.getInventory().setItem(pos, getItem(count));
        }
        setArmor(p);
    }

    private void setArmor(Player p) {
        String[] array = { "helmet", "chestplate", "leggings", "boots" };
        for (String s : array) {
            if (SortManager.ds_sort.getBoolean("settings." + s + "_enable").booleanValue()) {
                ItemStack item = new ItemStack(SortManager.ds_sort.getInt("settings." + s + "_id").intValue(), 1, SortManager.ds_sort.getShort("settings." + s + "_subid"));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(SortManager.ds_sort.getString("settings." + s + "_name"));
                meta.addEnchant(Enchantment.DURABILITY, 10, true);
                meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                item.setItemMeta(meta);
                if (s.equals("helmet")) {
                    p.getInventory().setHelmet(item);
                } else if (s.equals("chestplate")) {
                    p.getInventory().setChestplate(item);
                } else if (s.equals("leggings")) {
                    p.getInventory().setLeggings(item);
                } else if (s.equals("boots")) {
                    p.getInventory().setBoots(item);
                }
            }
        }
    }

    private ItemStack getItem(int count) {
        if (count == 1 && SortManager.ds_sort.getBoolean("settings.sword_enable").booleanValue())
            return getSword();
        if (count == 2 && SortManager.ds_sort.getBoolean("settings.stick_enable").booleanValue())
            return getStick();
        if (count == 3 && SortManager.ds_sort.getBoolean("settings.blocks_enable").booleanValue())
            return getBlock();
        if (count == 4 && SortManager.ds_sort.getBoolean("settings.cobweb_enable").booleanValue())
            return getCobweb();
        if (count == 5 && SortManager.ds_sort.getBoolean("settings.goldapple_enable").booleanValue())
            return getGoldApple();
        if (count == 6 && SortManager.ds_sort.getBoolean("settings.bow_enable").booleanValue())
            return getBow();
        if (count == 7 && SortManager.ds_sort.getBoolean("settings.arrow_enable").booleanValue())
            return getArrow();
        if (count == 8 && SortManager.ds_sort.getBoolean("settings.extra1_enable").booleanValue())
            return getExtra1();
        if (count == 9 && SortManager.ds_sort.getBoolean("settings.extra2_enable").booleanValue())
            return getExtra2();
        return null;
    }

    private ItemStack getSword() {
        ItemStack item = new ItemStack(SortManager.ds_sort.getInt("settings.sword_id").intValue(), 1, SortManager.ds_sort.getShort("settings.sword_subid"));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.sword_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getStick() {
        ItemStack item = new ItemStack(Material.STICK, 1, (short)0);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.KNOCKBACK, SortManager.ds_sort.getInt("settings.stick_enchant").intValue(), true);
        meta.setDisplayName(SortManager.ds_sort.getString("settings.stick_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getBlock() {
        ItemStack item = new ItemStack(SortManager.ds_sort.getInt("settings.blocks_id").intValue(), 64, SortManager.ds_sort.getShort("settings.blocks_subid"));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.blocks_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getCobweb() {
        ItemStack item = new ItemStack(Material.WEB, SortManager.ds_sort.getInt("settings.cobweb_amount").intValue(), (short)0);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.cobweb_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getGoldApple() {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE, SortManager.ds_sort.getInt("settings.goldapple_amount").intValue(), (short)0);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.goldapple_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getBow() {
        ItemStack item = new ItemStack(Material.BOW, 1, (short)0);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.bow_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getArrow() {
        ItemStack item = new ItemStack(Material.ARROW, SortManager.ds_sort.getInt("settings.arrow_amount").intValue(), (short)0);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.arrow_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getExtra1() {
        ItemStack item = new ItemStack(SortManager.ds_sort.getInt("settings.extra1_id").intValue(), SortManager.ds_sort.getInt("settings.extra1_amount").intValue(), SortManager.ds_sort.getShort("settings.extra1_subid"));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.extra1_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getExtra2() {
        ItemStack item = new ItemStack(SortManager.ds_sort.getInt("settings.extra2_id").intValue(), SortManager.ds_sort.getInt("settings.extra2_amount").intValue(), SortManager.ds_sort.getShort("settings.extra2_subid"));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SortManager.ds_sort.getString("settings.extra2_name"));
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    String getRandomKit(List<String> list) {
        Random r = new Random();
        int random = r.nextInt(list.size());
        String s = list.get(random);
        return s;
    }

    public String getActualKitName() {
        if (getKit.get(GetEnumCycleSystem.KIT) == null)
            return "§c§l/";
        return getKit.get(GetEnumCycleSystem.KIT);
    }
}