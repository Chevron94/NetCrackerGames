package gamepub.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "GAME")
public class Game implements SearchItem {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name="UID",nullable = true)
    String uid;
    @Column(name = "NAME", nullable = false)
    String name;
    @Column(name = "LINK_TO_STEAM", nullable = true)
    String linkToSteam;
    @Column(name = "LINK_TO_PLAYSTATION_STORE", nullable = true)
    String linkToSonyPlaystationStore;
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT", nullable = false)
    String description;
    @Column(name = "POSTER", nullable = false)
    String poster;

    @Column(name = "STEAM_ID", nullable = true)
    int steamId;

    @Column(name = "AVG_USER_MARK", nullable = true)
    Double avgMark;
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    List<GamePlatform> gamePlatforms;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    List<GameScreenshot> gameScreenshots;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    List<GameGenre> gameGenres;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    List<Tournament> tournaments;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    List<Mark> marks;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    List<UserGame> userGames;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    List<News> newses;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offeringGame")
    List<OfferingUserTrade> OfferingUserTrades;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receivingGame")
    List<ReceivingUserTrade> ReceivingUserTrades;
  
    public Game() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkToSteam() {
        return linkToSteam;
    }

    public void setLinkToSteam(String linkToSteam) {
        this.linkToSteam = linkToSteam;
    }

    public String getLinkToSonyPlaystationStore() {
        return linkToSonyPlaystationStore;
    }

    public void setLinkToSonyPlaystationStore(String linkToSonyPlaystationStore) {
        this.linkToSonyPlaystationStore = linkToSonyPlaystationStore;
    }

    public List<GamePlatform> getGamePlatforms() {
        return gamePlatforms;
    }

    public void setGamePlatforms(List<GamePlatform> gamePlatforms) {
        this.gamePlatforms = gamePlatforms;
    }

    public List<GameGenre> getGameGenres() {
        return gameGenres;
    }

    public void setGameGenres(List<GameGenre> gameGenres) {
        this.gameGenres = gameGenres;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public List<UserGame> getUserGames() {
        return userGames;
    }

    public void setUserGames(List<UserGame> userGames) {
        this.userGames = userGames;
    }

    public List<News> getNewses() {
        return newses;
    }

    public void setNewses(List<News> newses) {
        this.newses = newses;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getSteamId() {
        return steamId;
    }

    public void setSteamId(int steamId) {
        this.steamId = steamId;
    }

    public List<GameScreenshot> getGameScreenshots() {
        return gameScreenshots;
    }

    public void setGameScreenshots(List<GameScreenshot> gameScreenshots) {
        this.gameScreenshots = gameScreenshots;
    }
    @JsonIgnore
    public List<OfferingUserTrade> getOfferingUserTrades() {
        return OfferingUserTrades;
    }

    public void setOfferingUserTrades(List<OfferingUserTrade> OfferingUserTrades) {
        this.OfferingUserTrades = OfferingUserTrades;
    }
    @JsonIgnore
    public List<ReceivingUserTrade> getReceivingUserTrades() {
        return ReceivingUserTrades;
    }


    public Double getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(Double avgMark) {
        this.avgMark = avgMark;
    }

    public void setReceivingUserTrades(List<ReceivingUserTrade> ReceivingUserTrades) {
        this.ReceivingUserTrades = ReceivingUserTrades;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;

        Game game = (Game) o;

        return id == game.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", linkToSteam='" + linkToSteam + '\'' +
                ", linkToSonyPlaystationStore='" + linkToSonyPlaystationStore + '\'' +
                ", description='" + description + '\'' +
                ", poster='" + poster + '\'' +
                ", steamId=" + steamId +
                '}';
    }

   
}
