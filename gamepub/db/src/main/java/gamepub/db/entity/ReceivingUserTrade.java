package gamepub.db.entity;

import javax.persistence.*;
/**
 *
 * @author fitok
 */
@Entity
@Table(name = "RECEIVING_USER_TRADE")
public class ReceivingUserTrade {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRADE_ID", nullable = false)
    Trade receivingTrade;   
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RECEIVING_GAME_ID", nullable = false)
    Game receivingGame;


    public ReceivingUserTrade() {
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
     * @return the receivingTrade
     */
    public Trade getReceivingTrade() {
        return receivingTrade;
    }

    /**
     * @param receivingTrade the receivingTrade to set
     */
    public void setReceivingTrade(Trade receivingTrade) {
        this.receivingTrade = receivingTrade;
    }

    /**
     * @return the receivingGame
     */
    public Game getReceivingGame() {
        return receivingGame;
    }

    /**
     * @param receivingGame the receivingGame to set
     */
    public void setReceivingGame(Game receivingGame) {
        this.receivingGame = receivingGame;
    }

    

    
}
