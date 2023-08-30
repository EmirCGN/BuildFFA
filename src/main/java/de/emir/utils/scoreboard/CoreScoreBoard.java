package de.emir.utils.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.emir.main.BuildFFA;
import de.emir.main.BuildFFACore;
import de.emir.sql.MySQLEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class CoreScoreBoard {
    public Scoreboard board;

    private Player p;

    private HashMap<String, String> teams;

    private Boolean clantag;

    public String[] personalTeam;

    public CoreScoreBoard(Player p) {
        this.p = p;
        HashMap<String, Object> map = CacheScoreboard.getObject_ScoreBoard;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective o = this.board.registerNewObjective("aaa", "bbb");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName((String)map.get("displayname"));
        int coins = 0;
        int kills = 0;
        int death = 0;
        String kd = BuildFFACore.instance.getKDFromUUID(p.getUniqueId().toString());
        String date = BuildFFACore.instance.getDate();
        String ranking = "";
        try {
            coins = BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), "coinsTable", "coins").intValue();
            kills = BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), BuildFFA.getMySQLTable, "kills").intValue();
            death = BuildFFA.mysqlMethods.loadIntFromMySQL(MySQLEnum.UUID, p.getUniqueId().toString(), BuildFFA.getMySQLTable, "death").intValue();
            ranking = BuildFFA.mysqlMethods.getRankingValues(p.getUniqueId().toString());
        } catch (Exception e) {
            if (BuildFFA.debug)
                e.printStackTrace();
        }
        List<String> list1 = (List<String>)map.get("scoreboard");
        for (int i = 0; i < list1.size(); i++) {
            String prefix, s = ((String)list1.get(i)).replace("%COINS%", "" + coins).replace("%KILLS%", "" + kills).replace("%DEATH%", "" + death).replace("%DATE%", "" + date).replace("%NAME%", p.getName()).replace("%RANKING%", "" + ranking).replace("%KD%", kd);
            String suffix = null;
            Team st = this.board.registerNewTeam("st" + i);
            if (s.length() >= 16) {
                prefix = s.substring(0, 16);
                if (s.length() >= 17)
                    suffix = ChatColor.getLastColors(prefix) + s.substring(16);
                if (suffix != null && suffix.length() >= 16)
                    suffix = suffix.substring(0, 16);
            } else {
                prefix = s;
            }
            st.setPrefix(prefix);
            if (suffix != null)
                st.setSuffix(suffix);
            st.addEntry(getScoreboardColor().get(i));
            o.getScore(getScoreboardColor().get(i)).setScore(list1.size() - i);
        }
        this.teams = (HashMap<String, String>)map.get("teams");
        this.clantag = (Boolean)map.get("clantags");
        setTeams();
    }

    private void setTeams() {
        (new CachePlayer()).loadPlayer(this.p);
        if (!this.clantag.booleanValue()) {
            Team team = this.board.registerNewTeam("999Team");
            team.setPrefix(CacheScoreboard.ds_tab.getString("default.prefix"));
            for (String s : this.teams.keySet()) {
                try {
                    Team team1 = this.board.registerNewTeam(s);
                    team1.setPrefix(this.teams.get(s));
                } catch (Exception exception) {}
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                String TEAMNAME = CachePlayer.getTeamName.get(all.getUniqueId().toString());
                if (TEAMNAME == null) {
                    (new CachePlayer()).loadPlayer(all);
                    TEAMNAME = CachePlayer.getTeamName.get(all.getUniqueId().toString());
                }
                this.board.getTeam(TEAMNAME).addPlayer((OfflinePlayer)all);
            }
            String teamname = CachePlayer.getTeamName.get(this.p.getUniqueId().toString());
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all != this.p &&
                        CachePlayer.getBoard.containsKey(all.getUniqueId().toString()))
                    ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).board.getTeam(teamname).addPlayer((OfflinePlayer)this.p);
            }
            setBoard();
        } else {
            this.personalTeam = getTeamClanTag(this.p);
            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all != this.p) {
                        CoreScoreBoard cb = CachePlayer.getBoard.get(all.getUniqueId().toString());
                        if (cb != null) {
                            Team team = cb.board.registerNewTeam(this.personalTeam[0]);
                            team.setPrefix(this.personalTeam[1]);
                            team.setSuffix(this.personalTeam[2]);
                            team.addPlayer((OfflinePlayer)this.p);
                            Team team2 = this.board.registerNewTeam(cb.personalTeam[0]);
                            team2.setPrefix(cb.personalTeam[1]);
                            team2.setSuffix(cb.personalTeam[2]);
                            team2.addPlayer((OfflinePlayer)all);
                        }
                    }
                }
                Team team1 = this.board.registerNewTeam(this.personalTeam[0]);
                team1.setPrefix(this.personalTeam[1]);
                team1.setSuffix(this.personalTeam[2]);
                team1.addPlayer((OfflinePlayer)this.p);
            }
            setBoard();
        }
        CachePlayer.getBoard.put(this.p.getUniqueId().toString(), this);
    }

    public String[] getTeamClanTag(Player p) {
        String[] array = { "", "", "" };
        array[0] = (String)CachePlayer.getTeamName.get(p.getUniqueId().toString()) + "" + randomInt(p.getUniqueId().toString());
        array[1] = this.teams.get(CachePlayer.getTeamName.get(p.getUniqueId().toString()));
        List<String> info = ScoreboardUtils.instance.getClantag(p.getUniqueId().toString());
        if (p.hasMetadata("spec")) {
            array[2] = BuildFFA.spec_suffix;
        } else if (!((String)info.get(0)).equals("none")) {
            array[2] = " + (String)info.get(1) + (String)info.get(0) + ";
        }
        return array;
    }

    private String[] getFakeTeamClanTag(Player p) {
        String[] array = { "", "", "" };
        array[0] = (String)CachePlayer.getTeamName.get(p.getUniqueId().toString()) + "" + randomInt(p.getUniqueId().toString());
        array[1] = this.teams.get(CachePlayer.getTeamName.get(p.getUniqueId().toString()));
        return array;
    }

    private void setBoard() {
        this.p.setScoreboard(this.board);
    }

    private String randomInt(String uuid) {
        Random r = new Random();
        int i = r.nextInt(1000);
        String id = uuid.split("-")[2] + i;
        return id;
    }

    public void nickPlayer() {
        CachePlayer.getTeamName.remove(this.p.getUniqueId().toString());
        CachePlayer.getTeamName.put(this.p.getUniqueId().toString(), (new CachePlayer()).getTeamPlayer(this.p));
        if (!this.clantag.booleanValue()) {
            String teamname = CachePlayer.getTeamName.get(this.p.getUniqueId().toString());
            if (teamname == null)
                teamname = (new CachePlayer()).getTeamPlayer(this.p);
            for (Player all : Bukkit.getOnlinePlayers())
                ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).board.getTeam(teamname).addPlayer((OfflinePlayer)this.p);
        } else {
            String oldname = this.personalTeam[0];
            this.personalTeam = getFakeTeamClanTag(this.p);
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all != this.p) {
                    CoreScoreBoard cb = CachePlayer.getBoard.get(all.getUniqueId().toString());
                    cb.board.getTeam(oldname).unregister();
                    Team team = cb.board.registerNewTeam(this.personalTeam[0]);
                    team.setPrefix(this.personalTeam[1]);
                    team.addPlayer((OfflinePlayer)this.p);
                }
            }
            this.board.getTeam(oldname).unregister();
            Team team1 = this.board.registerNewTeam(this.personalTeam[0]);
            team1.setPrefix(this.personalTeam[1]);
            team1.addPlayer((OfflinePlayer)this.p);
        }
    }

    public void unnickPlayer() {
        CachePlayer.getTeamName.remove(this.p.getUniqueId().toString());
        CachePlayer.getTeamName.put(this.p.getUniqueId().toString(), (new CachePlayer()).getTeamPlayer(this.p));
        if (!this.clantag.booleanValue()) {
            String teamname = CachePlayer.getTeamName.get(this.p.getUniqueId().toString());
            if (teamname == null)
                teamname = (new CachePlayer()).getTeamPlayer(this.p);
            for (Player all : Bukkit.getOnlinePlayers())
                ((CoreScoreBoard)CachePlayer.getBoard.get(all.getUniqueId().toString())).board.getTeam(teamname).addPlayer((OfflinePlayer)this.p);
        } else {
            String oldname = this.personalTeam[0];
            this.personalTeam = getTeamClanTag(this.p);
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all != this.p) {
                    CoreScoreBoard cb = CachePlayer.getBoard.get(all.getUniqueId().toString());
                    cb.board.getTeam(oldname).unregister();
                    Team team = cb.board.registerNewTeam(this.personalTeam[0]);
                    team.setPrefix(this.personalTeam[1]);
                    team.setSuffix(this.personalTeam[2]);
                    team.addPlayer((OfflinePlayer)this.p);
                }
            }
            this.board.getTeam(oldname).unregister();
            Team team1 = this.board.registerNewTeam(this.personalTeam[0]);
            team1.setPrefix(this.personalTeam[1]);
            team1.setSuffix(this.personalTeam[2]);
            team1.addPlayer((OfflinePlayer)this.p);
        }
    }

    private List<String> getScoreboardColor() {
        List<String> list = new ArrayList<String>();
        list.add(ChatColor.BLACK.toString());
        list.add(ChatColor.DARK_AQUA.toString());
        list.add(ChatColor.AQUA.toString());
        list.add(ChatColor.BLUE.toString());
        list.add(ChatColor.DARK_BLUE.toString());
        list.add(ChatColor.DARK_GRAY.toString());
        list.add(ChatColor.YELLOW.toString());
        list.add(ChatColor.WHITE.toString());
        list.add(ChatColor.DARK_GREEN.toString());
        list.add(ChatColor.GREEN.toString());
        list.add(ChatColor.DARK_RED.toString());
        list.add(ChatColor.RED.toString());
        list.add(ChatColor.GOLD.toString());
        list.add(ChatColor.GRAY.toString());
        list.add(ChatColor.LIGHT_PURPLE.toString());
        list.add(ChatColor.DARK_PURPLE.toString());
        return list;
    }

    public void updateCoins(Player p) {
        if (CacheScoreboard.getObject_ScoreBoard.containsKey("pos_coins")) {
            String prefix;
            Team team = this.board.getTeam("st" + CacheScoreboard.getObject_ScoreBoard.get("pos_coins"));
            String s = "" + CacheScoreboard.getObject_ScoreBoard.get("field_coins");
            s = s.replace("%COINS%", "" + BuildFFA.mysqlMethods.loadIntFromCache(p.getUniqueId().toString(), "coinsTable", "coins"));
            String suffix = "";
            if (s.length() >= 16) {
                prefix = s.substring(0, 16);
                if (s.length() >= 17)
                    suffix = ChatColor.getLastColors(prefix) + s.substring(16);
                if (!suffix.equals("") && suffix.length() >= 16)
                    suffix = suffix.substring(0, 16);
            } else {
                prefix = s;
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }
    }

    public void updateKills(Player p) {
        if (CacheScoreboard.getObject_ScoreBoard.containsKey("pos_kills")) {
            String prefix;
            Team team = this.board.getTeam("st" + CacheScoreboard.getObject_ScoreBoard.get("pos_kills"));
            String s = "" + CacheScoreboard.getObject_ScoreBoard.get("field_kills");
            s = s.replace("%KILLS%", "" + BuildFFA.mysqlMethods.loadIntFromCache(p.getUniqueId().toString(), BuildFFA.getMySQLTable, "kills"));
            String suffix = "";
            if (s.length() >= 16) {
                prefix = s.substring(0, 16);
                if (s.length() >= 17)
                    suffix = ChatColor.getLastColors(prefix) + s.substring(16);
                if (!suffix.equals("") && suffix.length() >= 16)
                    suffix = suffix.substring(0, 16);
            } else {
                prefix = s;
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }
    }

    public void updateDeath(Player p) {
        if (CacheScoreboard.getObject_ScoreBoard.containsKey("pos_death")) {
            String prefix;
            Team team = this.board.getTeam("st" + CacheScoreboard.getObject_ScoreBoard.get("pos_death"));
            String s = "" + CacheScoreboard.getObject_ScoreBoard.get("field_death");
            s = s.replace("%DEATH%", "" + BuildFFA.mysqlMethods.loadIntFromCache(p.getUniqueId().toString(), BuildFFA.getMySQLTable, "death"));
            String suffix = "";
            if (s.length() >= 16) {
                prefix = s.substring(0, 16);
                if (s.length() >= 17)
                    suffix = ChatColor.getLastColors(prefix) + s.substring(16);
                if (!suffix.equals("") && suffix.length() >= 16)
                    suffix = suffix.substring(0, 16);
            } else {
                prefix = s;
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }
    }

    public void updateKD(Player p) {
        if (CacheScoreboard.getObject_ScoreBoard.containsKey("pos_kd")) {
            String prefix;
            Team team = this.board.getTeam("st" + CacheScoreboard.getObject_ScoreBoard.get("pos_kd"));
            String s = "" + CacheScoreboard.getObject_ScoreBoard.get("field_kd");
            s = s.replace("%KD%", "" + BuildFFACore.instance.getKDFromUUID(p.getUniqueId().toString()));
            String suffix = "";
            if (s.length() >= 16) {
                prefix = s.substring(0, 16);
                if (s.length() >= 17)
                    suffix = ChatColor.getLastColors(prefix) + s.substring(16);
                if (!suffix.equals("") && suffix.length() >= 16)
                    suffix = suffix.substring(0, 16);
            } else {
                prefix = s;
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }
    }

    public void updateRanking(Player p) {
        if (CacheScoreboard.getObject_ScoreBoard.containsKey("pos_ranking")) {
            String prefix;
            Team team = this.board.getTeam("st" + CacheScoreboard.getObject_ScoreBoard.get("pos_ranking"));
            String s = "" + CacheScoreboard.getObject_ScoreBoard.get("field_ranking");
            s = s.replace("%RANKING%", "" + BuildFFA.mysqlMethods.getRankingValues(p.getUniqueId().toString()));
            String suffix = "";
            if (s.length() >= 16) {
                prefix = s.substring(0, 16);
                if (s.length() >= 17)
                    suffix = ChatColor.getLastColors(prefix) + s.substring(16);
                if (!suffix.equals("") && suffix.length() >= 16)
                    suffix = suffix.substring(0, 16);
            } else {
                prefix = s;
            }
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }
    }
}
