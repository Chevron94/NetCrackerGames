package gamepub.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "GAME_STATUS")
public class GameStatus {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "NAME", nullable = false)
    String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gameStatus")
    List<UserGame> userGames;

    public GameStatus() {
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

    public List<UserGame> getUserGames() {
        return userGames;
    }

    public void setUserGames(List<UserGame> userGames) {
        this.userGames = userGames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameStatus)) return false;

        GameStatus that = (GameStatus) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "GameStatus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
