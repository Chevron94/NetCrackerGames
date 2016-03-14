package gamepub.db.entity;

import javax.persistence.*;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "FRIEND")
public class Friend {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "BLOCK", columnDefinition = "boolean default false")
    Boolean block;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SUBSCRIBER_ID", nullable = false)
    User subscriber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SUBSCRIBED_TO_ID", nullable = false)
    User subscribedTo;

    public Friend() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    public User getSubscribedTo() {
        return subscribedTo;
    }

    public void setSubscribedTo(User subscribedTo) {
        this.subscribedTo = subscribedTo;
    }

    public Boolean getBlock(){
        return block;
    }

    public void setBlock(Boolean block){
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend)) return false;

        Friend friend = (Friend) o;

        return id == friend.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", subscriber=" + subscriber +
                ", subscribedTo=" + subscribedTo +
                '}';
    }
}
