package de.emir.sql;

import de.emir.main.BuildFFA;
import de.emir.utils.PlayerSaver;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQLbuildffaTable {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public void onDeathKiller(final String UUID_killer, final String UUID_player, int points, int coins) {
        final int result = BuildFFA.mysqlMethods.loadIntFromCache(UUID_killer, "coinsTable", "coins").intValue() + coins;
        final int points_killer = BuildFFA.mysqlMethods.loadIntFromCache(UUID_killer, BuildFFA.getMySQLTable, "points").intValue() + points;
        final int kills_killer = BuildFFA.mysqlMethods.loadIntFromCache(UUID_killer, BuildFFA.getMySQLTable, "kills").intValue() + 1;
        final int death_player = BuildFFA.mysqlMethods.loadIntFromCache(UUID_player, BuildFFA.getMySQLTable, "death").intValue() + 1;
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                try {
                    PreparedStatement ps = MySQL.con.prepareStatement("UPDATE " + BuildFFA.getMySQLTable + " SET points= ?, kills= ? WHERE UUID= ?");
                    ps.setInt(1, points_killer);
                    ps.setInt(2, kills_killer);
                    ps.setString(3, UUID_killer);
                    ps.executeUpdate();
                    ps.close();
                    PreparedStatement ps1 = MySQL.con.prepareStatement("UPDATE " + BuildFFA.getMySQLTable + " SET death= ? WHERE UUID= ?");
                    ps1.setInt(1, death_player);
                    ps1.setString(2, UUID_player);
                    ps1.executeUpdate();
                    ps1.close();
                    PreparedStatement ps2 = MySQL.con.prepareStatement("UPDATE coinsTable SET coins= ? WHERE UUID= ?");
                    ps2.setInt(1, result);
                    ps2.setString(2, UUID_killer);
                    ps2.executeUpdate();
                    ps2.close();
                    if (BuildFFA.BungeeSystem && BuildFFA.Clans) {
                        String tag_killer = BuildFFA.mysqlMethods.getClanTag(UUID_killer);
                        String tag_player = BuildFFA.mysqlMethods.getClanTag(UUID_player);
                        if (!tag_killer.equalsIgnoreCase(tag_player)) {
                            if (!tag_killer.equalsIgnoreCase("none")) {
                                PreparedStatement ps3 = MySQL.con.prepareStatement("UPDATE clanTable SET elo= elo+? WHERE tag = ?");
                                ps3.setInt(1, BuildFFA.cp_killer);
                                ps3.setString(2, tag_killer);
                                ps3.executeUpdate();
                                ps3.close();
                            }
                            if (!tag_player.equalsIgnoreCase("none")) {
                                PreparedStatement ps3 = MySQL.con.prepareStatement("UPDATE clanTable SET elo= elo-? WHERE tag = ?");
                                ps3.setInt(1, BuildFFA.cp_player);
                                ps3.setString(2, tag_player);
                                ps3.executeUpdate();
                                ps3.close();
                            }
                        }
                    }
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                }
            }
        });
        HashMap<String, Object> profile = (HashMap<String, Object>) PlayerSaver.playerProfile.get(UUID_killer);
        int kills = BuildFFA.mysqlMethods.loadIntFromCache(UUID_killer, BuildFFA.getMySQLTable, "kills").intValue();
        int points1 = BuildFFA.mysqlMethods.loadIntFromCache(UUID_killer, BuildFFA.getMySQLTable, "points").intValue();
        profile.remove("" + BuildFFA.getMySQLTable + ";kills");
        profile.remove("" + BuildFFA.getMySQLTable + ";points");
        profile.put("" + BuildFFA.getMySQLTable + ";kills", Integer.valueOf(kills + 1));
        profile.put("" + BuildFFA.getMySQLTable + ";points", Integer.valueOf(points1 + points));
        profile.put("coinsTable;coins", Integer.valueOf(result));
        PlayerSaver.playerProfile.put(UUID_killer, profile);
        HashMap<String, Object> profile1 = (HashMap<String, Object>)PlayerSaver.playerProfile.get(UUID_player);
        int death = BuildFFA.mysqlMethods.loadIntFromCache(UUID_player, BuildFFA.getMySQLTable, "death").intValue();
        profile1.remove("" + BuildFFA.getMySQLTable + ";death");
        profile1.put("" + BuildFFA.getMySQLTable + ";death", Integer.valueOf(death + 1));
        PlayerSaver.playerProfile.put(UUID_player, profile1);
    }

    public void onDeathPlayer(final String UUID_player) {
        final int death = BuildFFA.mysqlMethods.loadIntFromCache(UUID_player, BuildFFA.getMySQLTable, "death").intValue() + 1;
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                try {
                    PreparedStatement ps = MySQL.con.prepareStatement("UPDATE " + BuildFFA.getMySQLTable + " SET death= ? WHERE UUID= ?");
                    ps.setInt(1, death);
                    ps.setString(2, UUID_player);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                }
            }
        });
        HashMap<String, Object> profile = (HashMap<String, Object>)PlayerSaver.playerProfile.get(UUID_player);
        profile.remove("" + BuildFFA.getMySQLTable + ";death");
        profile.put("" + BuildFFA.getMySQLTable + ";death", Integer.valueOf(death));
        PlayerSaver.playerProfile.put(UUID_player, profile);
    }

    public void onResetStats(final String uuid) {
        HashMap<String, Object> profile = (HashMap<String, Object>)PlayerSaver.playerProfile.get(uuid);
        profile.remove("" + BuildFFA.getMySQLTable + ";death");
        profile.remove("" + BuildFFA.getMySQLTable + ";kills");
        profile.put("" + BuildFFA.getMySQLTable + ";death", Integer.valueOf(0));
        profile.put("" + BuildFFA.getMySQLTable + ";kills", Integer.valueOf(0));
        PlayerSaver.playerProfile.put(uuid, profile);
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                try {
                    PreparedStatement ps = MySQL.con.prepareStatement("UPDATE " + BuildFFA.getMySQLTable + " SET death= ?, kills= ? WHERE UUID= ?");
                    ps.setInt(1, 0);
                    ps.setInt(2, 0);
                    ps.setString(3, uuid);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                }
            }
        });
    }
}