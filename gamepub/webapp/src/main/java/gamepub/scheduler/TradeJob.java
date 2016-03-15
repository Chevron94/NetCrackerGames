/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.scheduler;
import gamepub.db.dao.implementation.OfferingUserTradeDaoImplementation;
import gamepub.db.dao.implementation.ReceivingUserTradeDaoImplementation;
import gamepub.db.dao.implementation.TradeDaoImplementation;
import gamepub.db.dao.implementation.UserDaoImplementation;
import gamepub.db.dao.implementation.UserGameDaoImplementation;
import gamepub.db.entity.OfferingUserTrade;
import gamepub.db.entity.ReceivingUserTrade;
import gamepub.db.entity.Trade;
import gamepub.db.entity.User;
import gamepub.db.entity.UserGame;
import gamepub.db.service.TradeService;
import gamepub.db.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
        UserGameDaoImplementation ugdi = new UserGameDaoImplementation();
        OfferingUserTradeDaoImplementation outdi = new OfferingUserTradeDaoImplementation();
        ReceivingUserTradeDaoImplementation rutdi = new ReceivingUserTradeDaoImplementation();
        try{
       List<Trade> trades = tdi.getTradesByStatus("inProgress");
       for (Trade trade:trades){
           if (curDate.getTime() - trade.getCreateTime().getTime() > 220000){
               
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
                   
                   HashSet<Integer> offSet;
                   HashSet<Integer> recSet;
                   for(User tradeUser:udi.findAll()){
                       offSet=new HashSet<Integer>();
                       recSet=new HashSet<Integer>();
                       if(tdi.getTradesByOfferingUserId(tradeUser.getId())!=null){
                      List<Trade> offUserTrades = tdi.getTradesByOfferingUserId(tradeUser.getId());
                        for(Trade ot:offUserTrades){
                          User ou = ot.getReceivingUser();                          
                          if(ot.getStatus().equals("confirmed")){
                          offSet.add(ou.getId());
                          
                          }               
                      }
                      }
                       if(tdi.getTradesByReceivingUserId(tradeUser.getId())!=null){
                      List<Trade> recUserTrades = tdi.getTradesByReceivingUserId(tradeUser.getId());
                      for(Trade rc:recUserTrades){
                          User ru = rc.getOfferingUser();
                          if(rc.getStatus().equals("confirmed")){
                          recSet.add(ru.getId());}
                                                     
                      }
                       }
                      tradeUser.setReputation(offSet.size()+recSet.size());
                      tradeUser.setTradesLeft(tradeUser.getTradesLeft()-1);
                      udi.update(tradeUser);
                       
                   }
                    /*money back to bothUsers*/
                   tdi.update(trade);
                   
               }
               if(trade.getOfferingUserPay()==false && trade.getReceivingUserPay()==false){                   
                   tdi.delete(trade.getId());
                   
                   
               }
               
           }
       }
        List<Trade> confirmedTrades = tdi.getTradesByStatus("confirmed");
        for(Trade confirmedTrade : confirmedTrades){
            User offeringUser = confirmedTrade.getOfferingUser();
            User receivingUser = confirmedTrade.getReceivingUser();
            
            List<OfferingUserTrade> offeringUserTrades =  outdi.getOfferingUserTradesByTradeId(confirmedTrade.getId());
            for(OfferingUserTrade out: offeringUserTrades){                
                if(ugdi.getUserGameByUserIdAndGameId(offeringUser.getId(),out.getOfferingGame().getId())!=null){
               UserGame userGame = ugdi.getUserGameByUserIdAndGameId(offeringUser.getId(),out.getOfferingGame().getId());               
               userGame.setCanExchange(false);
               userGame.setUser(receivingUser);
               ugdi.update(userGame);                
                }
            }
            
            
            List<ReceivingUserTrade> receivingUserTrades =  rutdi.getReceivingUserTradesByTradeId(confirmedTrade.getId());
            for(ReceivingUserTrade rut: receivingUserTrades){
                if(ugdi.getUserGameByUserIdAndGameId(receivingUser.getId(),rut.getReceivingGame().getId())!=null){
                UserGame userGame = ugdi.getUserGameByUserIdAndGameId(receivingUser.getId(),rut.getReceivingGame().getId());
                userGame.setCanExchange(false);
                userGame.setUser(offeringUser);
                ugdi.update(userGame);                
            }
            }
            
        }
       
       
       
        }catch(Exception e ){e.printStackTrace();}
    }
    
}
