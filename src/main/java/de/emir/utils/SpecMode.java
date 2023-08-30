package de.emir.utils;


import de.emir.main.BuildFFA;
import de.emir.sql.MySQL;
import de.emir.sql.MySQLEnum;
import de.emir.utils.cycle.Map;
import de.emir.utils.inv.Inv;
import de.emir.utils.scoreboard.CachePlayer;
import de.emir.utils.scoreboard.CacheScoreboard;
import de.emir.utils.scoreboard.CoreScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecMode {
    public static SpecMode instance;

    public SpecMode() {
        instance = this;
    }

    public boolean checkSpecMode(Player p) {
        if (p.hasPermission("bungee.specmode") && BuildFFA.BungeeSystem) {
            if (getSpecMode(p.getUniqueId().toString())) {
                setSpecMode(p);
                return true;
            }
            if (p.hasMetadata("spec"))
                changeMySQLState(p);
            return false;
        }
        if (p.hasMetadata("spec"))
            changeMySQLState(p);
        return false;
    }

    public void manageSpecMode(Player p) {
        if (p.hasPermission("bungee.specmode") && BuildFFA.BungeeSystem)
            changeMySQLState(p);
    }

    public void setSpecMode(Player p) {
        p.setMetadata("spec", (MetadataValue)new FixedMetadataValue((Plugin)BuildFFA.instance, Boolean.valueOf(true)));
        p.setAllowFlight(true);
        p.setFlying(true);
        p.getInventory().clear();
        if (!BuildFFA.mysqlMethods.containsMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "teamTable"))
            registerMySQLState(p);
        setSB(p);
    }

    public void removeSpecMode(Player p) {
        p.removeMetadata("spec", (Plugin)BuildFFA.instance);
        p.setFlying(false);
        p.setAllowFlight(false);
        (new Map()).tpPlayerToActualSpawnLocation(p);
        Inv.instance.setInventory(p);
        p.setFlySpeed(0.2F);
        p.setWalkSpeed(0.2F);
        removeSB(p);
    }

    public void setSB(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)BuildFFA.getPlugin(BuildFFA.class), new Runnable() {
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.hasPermission("bungee.specmode"))
                        all.hidePlayer(p);
                }
                String teamname = ((CoreScoreBoard)CachePlayer.getBoard.get(p.getUniqueId().toString())).personalTeam[0];
                if (teamname == null)
                    teamname = (new CachePlayer()).getTeamPlayer(p);
                for (Player all : Bukkit.getOnlinePlayers()) {
                    Scoreboard b = ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).board;
                    Team team = b.getTeam(teamname);
                    team.setSuffix(BuildFFA.spec_suffix);
                }
            }
        },  20L);
    }

    public void removeSB(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)BuildFFA.getPlugin(BuildFFA.class), new Runnable() {
            public void run() {
                if (((Boolean) CacheScoreboard.getObject_ScoreBoard.get("clantags")).booleanValue()) {
                    String teamname = ((CoreScoreBoard)CachePlayer.getBoard.get(p.getUniqueId().toString())).personalTeam[0];
                    if (teamname == null)
                        teamname = (new CachePlayer()).getTeamPlayer(p);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        String[] array = ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).getTeamClanTag(p);
                        if (array[2].equalsIgnoreCase("none")) {
                            ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).board.getTeam(teamname).setSuffix("");
                            continue;
                        }
                        ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).board.getTeam(teamname).setSuffix(array[2]);
                    }
                } else {
                    String teamname = ((CoreScoreBoard)CachePlayer.getBoard.get(p.getUniqueId().toString())).personalTeam[0];
                    if (teamname == null)
                        teamname = (new CachePlayer()).getTeamPlayer(p);
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        String[] array = ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).getTeamClanTag(p);
                        if (array[2].equalsIgnoreCase("none"))
                            continue;
                        ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).board.getTeam(teamname).setSuffix("");
                    }
                }
                for (Player all : Bukkit.getOnlinePlayers())
                    all.showPlayer(p);
            }
        },  20L);
    }

    public void changeMySQLState(Player p) {
        int i = 0;
        if (!p.hasMetadata("spec")) {
            i = 1;
            setSpecMode(p);
        } else {
            removeSpecMode(p);
        }
        PreparedStatement st = null;
        try {
            st = MySQL.con.prepareStatement("UPDATE teamTable SET rm=? WHERE UUID=?");
            st.setInt(1, i);
            st.setString(2, p.getUniqueId().toString());
            st.executeUpdate();
        } catch (SQLException e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        } finally {
            if (st != null)
                try {
                    st.close();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                }
        }
    }

    public void registerMySQLState(Player p) {
        PreparedStatement st = null;
        try {
            st = MySQL.con.prepareStatement("INSERT INTO teamTable (UUID, name, rm) VALUES (?, ?, ?)");
            st.setString(1, p.getUniqueId().toString());
            st.setString(2, p.getName());
            st.setInt(3, 1);
            st.executeUpdate();
        } catch (SQLException e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        } finally {
            if (st != null)
                try {
                    st.close();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                }
        }
    }

    public boolean getSpecMode(String UUID) {
        boolean b = false;
        ResultSet rs = null;
        try {
            rs = MySQL.instance.getResult("SELECT rm FROM teamTable WHERE UUID='" + UUID + "'");
            if (rs.next()) {
                int i = rs.getInt("rm");
                if (i == 1)
                    b = true;
            }
        } catch (SQLException e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                }
        }
        return b;
    }
}