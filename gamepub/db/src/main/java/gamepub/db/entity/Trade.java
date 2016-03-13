package gamepub.db.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "TRADE")
public class Trade {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @ManyToOne
    @JoinColumn(name = "offeringUser",nullable = false)
    User offeringUser;
    @ManyToOne
    @JoinColumn(name = "receivingUser",nullable = false)
    User receivingUser;            
    
    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "offeringTrade",cascade = CascadeType.REMOVE)    
    List<OfferingUserTrade> offeringUserTrades;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "receivingTrade",cascade = CascadeType.REMOVE)    
    List<ReceivingUserTrade> receivingUserTrades;        
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "offeringGame",cascade = CascadeType.REMOVE)    
    List<OfferingUserTrade> offeringUserGame;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "receivingGame",cascade = CascadeType.REMOVE)
    List<ReceivingUserTrade> receivingUserGame;
    
    
    @Column(name="OFFERING_USER_PAY",columnDefinition = "boolean default false")
    Boolean offeringUserPay;
    @Column(name="RECEIVING_USER_PAY",columnDefinition = "boolean default false")
    Boolean receivingUserPay;
    @Column(name="CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)        
    Date createTime;
    @Column(name = "STATUS")
    String status;
    @Column(name = "RECEIVED_BY_OFFERING_USER",columnDefinition = "boolean default false")
    Boolean receivedByOfferingUser;
    @Column(name = "RECEIVED_BY_RECEIVING_USER",columnDefinition = "boolean default false")
    Boolean receivedByReceivingUser;
    
    @PrePersist
    protected void onCreate() {
        setCreateTime(new Date());
  }
    public Trade() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trade)) return false;

        Trade trade = (Trade) o;

        return id == trade.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
    
    

    

    /**
     * @return the offeringUserGame
     */
    public List<OfferingUserTrade> getOfferingUserGame() {
        return offeringUserGame;
    }

    /**
     * @param offeringUserGame the offeringUserGame to set
     */
    public void setOfferingUserGame(List<OfferingUserTrade> offeringUserGame) {
        this.offeringUserGame = offeringUserGame;
    }

    /**
     * @return the receivingUserGame
     */
    public List<ReceivingUserTrade> getReceivingUserGame() {
        return receivingUserGame;
    }

    /**
     * @param receivingUserGame the receivingUserGame to set
     */
    public void setReceivingUserGame(List<ReceivingUserTrade> receivingUserGame) {
        this.receivingUserGame = receivingUserGame;
    }


    /**
     * @return the offeringUser
     */
    public User getOfferingUser() {
        return offeringUser;
    }

    /**
     * @param offeringUser the offeringUser to set
     */
    public void setOfferingUser(User offeringUser) {
        this.offeringUser = offeringUser;
    }

    /**
     * @return the receivingUser
     */
    public User getReceivingUser() {
        return receivingUser;
    }

    /**
     * @param receivingUser the receivingUser to set
     */
    public void setReceivingUser(User receivingUser) {
        this.receivingUser = receivingUser;
    }

    /**
     * @return the offeringUserTrades
     */
    public List<OfferingUserTrade> getOfferingUserTrades() {
        return offeringUserTrades;
    }

    /**
     * @param offeringUserTrades the offeringUserTrades to set
     */
    public void setOfferingUserTrades(List<OfferingUserTrade> offeringUserTrades) {
        this.offeringUserTrades = offeringUserTrades;
    }

    /**
     * @return the receivingUserTrades
     */
    public List<ReceivingUserTrade> getReceivingUserTrades() {
        return receivingUserTrades;
    }

    /**
     * @param receivingUserTrades the receivingUserTrades to set
     */
    public void setReceivingUserTrades(List<ReceivingUserTrade> receivingUserTrades) {
        this.receivingUserTrades = receivingUserTrades;
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

    /**
     * @return the offeringUserPay
     */
    public Boolean getOfferingUserPay() {
        return offeringUserPay;
    }

    /**
     * @param offeringUserPay the offeringUserPay to set
     */
    public void setOfferingUserPay(Boolean offeringUserPay) {
        this.offeringUserPay = offeringUserPay;
    }

    /**
     * @return the receivingUserPay
     */
    public Boolean getReceivingUserPay() {
        return receivingUserPay;
    }

    /**
     * @param receivingUserPay the receivingUserPay to set
     */
    public void setReceivingUserPay(Boolean receivingUserPay) {
        this.receivingUserPay = receivingUserPay;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the receivedByOfferingUser
     */
    public Boolean getReceivedByOfferingUser() {
        return receivedByOfferingUser;
    }

    /**
     * @param receivedByOfferingUser the receivedByOfferingUser to set
     */
    public void setReceivedByOfferingUser(Boolean receivedByOfferingUser) {
        this.receivedByOfferingUser = receivedByOfferingUser;
    }

    /**
     * @return the receivedByReceivingUser
     */
    public Boolean getReceivedByReceivingUser() {
        return receivedByReceivingUser;
    }

    /**
     * @param receivedByReceivingUser the receivedByReceivingUser to set
     */
    public void setReceivedByReceivingUser(Boolean receivedByReceivingUser) {
        this.receivedByReceivingUser = receivedByReceivingUser;
    }

  

    
   
}
