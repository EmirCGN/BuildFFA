package de.emir.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class DataSaver {
    private HashMap<String, Object> data;

    public DataSaver(HashMap<String, Object> data) {
        this.data = data;
    }

    public boolean contains(String path) {
        Boolean b = Boolean.valueOf(false);
        if (this.data.containsKey(path))
            b = Boolean.valueOf(true);
        return b.booleanValue();
    }

    public Integer getInt(String path) {
        Integer i = Integer.valueOf(0);
        try {
            i = (Integer)this.data.get(path);
        } catch (Exception exception) {}
        return i;
    }

    public String getString(String path) {
        String s = "";
        try {
            s = (String)this.data.get(path);
        } catch (Exception exception) {}
        return s;
    }

    public Boolean getBoolean(String path) {
        Boolean b = Boolean.valueOf(false);
        try {
            b = (Boolean)this.data.get(path);
        } catch (Exception exception) {}
        return b;
    }

    public Double getDouble(String path) {
        Double d = Double.valueOf(0.0D);
        try {
            d = (Double)this.data.get(path);
        } catch (Exception exception) {}
        return d;
    }

    public Float getFloat(String path) {
        Float f = Float.valueOf(0.0F);
        try {
            f = (Float)this.data.get(path);
        } catch (Exception exception) {}
        return f;
    }

    public long getLong(String path) {
        int i = 0;
        try {
            i = ((Integer)this.data.get(path)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public short getShort(String path) {
        int i = 0;
        try {
            i = ((Integer)this.data.get(path)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (short)i;
    }

    public ItemStack getItemStack(String path) {
        ItemStack item = null;
        try {
            item = (ItemStack)this.data.get(path);
        } catch (Exception exception) {}
        return item;
    }

    public Location getLocation(String path) {
        Location loc = null;
        try {
            loc = (Location)this.data.get(path);
        } catch (Exception exception) {}
        return loc;
    }

    public List<String> getStringList(String path) {
        List<String> list = new ArrayList<String>();
        try {
            list = (List<String>)this.data.get(path);
        } catch (Exception exception) {}
        return list;
    }

    public List<Integer> getIntegerList(String path) {
        List<Integer> list = new ArrayList<Integer>();
        try {
            list = (List<Integer>)this.data.get(path);
        } catch (Exception exception) {}
        return list;
    }

    public List<Location> getLocationList(String path) {
        List<Location> list = new ArrayList<Location>();
        try {
            list = (List<Location>)this.data.get(path);
        } catch (Exception exception) {}
        return list;
    }
}