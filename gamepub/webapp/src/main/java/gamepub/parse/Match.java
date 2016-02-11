/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.parse;

/**
 *
 * @author fitok
 */
public class Match {
    private String team1,team2,dataScore,tournament,status;

    /**
     * @return the team1
     */
    public String getTeam1() {
        return team1;
    }

    /**
     * @param team1 the team1 to set
     */
    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    /**
     * @return the team2
     */
    public String getTeam2() {
        return team2;
    }

    /**
     * @param team2 the team2 to set
     */
    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    /**
     * @return the dataScore
     */
    public String getDataScore() {
        return dataScore;
    }

    /**
     * @param dataScore
     */
    public void setDataScore(String dataScore) {
        this.dataScore = dataScore;
    }

    /**
     * @return the tournament
     */
    public String getTournament() {
        return tournament;
    }

    /**
     * @param tournament the tournament to set
     */
    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
