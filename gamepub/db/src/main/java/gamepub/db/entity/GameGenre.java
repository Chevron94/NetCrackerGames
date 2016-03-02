package gamepub.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "GAME_GENRE")
public class GameGenre {
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
    @JoinColumn(name = "GENRE_ID", nullable = false)
    Genre genre;

    public GameGenre() {
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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameGenre)) return false;

        GameGenre gameGenre = (GameGenre) o;

        return id == gameGenre.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "GameGenre{" +
                "id=" + id +
                ", game=" + game +
                ", genre=" + genre +
                '}';
    }
}
