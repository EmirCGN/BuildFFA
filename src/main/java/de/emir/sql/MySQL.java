package de.emir.sql;

import de.emir.main.BuildFFA;
import de.emir.main.FileManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
    public static MySQL instance;

    public static Connection con;

    public MySQL() {
        instance = this;
        connect();
        createTable();
    }

    private void connect() {
        if (!isConnected())
            try {
                String host = FileManager.getObject_MySQL.get("Host").toString();
                int port = Integer.parseInt((new StringBuilder()).append(FileManager.getObject_MySQL.get("Port")).append("").toString());
                String database = FileManager.getObject_MySQL.get("Database").toString();
                String user = FileManager.getObject_MySQL.get("User").toString();
                String password = FileManager.getObject_MySQL.get("Password").toString();
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", "" + user, "" + password);
            } catch (Exception e) {
                if (BuildFFA.debug)
                    e.printStackTrace();
            }
    }

    public void disconnect() {
        if (isConnected())
            try {
                con.close();
            } catch (Exception e) {
                if (BuildFFA.debug)
                    e.printStackTrace();
            }
    }

    private void createTable() {
        if (isConnected())
            try {
                con.prepareStatement("CREATE TABLE IF NOT EXISTS coinsTable (UUID VARCHAR(40) PRIMARY KEY, name VARCHAR(17), coins INT(20))")
                        .executeUpdate();
                con.prepareStatement("CREATE TABLE IF NOT EXISTS nickTable (UUID VARCHAR(40) PRIMARY KEY, name VARCHAR(17), autonick INT(5), premiumnick INT(5))")
                        .executeUpdate();
                con.prepareStatement("CREATE TABLE IF NOT EXISTS playerTable (UUID VARCHAR(40) PRIMARY KEY, name VARCHAR(17), rewards1 VARCHAR(11), rewards2 VARCHAR(11), rewards3 VARCHAR(11), rewards4 VARCHAR(11), tickets INT(10), cases INT(10), kills INT(5), death INT(5), chat INT(2), doublejump INT(2), broadcaster INT(2), speed INT(2), terms INT(2), saveInv INT(2), s1 INT(2), s2 INT(2), s3 INT(2), s4 INT(2), s5 INT(2), srtokens INT(10))")

                        .executeUpdate();
                con.prepareStatement("CREATE TABLE IF NOT EXISTS " + BuildFFA.getMySQLTable + " (UUID VARCHAR(40) PRIMARY KEY, name VARCHAR(17), kills INT(12), death INT(12), points INT(12))")

                        .executeUpdate();
                con.prepareStatement("CREATE TABLE IF NOT EXISTS invsortTable (UUID VARCHAR(40) PRIMARY KEY, name VARCHAR(17), buildffa VARCHAR(100), knockout VARCHAR(100), xffa VARCHAR(100), ffa VARCHAR(100), surf VARCHAR(100))")
                        .executeUpdate();
            } catch (SQLException e) {
                        if (BuildFFA.debug)
                    e.printStackTrace();
            }
    }

    public boolean isConnected() {
        return (con != null);
    }

    public void update(String qry) {
        if (isConnected())
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                if (BuildFFA.debug)
                    e.printStackTrace();
            }
    }

    public ResultSet getResult(String qry) {
        ResultSet rs = null;
        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            connect();
        }
        return rs;
    }
}
