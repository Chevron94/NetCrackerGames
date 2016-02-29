package gamepub.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "GAME_PLATFORM")
public class GamePlatform {
    @JsonIgnore
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GAME_ID", nullable = false)
    Game game;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLATFORM_ID", nullable = false)
    Platform platform;

    @Column(name="SYSTEM_REQUIREMENTS", columnDefinition = "TEXT", nullable = true)
    String systemRequirements;

    @Column(name = "RELEASE_DATE")
    Date releaseDate;

    @Column(name = "METACRITIC")
    int metacritic;

    public GamePlatform() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getSystemRequirements() {
        return systemRequirements;
    }

    public void setSystemRequirements(String systemRequirements) {
        this.systemRequirements = systemRequirements;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GamePlatform)) return false;

        GamePlatform that = (GamePlatform) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "GamePlatform{" +
                "id=" + id +
                ", game=" + game +
                ", platform=" + platform +
                '}';
    }
}
