/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;

import gamepub.db.entity.User;
import gamepub.db.entity.UserGame;
import gamepub.db.service.GameService;
import gamepub.db.service.UserGameService;
import gamepub.db.service.UserService;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author fitok
 */
@ManagedBean
@RequestScoped
public class TradingLoungeBean {
    @EJB
    UserService userService;
    @EJB
    GameService gameService;
    @EJB
    UserGameService userGameService;

    private List<User> users;
    /**
     * Creates a new instance of TradingLoungeBean
     */
    public TradingLoungeBean() {
    }

    /**
     * @return the users
     */
    public List<User> getUsers() {
        users = userService.findAll();
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public List<UserGame> getWantedGames(User u) {       
        return userGameService.getWantedUserGamesByUserId(u.getId());
    }
    public List<UserGame> getExchangeGames(User u) {       
        return userGameService.getCanExchangeUserGamesByUserId(u.getId());
    }
    public boolean noWantedGamesCheck(User u){
       return (getWantedGames(u).isEmpty());
   
    }
    public boolean noExchangeGamesCheck(User u){
       return (getExchangeGames(u).isEmpty());
   
    }
}
