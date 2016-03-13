/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.scheduler;
import gamepub.db.dao.implementation.TradeDaoImplementation;
import gamepub.db.dao.implementation.UserDaoImplementation;
import gamepub.db.entity.Trade;
import gamepub.db.entity.User;
import gamepub.db.service.TradeService;
import gamepub.db.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import javax.ejb.EJB;

/**
 *
 * @author fitok
 */

public class TradeJob extends TimerTask{

    @Override
    public void run() {
        System.out.print(":EXECUTED:");
        Date curDate = new Date();
        TradeDaoImplementation tdi = new TradeDaoImplementation();
        UserDaoImplementation udi = new UserDaoImplementation();
        try{
       List<Trade> trades = tdi.getTradesByStatus("inProgress");
       for (Trade trade:trades){
           if (curDate.getTime() - trade.getCreateTime().getTime() > 400000){
               
               if(trade.getOfferingUserPay()==false && trade.getReceivingUserPay()==true){
                   User offUser = trade.getOfferingUser();
                   offUser.setFine(200);
                   offUser.setReputation(udi.getUserById(offUser.getId()).getReputation()-1);
                   tdi.delete(trade.getId());
                   /* money back to receivingUser*/
               }
               if(trade.getReceivingUserPay()==false && trade.getOfferingUserPay()==true){
                   User recUser = trade.getReceivingUser();
                   trade.getReceivingUser().setFine(200);
                   recUser.setReputation(udi.getUserById(recUser.getId()).getReputation()-1);
                   tdi.delete(trade.getId());
                   /* money back to offeringUser*/
               }    
               if(trade.getReceivedByOfferingUser()==true && trade.getReceivedByReceivingUser()==true){                   
                   trade.setStatus("confirmed");
                   User recUser = trade.getReceivingUser();
                   User offUser = trade.getOfferingUser();
                   List<Trade> confirmedTrades = tdi.getTradesByStatus("confirmed");
                   for(Trade tr:confirmedTrades){
                       if(!((tr.getOfferingUser().getId()==recUser.getId() && tr.getReceivingUser().getId()==offUser.getId())
                               || tr.getOfferingUser().getId()==offUser.getId() && tr.getReceivingUser().getId()==recUser.getId())){
                           recUser.setReputation(udi.getUserById(recUser.getId()).getReputation()+1);
                   recUser.setReputation(udi.getUserById(offUser.getId()).getReputation()+1);
                   udi.update(offUser);
                   udi.update(recUser);
                       }
                   }
                   
                   tdi.update(trade);
                   
               }
               if(trade.getOfferingUserPay()==false && trade.getReceivingUserPay()==false){                   
                   tdi.delete(trade.getId());
                   
                   
               }
               
           }
       }
        
        }catch(Exception e ){e.printStackTrace();}
    }
    
}
