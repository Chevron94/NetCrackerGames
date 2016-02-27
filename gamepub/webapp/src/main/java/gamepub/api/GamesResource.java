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

    @GET
    @Path("/allGames")
    @Produces("application/json")
    public List<Game> getAllGames() {
        return gameService.findAll();
    }
    
    @GET
    @Path("/id/{id}")
    @Produces("application/json")
    public Game getOneGameById(@PathParam("id") Integer id){
        return gameService.find(id);
    }
    
    @GET
    @Path("/{id}/reviews")
    @Produces("application/json")
    public List<Mark> getReviews(@PathParam("id") Integer id){
        return markService.getMarksByGameId(id);
    }
}
