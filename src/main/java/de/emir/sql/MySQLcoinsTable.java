package de.emir.sql;

import de.emir.main.BuildFFA;
import de.emir.utils.PlayerSaver;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQLcoinsTable {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public void setCoins(final String uuid, final int coins) {
        String update = "UPDATE coinsTable SET coins= ? WHERE UUID= ?";
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                PreparedStatement p = null;
                try {
                    p = MySQL.con.prepareStatement("UPDATE coinsTable SET coins= ? WHERE UUID= ?");
                    p.setInt(1, coins);
                    p.setString(2, uuid);
                    p.execute();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                } finally {
                    if (p != null)
                        try {
                            p.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
            }
        });
        HashMap<String, Object> profile = (HashMap<String, Object>)PlayerSaver.playerProfile.get(uuid);
        profile.remove("coinsTable;coins");
        profile.put("coinsTable;coins", Integer.valueOf(coins));
        PlayerSaver.playerProfile.remove(uuid);
        PlayerSaver.playerProfile.put(uuid, profile);
    }

    public void addCoins(final String uuid, int coins) {
        final int result = BuildFFA.mysqlMethods.loadIntFromCache(uuid, "coinsTable", "coins").intValue() + coins;
        String update = "UPDATE coinsTable SET coins= ? WHERE UUID= ?";
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                PreparedStatement p = null;
                try {
                    p = MySQL.con.prepareStatement("UPDATE coinsTable SET coins= ? WHERE UUID= ?");
                    p.setInt(1, result);
                    p.setString(2, uuid);
                    p.execute();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                } finally {
                    if (p != null)
                        try {
                            p.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
            }
        });
        HashMap<String, Object> profile = (HashMap<String, Object>)PlayerSaver.playerProfile.get(uuid);
        profile.remove("coinsTable;coins");
        profile.put("coinsTable;coins", Integer.valueOf(result));
        PlayerSaver.playerProfile.remove(uuid);
        PlayerSaver.playerProfile.put(uuid, profile);
    }

    public void removeCoins(final String uuid, int coins) {
        final int result = BuildFFA.mysqlMethods.loadIntFromCache(uuid, "coinsTable", "coins").intValue() - coins;
        String update = "UPDATE coinsTable SET coins= ? WHERE UUID= ?";
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                PreparedStatement p = null;
                try {
                    p = MySQL.con.prepareStatement("UPDATE coinsTable SET coins= ? WHERE UUID= ?");
                    p.setInt(1, result);
                    p.setString(2, uuid);
                    p.execute();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                } finally {
                    if (p != null)
                        try {
                            p.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
            }
        });
        HashMap<String, Object> profile = (HashMap<String, Object>)PlayerSaver.playerProfile.get(uuid);
        profile.remove("coinsTable;coins");
        profile.put("coinsTable;coins", Integer.valueOf(result));
        PlayerSaver.playerProfile.remove(uuid);
        PlayerSaver.playerProfile.put(uuid, profile);
    }
}