package gamepub.api;

import gamepub.db.entity.UserGame;
import gamepub.db.service.GameService;
import gamepub.db.service.UserGameService;
import gamepub.db.service.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import java.util.List;

/**
 * Created by roman on 28.02.16.
 */
@Path("/trading")
@Stateless
public class TradingResource {
    @EJB
    UserService userService;
    @EJB
    GameService gameService;
    @EJB
    UserGameService userGameService;

    AuthResource authResource = new AuthResource();

    @GET
    @Path("all")
    @Produces("application/json")
    public List<UserGame> getAll(@QueryParam("token") String token){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return userGameService.findAll();
            }
            default:{
                return null;
            }
        }
    }

    @GET
    @Path("exchange/games/{gameId}")
    @Produces("application/json")
    public List<UserGame> getExchangeByGame(@QueryParam("token") String token, @PathParam("gameId") Integer gameId){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return userGameService.getCanExchangeUserGamesByGameId(gameId);
            }
            default:{
                return null;
            }
        }
    }

    @GET
    @Path("exchange/users/{userId}")
    @Produces("application/json")
    public List<UserGame> getExchangeByUser(@QueryParam("token") String token, @PathParam("userId") Integer userId){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return userGameService.getCanExchangeUserGamesByGameId(userId);
            }
            default:{
                return null;
            }
        }
    }

    @GET
    @Path("wanted/games/{gameId}")
    @Produces("application/json")
    public List<UserGame> getWantedByGame(@QueryParam("token") String token, @PathParam("gameId") Integer gameId){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return userGameService.getWantedUserGamesByGameId(gameId);
            }
            default:{
                return null;
            }
        }
    }

    @GET
    @Path("wanted/users/{userId}")
    @Produces("application/json")
    public List<UserGame> getWantedByUser(@QueryParam("token") String token, @PathParam("userId") Integer userId){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return userGameService.getWantedUserGamesByUserId(userId);
            }
            default:{
                return null;
            }
        }
    }
}
