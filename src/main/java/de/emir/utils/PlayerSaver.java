package de.emir.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.emir.main.BuildFFA;
import de.emir.sql.MySQL;
import de.emir.sql.MySQLEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlayerSaver {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static PlayerSaver instance;

    public PlayerSaver() {
        instance = this;
        for (Player all : Bukkit.getOnlinePlayers())
            createNewPlayer(all.getUniqueId().toString(), all.getName());
    }

    public static HashMap<String, HashMap<String, Object>> playerProfile = new HashMap<String, HashMap<String, Object>>();

    public void createNewPlayer(final String uuid, final String name) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                HashMap<String, Object> profile = new HashMap<String, Object>();
                if (!BuildFFA.BungeeSystem) {
                    if (!BuildFFA.mysqlMethods.containsMySQL(MySQLEnum.UUID, uuid, "coinsTable")) {
                        String update = "INSERT INTO coinsTable (UUID, name, coins) VALUES (?, ?, ?)";
                        PreparedStatement statement = null;
                        try {
                            statement = MySQL.con.prepareStatement(update);
                            statement.setString(1, uuid);
                            statement.setString(2, name);
                            statement.setInt(3, 0);
                            statement.execute();
                        } catch (SQLException e) {
                            if (BuildFFA.debug)
                                e.printStackTrace();
                        } finally {
                            if (statement != null)
                                try {
                                    statement.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                        }
                        profile.put("coinsTable;coins", Integer.valueOf(0));
                    } else {
                        PlayerSaver.this.updateCoins(uuid, profile);
                    }
                } else {
                    PlayerSaver.this.updateCoins(uuid, profile);
                }
                if (!BuildFFA.mysqlMethods.containsMySQL(MySQLEnum.UUID, uuid, "playerTable")) {
                    String update = "INSERT INTO playerTable (UUID, name, rewards1, rewards2, rewards3, rewards4, tickets, cases, kills, death, chat, doublejump, broadcaster, speed, terms, saveInv, s1, s2, s3, s4, s5, srtokens) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = null;
                    try {
                        statement = MySQL.con.prepareStatement(update);
                        statement.setString(1, uuid);
                        statement.setString(2, name);
                        statement.setString(3, "none");
                        statement.setString(4, "none");
                        statement.setString(5, "none");
                        statement.setString(6, "none");
                        statement.setInt(7, 0);
                        statement.setInt(8, 0);
                        statement.setInt(9, 0);
                        statement.setInt(10, 0);
                        statement.setInt(11, 1);
                        statement.setInt(12, 1);
                        statement.setInt(13, 1);
                        statement.setInt(14, 1);
                        statement.setInt(15, 0);
                        statement.setInt(16, 0);
                        statement.setInt(17, 0);
                        statement.setInt(18, 0);
                        statement.setInt(19, 0);
                        statement.setInt(20, 0);
                        statement.setInt(21, 0);
                        statement.setInt(22, 0);
                        statement.execute();
                    } catch (SQLException e) {
                        if (BuildFFA.debug)
                            e.printStackTrace();
                    } finally {
                        if (statement != null)
                            try {
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                    profile.put("playerTable;srtokens", Integer.valueOf(0));
                    Player p = Bukkit.getPlayer(name);
                    if (p != null)
                        p.setMetadata("firstjoin", (MetadataValue)new FixedMetadataValue((Plugin)BuildFFA.instance, Boolean.valueOf(true)));
                } else {
                    String update = "SELECT * FROM playerTable WHERE UUID='" + uuid + "'";
                    ResultSet statement = null;
                    try {
                        Statement st = MySQL.con.createStatement();
                        statement = st.executeQuery(update);
                        if (statement.next())
                            profile.put("playerTable;srtokens", statement.getObject("srtokens"));
                    } catch (SQLException e) {
                        if (BuildFFA.debug)
                            e.printStackTrace();
                    } finally {
                        if (statement != null)
                            try {
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                }
                if (!BuildFFA.BungeeSystem &&
                        !BuildFFA.mysqlMethods.containsMySQL(MySQLEnum.UUID, uuid, "nickTable")) {
                    String update = "INSERT INTO nickTable (UUID, name, autonick, premiumnick) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = null;
                    try {
                        statement = MySQL.con.prepareStatement(update);
                        statement.setString(1, uuid);
                        statement.setString(2, name);
                        statement.setInt(3, 0);
                        statement.setInt(4, 0);
                        statement.execute();
                    } catch (SQLException e) {
                        if (BuildFFA.debug)
                            e.printStackTrace();
                    } finally {
                        if (statement != null)
                            try {
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                }
                if (!BuildFFA.mysqlMethods.containsMySQL(MySQLEnum.UUID, uuid, BuildFFA.getMySQLTable)) {
                    String update = "INSERT INTO " + BuildFFA.getMySQLTable + " (UUID, name, kills, death, points) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = null;
                    try {
                        statement = MySQL.con.prepareStatement(update);
                        statement.setString(1, uuid);
                        statement.setString(2, name);
                        statement.setInt(3, 0);
                        statement.setInt(4, 0);
                        statement.setInt(5, 0);
                        statement.execute();
                    } catch (SQLException e) {
                        if (BuildFFA.debug)
                            e.printStackTrace();
                    } finally {
                        if (statement != null)
                            try {
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                    profile.put(BuildFFA.getMySQLTable + ";kills", Integer.valueOf(0));
                    profile.put(BuildFFA.getMySQLTable + ";death", Integer.valueOf(0));
                    profile.put(BuildFFA.getMySQLTable + ";points", Integer.valueOf(0));
                } else {
                    String update = "SELECT * FROM " + BuildFFA.getMySQLTable + " WHERE UUID='" + uuid + "'";
                    ResultSet statement = null;
                    try {
                        Statement st = MySQL.con.createStatement();
                        statement = st.executeQuery(update);
                        statement.next();
                        if (!statement.getObject("name").equals(name))
                            PlayerSaver.this.updatePlayersName(uuid, name);
                        profile.put(BuildFFA.getMySQLTable + ";kills", Integer.valueOf(statement.getInt("kills")));
                        profile.put(BuildFFA.getMySQLTable + ";death", Integer.valueOf(statement.getInt("death")));
                        profile.put(BuildFFA.getMySQLTable + ";points", Integer.valueOf(statement.getInt("points")));
                    } catch (SQLException e) {
                        if (BuildFFA.debug)
                            e.printStackTrace();
                    } finally {
                        if (statement != null)
                            try {
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                }
                if (!BuildFFA.mysqlMethods.containsMySQL(MySQLEnum.UUID, uuid, "invsortTable")) {
                    String update = "INSERT INTO invsortTable (UUID, name, buildffa, knockout, xffa, ffa, surf) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = null;
                    try {
                        statement = MySQL.con.prepareStatement(update);
                        statement.setString(1, uuid);
                        statement.setString(2, name);
                        statement.setString(3, "1:0;2:1;3:2;4:3;5:4;6:5;7:6;8:7;9:8");
                        statement.setString(4, "none");
                        statement.setString(5, "none");
                        statement.setString(6, "none");
                        statement.setString(7, "none");
                        statement.execute();
                    } catch (SQLException e) {
                        if (BuildFFA.debug)
                            e.printStackTrace();
                    } finally {
                        if (statement != null)
                            try {
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                    profile.put("invsortTable;buildffa", "1:0;2:1;3:2;4:3;5:4;6:5;7:6;8:7;9:8");
                } else {
                    String update = "SELECT buildffa FROM invsortTable WHERE UUID='" + uuid + "'";
                    ResultSet statement = null;
                    try {
                        Statement st = MySQL.con.createStatement();
                        statement = st.executeQuery(update);
                        if (statement.next())
                            profile.put("invsortTable;buildffa", statement.getString("buildffa"));
                    } catch (SQLException e) {
                        if (BuildFFA.debug)
                            e.printStackTrace();
                    } finally {
                        if (statement != null)
                            try {
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                }
                if (PlayerSaver.playerProfile.containsKey(uuid)) {
                    PlayerSaver.playerProfile.remove(uuid);
                    PlayerSaver.playerProfile.put(uuid, profile);
                } else {
                    PlayerSaver.playerProfile.put(uuid, profile);
                }
            }
        });
    }

    private void updatePlayersName(String uuid, String name) {
        String[] array;
        if (BuildFFA.BungeeSystem) {
            array = new String[] { "playerTable", BuildFFA.getMySQLTable };
        } else {
            array = new String[] { "playerTable", BuildFFA.getMySQLTable, "coinsTable", "nickTable" };
        }
        for (String anArray : array) {
            String update = "UPDATE " + anArray + " SET name= ? WHERE UUID= ?";
            PreparedStatement statement = null;
        }
    }

    public void updateCoins(String uuid, HashMap<String, Object> profile) {
        ResultSet rs = null;
        try {
            rs = MySQL.instance.getResult("SELECT * FROM coinsTable WHERE UUID='" + uuid + "'");
            if (rs.next())
                profile.put("coinsTable;coins", Integer.valueOf(rs.getInt("coins")));
        } catch (SQLException e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}