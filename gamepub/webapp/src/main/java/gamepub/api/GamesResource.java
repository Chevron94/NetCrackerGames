/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api;

import gamepub.db.entity.Game;
import gamepub.db.entity.Mark;
import gamepub.db.service.GameService;
import gamepub.db.service.MarkService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;

/**
 *
 * @author Ivan
 */
@Stateless
@Path("/games")
public class GamesResource {

    @EJB
    GameService gameService;
    @EJB
    MarkService markService;
    @EJB
    AuthResource authResource;

    @GET
    @Path("/allGames")
    @Produces("application/json")
    public List<Game> getAllGames(@QueryParam("token") String token) {
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return gameService.findAll();
            }
            default:{
                return null;
            }
        }        
    }
    
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Game getOneGameById(@PathParam("id") Integer id, @QueryParam("token") String token){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return gameService.find(id);
            }
            default:{
                return null;
            }
        }
    }
    
    @GET
    @Path("/{id}/reviews")
    @Produces("application/json")
    public List<Mark> getReviews(@PathParam("id") Integer id, @QueryParam("token") String token){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth){
            case OK:{
                return markService.getMarksByGameId(id);
            }
            default:{
                return null;
            }
        }
    }
}
