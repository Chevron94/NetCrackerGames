/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api.resources;

import gamepub.api.Interceptors.ValidationInterceptor;
import gamepub.api.annotations.Secure;
import gamepub.db.entity.Game;
import gamepub.db.entity.Mark;
import gamepub.db.service.GameService;
import gamepub.db.service.GenreService;
import gamepub.db.service.MarkService;

import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
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
    GenreService genreService;

    @GET
    @Path("/")
    @Produces("application/json")
    @Secure
    public List<Game> getAllGames(@HeaderParam("token") String token,
                                  @DefaultValue("0") @QueryParam("start") String startStr,
                                  @DefaultValue("") @QueryParam("name") String name,
                                  @DefaultValue("") @QueryParam("genre") String genre,
                                  @DefaultValue("") @QueryParam("date") String date) {
        try {
            Integer start = Integer.valueOf(startStr);
            List<Map.Entry<String, Object>> params = new ArrayList<Map.Entry<String, Object>>();
            if (name != null && !name.trim().equals("")) {
                params.add(new HashMap.SimpleEntry<String, Object>("name", name));
            }
            if (date != null && !date.trim().equals("")){
                params.add(new HashMap.SimpleEntry<String, Object>("date",new Date(date)));
            }
            if (genre != null && !genre.trim().equals("")){
                params.add(new HashMap.SimpleEntry<String, Object>("genre",genreService.getGenreByName(genre)));
            }
            return gameService.getGamesByCustomParams(params, false, start, 100);
        }catch (Exception e){
            return null;
        }
    }
    
    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Secure
    public Game getOneGameById(@PathParam("id") Integer id, @HeaderParam("token") String token){
        return gameService.find(id);
    }
    
    @GET
    @Path("/{id}/reviews")
    @Produces("application/json")
    @Secure
    public List<Mark> getReviews(@PathParam("id") Integer id, @HeaderParam("token") String token){
        return markService.getMarksByGameId(id);
    }
}
