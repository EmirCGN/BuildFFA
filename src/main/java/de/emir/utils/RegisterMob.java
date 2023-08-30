package de.emir.utils;

import de.emir.main.BuildFFA;
import de.emir.utils.cycle.MapData;
import de.emir.utils.cycle.Map;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.Plugin;

public class RegisterMob {
    private Location loc;

    public RegisterMob() {
        if (!BuildFFA.invsort)
            return;
        removeEntity();
        createVillager();
    }

    public static ArrayList<Entity> getEntity = new ArrayList<Entity>();

    private void createVillager() {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)BuildFFA.getPlugin(BuildFFA.class), new Runnable() {
            public void run() {
                String name = (new Map()).getActualMapName();
                DataSaver ds = new DataSaver((HashMap<String, Object>) MapData.getObject_Map_values.get(name));
                RegisterMob.this.loc = ds.getLocation(name + ".mob");
                if (RegisterMob.this.loc != null)
                    RegisterMob.this.spawnMob();
            }
        },  2L);
    }

    private void spawnMob() {
        Entity entity = getEntityByNumber(this.loc, BuildFFA.invsort_type);
        entity.setCustomName(BuildFFA.invsort_name.replaceAll("&", "§"));
                entity.setCustomNameVisible(true);
        CraftEntity craftentity = (CraftEntity)entity;
        NBTTagCompound tag = new NBTTagCompound();
        craftentity.getHandle().c(tag);
        tag.setBoolean("PersistenceRequired", true);
        tag.setBoolean("CanPickUpLoot", false);
        tag.setBoolean("NoAI", true);
        EntityLiving el = (EntityLiving)craftentity.getHandle();
        el.a(tag);
        NBTTagCompound compound = new NBTTagCompound();
        craftentity.getHandle().e(compound);
        compound.setInt("Silent", 1);
        craftentity.getHandle().f(compound);
        try {
            Field invulnerableField = Entity.class.getDeclaredField("invulnerable");
            invulnerableField.setAccessible(true);
            invulnerableField.setBoolean(craftentity.getHandle(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getEntity.add(entity);
    }

    private Entity getEntityByNumber(Location loc, String s) {
        Entity entity = null;
        if (s.equals("chicken"))
            entity = loc.getWorld().spawn(loc, Chicken.class);
        if (s.equals("rabbit"))
            entity = loc.getWorld().spawn(loc, Rabbit.class);
        if (s.equals("sheep"))
            entity = loc.getWorld().spawn(loc, Sheep.class);
        if (s.equals("pig"))
            entity = loc.getWorld().spawn(loc, Pig.class);
        if (s.equals("cow"))
            entity = loc.getWorld().spawn(loc, Cow.class);
        if (s.equals("mushroomcow"))
            entity = loc.getWorld().spawn(loc, MushroomCow.class);
        if (s.equals("ocelot"))
            entity = loc.getWorld().spawn(loc, Ocelot.class);
        if (s.equals("wolf"))
            entity = loc.getWorld().spawn(loc, Wolf.class);
        if (s.equals("spider"))
            entity = loc.getWorld().spawn(loc, Spider.class);
        if (s.equals("cavespider"))
            entity = loc.getWorld().spawn(loc, CaveSpider.class);
        if (s.equals("enderman"))
            entity = loc.getWorld().spawn(loc, Enderman.class);
        if (s.equals("creeper"))
            entity = loc.getWorld().spawn(loc, Creeper.class);
        if (s.equals("pigzombie")) {
            entity = loc.getWorld().spawn(loc, PigZombie.class);
            ((PigZombie)entity).setBaby(false);
        }
        if (s.equals("blaze"))
            entity = loc.getWorld().spawn(loc, Blaze.class);
        if (s.equals("witch"))
            entity = loc.getWorld().spawn(loc, Witch.class);
        if (s.equals("villager"))
            entity = loc.getWorld().spawn(loc, Villager.class);
        if (s.equals("golem"))
            entity = loc.getWorld().spawn(loc, IronGolem.class);
        if (entity == null)
            entity = loc.getWorld().spawn(loc, Villager.class);
        return entity;
    }

    public void removeEntity() {
        for (Entity entity : getEntity)
            entity.remove();
    }
}
