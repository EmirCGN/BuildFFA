package de.emir.utils.scoreboard;

import de.emir.main.BuildFFA;
import de.emir.sql.MySQL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardUtils {
    public static ScoreboardUtils instance;

    public ScoreboardUtils() {
        this;
        instance = this;
    }

    public List<String> getClantag(String UUID) {
        List<String> list = new ArrayList<String>();
        ResultSet rs = null;
        try {
            rs = MySQL.instance.getResult("SELECT tag,color FROM clanTable WHERE members LIKE '%" + UUID + "%'");
            if (rs.next()) {
                list.add(rs.getString("tag"));
                list.add(rs.getString("color"));
                return list;
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
        list.add("none");
        list.add("e");
        return list;
    }
}