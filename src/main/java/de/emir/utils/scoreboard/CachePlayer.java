package de.emir.utils.scoreboard;

import de.emir.main.BuildFFA;
import de.emir.main.FileManager;
import de.emir.sql.MySQLEnum;
import de.emir.utils.DataSaver;

import java.util.HashMap;

import de.emir.utils.nick.Nick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CachePlayer {
    private DataSaver ds_tab;

    public static CachePlayer instance;

    public CachePlayer() {
        this.ds_tab = new DataSaver(FileManager.getObject_Tab);
        instance = this;
        for (Player all : Bukkit.getOnlinePlayers())
            loadPlayer(all);
    }

    public static HashMap<String, String> getTeamName = new HashMap<String, String>();

    public static HashMap<String, CoreScoreBoard> getBoard = new HashMap<String, CoreScoreBoard>();

    void loadPlayer(Player p) {
        getTeamName.put(p.getUniqueId().toString(), getTeamPlayer(p));
    }

    public String getTeamPlayer(Player p) {
        String s = "999Team";
        if (Nick.isNicked.containsKey(p)) {
            if (BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "premiumnick").intValue() == 1) {
                int i1 = this.ds_tab.getInt("nick.numberforpremium").intValue();
                if (i1 < 10)
                    return "0" + i1 + "Team";
                return i1 + "Team";
            }
            return s;
        }
        for (int i = 1; i <= FileManager.getTeamSize; i++) {
            if (this.ds_tab.contains(i + ".perm") &&
                    p.hasPermission(this.ds_tab.getString(i + ".perm"))) {
                if (i < 10)
                    return "0" + i + "Team";
                return i + "Team";
            }
        }
        return s;
    }
}