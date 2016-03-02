/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;

import gamepub.db.entity.Game;
import gamepub.db.entity.OfferingUserTrade;
import gamepub.db.entity.ReceivingUserTrade;
import gamepub.db.entity.Trade;
import gamepub.db.entity.User;
import gamepub.db.entity.UserGame;
import gamepub.db.service.GameService;
import gamepub.db.service.OfferingUserTradeService;
import gamepub.db.service.ReceivingUserTradeService;
import gamepub.db.service.TradeService;
import gamepub.db.service.UserGameService;
import gamepub.db.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author fitok
 */
@ManagedBean
@SessionScoped
public class TradeOfferBean {
    @EJB
    TradeService tradeService;
    @EJB
    OfferingUserTradeService offeringUserTradeService;
    @EJB
    ReceivingUserTradeService receivingUserTradeService;
    @EJB
    UserService userService;
    @EJB
    GameService gameService;
    @EJB
    UserGameService userGameService;


private DualListModel<Game> myGames;
private DualListModel<Game> tradeUserGames;
private ArrayList<Game> myWantedGamesSource; 
private ArrayList<Game> myWantedGamesTarget;
private ArrayList<Game> userWantedGamesSource;
private ArrayList<Game> userWantedGamesTarget;

 public DualListModel<Game> getMyGames() {     
      myWantedGamesSource = new ArrayList<Game>(); 
      myWantedGamesTarget = new ArrayList<Game>(); 
        List<UserGame> wantedUserGameList = userGameService.getWantedUserGamesByUserId(SessionBean.getUserId());
        for(UserGame ug:wantedUserGameList){
        myWantedGamesSource.add(ug.getGame());}  
        setMyGames((DualListModel<Game>) new DualListModel(myWantedGamesSource,myWantedGamesTarget));
        return myGames;
    }
    
  public DualListModel<Game> getTradeUserGames() {     
      userWantedGamesSource = new ArrayList<Game>(); 
      userWantedGamesTarget = new ArrayList<Game>(); 
        List<UserGame> wantedUserGameList = userGameService.getWantedUserGamesByUserId(SessionBean.getUserForTradeId());
        for(UserGame ug:wantedUserGameList){
        userWantedGamesSource.add(ug.getGame());}  
        setTradeUserGames((DualListModel<Game>) new DualListModel(userWantedGamesSource,userWantedGamesTarget));
        return tradeUserGames;
  }
   
    public void openOfferDialog(User u){
       SessionBean.getSession().setAttribute("tradeuser",u.getId());
       Map<String,Object> options = new HashMap<String, Object>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("contentWidth", 1400);
        options.put("contentHeight", 800);
        options.put("modal", true);         
        RequestContext.getCurrentInstance().openDialog("tradeOffer",options,null);
    }
    
    public void sendOffer(){
       Trade trade = new Trade();
       trade.setStatus(Boolean.TRUE);
       trade.setOfferingUser(userService.getUserById(SessionBean.getUserId()));
       trade.setReceivingUser(userService.getUserById(SessionBean.getUserForTradeId()));
       tradeService.create(trade);
       Trade currentTrade = tradeService.getLastTradeByOfferingUserId(SessionBean.getUserId());
       for(Game g:myGames.getTarget()){
           OfferingUserTrade out = new OfferingUserTrade();
           out.setOfferingGame(g);
           out.setOfferingTrade(currentTrade);        
        offeringUserTradeService.create(out);
       } 
     
       for(Game g:tradeUserGames.getTarget()){
           ReceivingUserTrade rut = new ReceivingUserTrade();
           rut.setReceivingGame(g);
           rut.setReceivingTrade(currentTrade);       
        receivingUserTradeService.create(rut);
       } 
              
       SessionBean.getSession().removeAttribute("tradeuser");             
        RequestContext.getCurrentInstance().closeDialog("tradeOffer");
    }

    /**
     * @param myGames the myGames to set
     */
    public void setMyGames(DualListModel<Game> myGames) {
        this.myGames = myGames;
    }

    /**
     * @param tradeUserGames the tradeUserGames to set
     */
    public void setTradeUserGames(DualListModel<Game> tradeUserGames) {
        this.tradeUserGames = tradeUserGames;
    }
    
         
        
    }
    

