package gamepub.db.entity;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Анатолий on 25.02.2016.
 */
@Entity
@Table(name = "USER_TRANSACTIONS")
public class UserTransaction {

    //ID	TYPE SIGNATURE OUT_SUMM	PAYMENT_DATE PAYMENT_STATUS USER_ID
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "OUT_SUMM", nullable = true)
    Integer outSumm;
    @Column(name = "PAYMENT_STATUS", nullable = false)
    boolean status;
    @Column(name = "PAYMENT_DATE", nullable = false)
    Date date;
    @Column(name = "DESCRIPTION")
    String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOutSumm() {
        return outSumm;
    }

    public void setOutSumm(int outSumm) {
        this.outSumm = outSumm;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
