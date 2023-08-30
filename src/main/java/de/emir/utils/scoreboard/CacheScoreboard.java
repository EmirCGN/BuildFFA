package de.emir.utils.scoreboard;

import de.emir.main.BuildFFA;
import de.emir.main.FileManager;
import de.emir.utils.DataSaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheScoreboard {
    public static CacheScoreboard instance;

    public static DataSaver ds_tab;

    public CacheScoreboard() {
        instance = this;
        loadCache();
        ds_tab = new DataSaver(FileManager.getObject_Tab);
        new ScoreboardUtils();
    }

    public static HashMap<String, Object> getObject_ScoreBoard = new HashMap<String, Object>();

    public void loadCache() {
        DataSaver ds_tab = new DataSaver(FileManager.getObject_Tab);
        getObject_ScoreBoard.put("displayname", BuildFFA.instance.getConfig().getString("Scoreboard.name").replaceAll("&", "§"));
        List<String> list1 = BuildFFA.instance.getConfig().getStringList("Scoreboard.list");
        List<String> list2 = new ArrayList<String>();
    }
}
