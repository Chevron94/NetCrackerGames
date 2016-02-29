package gamepub.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "PRIVATE_MESSAGE")
public class PrivateMessage {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "DATE", nullable = false)
    Date date;

    @Column(name = "TEXT", columnDefinition = "TEXT", nullable = false)
    String text;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SENDER_ID", nullable = false)
    User sender;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RECEIVER_ID", nullable = false)
    User receiver;

    public PrivateMessage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrivateMessage)) return false;

        PrivateMessage that = (PrivateMessage) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
