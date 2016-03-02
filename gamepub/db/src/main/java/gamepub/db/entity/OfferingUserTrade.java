package gamepub.db.entity;

import javax.persistence.*;
/**
 *
 * @author fitok
 */
@Entity
@Table(name = "OFFERING_USER_TRADE")
public class OfferingUserTrade {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRADE_ID", nullable = false)
    Trade offeringTrade;   
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OFFERING_GAME_ID", nullable = false)
    Game offeringGame;

    public OfferingUserTrade() {
    }


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the offeringTrade
     */
    public Trade getOfferingTrade() {
        return offeringTrade;
    }

    /**
     * @param offeringTrade the offeringTrade to set
     */
    public void setOfferingTrade(Trade offeringTrade) {
        this.offeringTrade = offeringTrade;
    }

    /**
     * @return the offeringGame
     */
    public Game getOfferingGame() {
        return offeringGame;
    }

    /**
     * @param offeringGame the offeringGame to set
     */
    public void setOfferingGame(Game offeringGame) {
        this.offeringGame = offeringGame;
    }

   

    

    
}
