/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;

import gamepub.db.entity.Game;
import gamepub.db.entity.User;
import gamepub.db.entity.UserGame;
import gamepub.db.service.GameService;
import gamepub.db.service.UserGameService;
import gamepub.db.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author fitok
 */
@ManagedBean
@ViewScoped
public class TradeBean {
    @EJB
    UserService userService;
    @EJB
    GameService gameService;
    @EJB
    UserGameService userGameService;
    
    private List<User> users;
    private List<Game> wantedGames;
        
    public TradeBean() {
    }

    /**
     * @param game
     * @return the userList
     */
    public List<User> getUsers(Game game) {
        users = new ArrayList<User>();
         List<UserGame> exchangeUserGameList=userGameService.getCanExchangeUserGamesByGameId(game.getId());
        for(UserGame ug:exchangeUserGameList){
        users.add(ug.getUser());}
        
        return users;
        
    }

    /**
     * @param userList the userList to set
     */
    public void setUserNames(List<User> userList) {
        this.users = userList;
    }

    /**
     * @return the userGameList
     */
    

    /**
     * @return the wantedGames
     */
    public List<Game> getWantedGames() {    
        wantedGames = new ArrayList<Game>(); 
        List<UserGame> wantedUserGameList = userGameService.getWantedUserGamesByUserId(SessionBean.getUserId());
        for(UserGame ug:wantedUserGameList){
        wantedGames.add(ug.getGame());}
        
        return wantedGames;
    }

    /**
     * @param wantedGames the wantedGames to set
     */
    public void setWantedGames(List<Game> wantedGames) {
        this.wantedGames = wantedGames;
    }
    
   
    

    public boolean noUsersCheck(Game game){
       return (getUsers(game).isEmpty());
               
    }
    
   
    
    
    
}
