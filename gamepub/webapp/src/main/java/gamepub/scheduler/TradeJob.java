/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.scheduler;

import gamepub.db.entity.Trade;
import gamepub.db.entity.User;
import gamepub.db.service.TradeService;
import gamepub.db.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import javax.ejb.EJB;

/**
 *
 * @author fitok
 */

public class TradeJob extends TimerTask{
@EJB
TradeService tradeService;
@EJB
UserService userService;
    @Override
    public void run() {
        System.out.print("123");
        Date curDate = new Date();
       List<Trade> trades = tradeService.getTradesByStatus("inProgress");
       for (Trade trade:trades){
           if (curDate.getTime() - trade.getCreateTime().getTime() > 300000){
               if(trade.getOfferingUserPay()==false && trade.getReceivingUserPay()==true){
                   User offUser = trade.getOfferingUser();
                   offUser.setFine(400);
                   offUser.setReputation(userService.getUserById(offUser.getId()).getReputation()-1);
                   /* money back to receivingUser*/
               }
               if(trade.getReceivingUserPay()==false && trade.getOfferingUserPay()==true){
                   User recUser = trade.getReceivingUser();
                   trade.getReceivingUser().setFine(400);
                   recUser.setReputation(userService.getUserById(recUser.getId()).getReputation()-1);
                   /* money back to offeringUser*/
               }
               if(trade.getOfferingUserPay()==true && trade.getReceivingUserPay()==true){                   
                   trade.setStatus("confirmed");
                   tradeService.update(trade);
                   
               }
               if(trade.getOfferingUserPay()==false && trade.getReceivingUserPay()==false){                   
                   tradeService.delete(trade.getId());
                   
                   
               }
               
           }
       }
    }
    
}
