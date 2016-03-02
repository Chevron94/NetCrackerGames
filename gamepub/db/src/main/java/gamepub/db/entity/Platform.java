package gamepub.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "PLATFORM")
public class Platform {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "NAME", nullable = false)
    String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "platform")
    List<GamePlatform> gamePlatforms;

    public Platform() {
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

    public List<GamePlatform> getGamePlatforms() {
        return gamePlatforms;
    }

    public void setGamePlatforms(List<GamePlatform> gamePlatforms) {
        this.gamePlatforms = gamePlatforms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Platform)) return false;

        Platform platform = (Platform) o;

        return id == platform.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
