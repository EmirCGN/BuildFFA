package de.emir.sql;

import de.emir.main.BuildFFA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.emir.utils.PlayerSaver;
import de.emir.utils.nick.Nick;
import org.bukkit.entity.Player;

public class MySQLMethods {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static HashMap<Integer, String> getGroupsPrefix = new HashMap<Integer, String>();

    public static HashMap<String, Integer> getGroupID = new HashMap<String, Integer>();

    public MySQLMethods() {
        if (BuildFFA.BungeeSystem && BuildFFA.xperms)
            loadGroupsPrefix();
    }

    private String getTypeMySQLEnum(MySQLEnum type) {
        if (type == MySQLEnum.NAME)
            return "name";
        return "UUID";
    }

    public String loadStringFromCache(String uuid, String table, String identifier) {
        return "" + ((HashMap)PlayerSaver.playerProfile.get(uuid)).get(table + ";" + identifier);
    }

    public Integer loadIntFromCache(String uuid, String table, String identifier) {
        return Integer.valueOf(Integer.parseInt("" + ((HashMap)PlayerSaver.playerProfile.get(uuid)).get(table + ";" + identifier)));
    }

    public Long loadLongFromCache(String uuid, String table, String identifier) {
        return new Long("" + ((HashMap)PlayerSaver.playerProfile.get(uuid)).get(table + ";" + identifier));
    }

    public String loadStringFromMySQL(MySQLEnum type, String identifier, String table, String field) {
        String s = "";
        String update = "SELECT * FROM " + table + " WHERE " + getTypeMySQLEnum(type) + "='" + identifier + "'";
        ResultSet statement = null;
        try {
            Statement st = MySQL.con.createStatement();
            statement = st.executeQuery(update);
            if (statement.next())
                s = statement.getString(field);
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
        return s;
    }

    public Integer loadIntFromMySQL(MySQLEnum type, String identifier, String table, String field) {
        int i = 0;
        try {
            Statement st = MySQL.con.createStatement();
            ResultSet rs = st.executeQuery("SELECT " + field + " FROM " + table + " WHERE " + getTypeMySQLEnum(type) + "='" + identifier + "'");
            if (rs.next())
                i = rs.getInt(field);
        } catch (SQLException e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
        return Integer.valueOf(i);
    }

    public Long loadLongFromMySQL(MySQLEnum type, String identifier, String table, String field) {
        Long l = Long.valueOf(0L);
        String update = "SELECT * FROM " + table + " WHERE " + getTypeMySQLEnum(type) + "='" + identifier + "'";
        ResultSet statement = null;
        try {
            Statement st = MySQL.con.createStatement();
            System.out.println(update);
            statement = st.executeQuery(update);
            if (statement.next())
                l = Long.valueOf(statement.getLong(field));
        } catch (SQLException e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        } finally {
            if (statement != null)
                try {
                    statement.close();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                }
        }
        return l;
    }

    public void setStringFromMySQL(final MySQLEnum type, final String identifier, final String table, final String field, final String value) {
        if (type == MySQLEnum.UUID) {
            ((HashMap)PlayerSaver.playerProfile.get(identifier)).remove(table + ";" + field);
            ((HashMap<String, String>)PlayerSaver.playerProfile.get(identifier)).put(table + ";" + field, value);
        }
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                String update = "UPDATE " + table + " SET " + field + "= ? WHERE " + MySQLMethods.this.getTypeMySQLEnum(type) + "= ?";
                PreparedStatement p = null;
                try {
                    p = MySQL.con.prepareStatement(update);
                    p.setString(1, value);
                    p.setString(2, identifier);
                    p.executeUpdate();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                } finally {
                    if (p != null)
                        try {
                            p.close();
                        } catch (SQLException e) {
                            if (BuildFFA.debug)
                                e.printStackTrace();
                        }
                }
            }
        });
    }

    public void setIntFromMySQL(final MySQLEnum type, final String identifier, final String table, final String field, final int value) {
        if (type == MySQLEnum.UUID) {
            ((HashMap)PlayerSaver.playerProfile.get(identifier)).remove(table + ";" + field);
            ((HashMap<String, Integer>)PlayerSaver.playerProfile.get(identifier)).put(table + ";" + field, Integer.valueOf(value));
        }
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                String update = "UPDATE " + table + " SET " + field + "= ? WHERE " + MySQLMethods.this.getTypeMySQLEnum(type) + "= ?";
                PreparedStatement p = null;
                try {
                    p = MySQL.con.prepareStatement(update);
                    p.setInt(1, value);
                    p.setString(2, identifier);
                    p.executeUpdate();
                } catch (SQLException e) {
                    if (BuildFFA.debug)
                        e.printStackTrace();
                } finally {
                    if (p != null)
                        try {
                            p.close();
                        } catch (SQLException e) {
                            if (BuildFFA.debug)
                                e.printStackTrace();
                        }
                }
            }
        });
    }

    public void setLongFromMySQL(final MySQLEnum type, final String identifier, final String table, final String field, final long value) {
        if (type == MySQLEnum.UUID) {
            ((HashMap)PlayerSaver.playerProfile.get(identifier)).remove(table + ";" + field);
            ((HashMap<String, Long>)PlayerSaver.playerProfile.get(identifier)).put(table + ";" + field, Long.valueOf(value));
        }
        EXECUTOR_SERVICE.execute(new Runnable() {
            public void run() {
                String update = "UPDATE " + table + " SET " + field + "= ? WHERE " + MySQLMethods.this.getTypeMySQLEnum(type) + "= ?";
                PreparedStatement p = null;
                try {
                    p = MySQL.con.prepareStatement(update);
                    p.setLong(1, value);
                    p.setString(2, identifier);
                    p.executeUpdate();
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
    }

    public boolean containsMySQL(MySQLEnum type, String identifier) {
        try {
            ResultSet rs = MySQL.instance.getResult("SELECT * FROM coinsTable WHERE " + getTypeMySQLEnum(type) + "='" + identifier + "'");
            if (rs.next())
                return (rs.getString(getTypeMySQLEnum(type)) != null);
            rs.close();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean containsMySQL(MySQLEnum type, String identifier, String table) {
        try {
            ResultSet rs = MySQL.instance.getResult("SELECT * FROM " + table + " WHERE " + getTypeMySQLEnum(type) + "='" + identifier + "'");
            if (rs.next())
                return (rs.getString(getTypeMySQLEnum(type)) != null);
            rs.close();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getTopValues(int size, String type, String table, String field) {
        List<String> list = new ArrayList<String>();
        try {
            ResultSet rs = MySQL.instance.getResult("SELECT " + type + " FROM " + table + " ORDER BY " + field + " DESC LIMIT " + size);
            while (rs.next())
                list.add(rs.getString("name"));
            rs.close();
        } catch (Exception e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
        return list;
    }

    public String getRankingValues(String UUID) {
        String s = "-1";
        try {
            ResultSet rs = MySQL.instance.getResult("SELECT UUID FROM " + BuildFFA.getMySQLTable + " ORDER BY points DESC");
            int counter = 0;
            while (rs.next()) {
                counter++;
                if (rs.getString("UUID").equalsIgnoreCase(UUID))
                    break;
            }
            s = counter + "";
            rs.close();
        } catch (Exception e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
        return s;
    }

    public String getClanTag(String UUID) {
        String tag = "none";
        ResultSet rs = null;
        try {
            rs = MySQL.instance.getResult("SELECT clan FROM friendsTable WHERE UUID='" + UUID + "'");
            if (rs.next())
                tag = rs.getString("clan");
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
        return tag;
    }

    public String getRankedName(Player p) {
        String name = p.getName();
        if (!BuildFFA.xperms)
            return name;
        String FINALNAME = name;
        if (Nick.isNicked.containsKey(p)) {
            if (BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "nickTable", "premiumnick").intValue() == 1) {
                FINALNAME = (String)getGroupsPrefix.get(Integer.valueOf(BuildFFA.xperms_premium)) + name;
            } else {
                FINALNAME = (String)getGroupsPrefix.get(Integer.valueOf(-1)) + name;
            }
            return FINALNAME;
        }
        try {
            int groupid;
            if (getGroupID.containsKey(name)) {
                groupid = ((Integer)getGroupID.get(name)).intValue();
            } else {
                groupid = getPlayersGroupID(name);
                getGroupID.put(name, Integer.valueOf(groupid));
            }
            FINALNAME = (String)getGroupsPrefix.get(Integer.valueOf(groupid)) + name;
        } catch (Exception e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
        return FINALNAME;
    }

    public int getPlayersGroupID(String name) {
        try {
            ResultSet rs = MySQL.instance.getResult("SELECT * FROM permsTable WHERE name='" + name + "'");
            if (rs.next())
                return rs.getInt("groupid");
            rs.close();
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    private void loadGroupsPrefix() {
        if (!BuildFFA.xperms)
            return;
        ResultSet rs = null;
        try {
            rs = MySQL.instance.getResult("SELECT groupid,prefix FROM groupsTable");
            while (rs.next()) {
                if (rs.getString("prefix") != null)
                    getGroupsPrefix.put(Integer.valueOf(rs.getInt("groupid")), rs.getString("prefix").replaceAll("&", "§"));
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
    }
}