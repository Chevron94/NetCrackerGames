package gamepub.api.resources;

import gamepub.api.Interceptors.ValidationInterceptor;
import gamepub.api.annotations.Secure;
import gamepub.db.entity.UserGame;
import gamepub.db.service.GameService;
import gamepub.db.service.UserGameService;
import gamepub.db.service.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
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

    @GET
    @Path("/")
    @Produces("application/json")
    @Secure
    public List<UserGame> getAll(@HeaderParam("token") String token){
        return userGameService.findAll();
    }

    @GET
    @Path("exchange/users/{gameId}")
    @Produces("application/json")
    @Secure
    public List<UserGame> getExchangeByGame(@HeaderParam("token") String token, @PathParam("gameId") Integer gameId){
        return userGameService.findAll();
    }

    @GET
    @Path("exchange/games/{userId}")
    @Produces("application/json")
    @Secure
    public List<UserGame> getExchangeByUser(@HeaderParam("token") String token, @PathParam("userId") Integer userId){
        return userGameService.findAll();
    }

    @GET
    @Path("wanted/users/{gameId}")
    @Produces("application/json")
    @Secure
    public List<UserGame> getWantedByGame(@HeaderParam("token") String token, @PathParam("gameId") Integer gameId){
        return userGameService.findAll();
    }

    @GET
    @Path("wanted/games/{userId}")
    @Produces("application/json")
    @Secure
    public List<UserGame> getWantedByUser(@HeaderParam("token") String token, @PathParam("userId") Integer userId){
        return userGameService.findAll();
    }
}
