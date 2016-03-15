/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api.resources;

import gamepub.api.Interceptors.ValidationInterceptor;
import gamepub.api.annotations.Secure;
import gamepub.db.entity.Mark;
import gamepub.db.entity.User;
import gamepub.db.service.GameService;
import gamepub.db.service.MarkService;
import gamepub.db.service.UserService;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Ivan
 */
@Path("/reviews")
@Stateless
public class ReviewsResource {

    @EJB
    MarkService markService;
    @EJB
    UserService userService;
    @EJB
    GameService gameService;


    @POST
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Secure
    public String addMark(MultivaluedMap<String, String> form, @HeaderParam("token") String token) {
        try {
            Mark mark = new Mark();
            mark.setDate(new Date());
            mark.setUser(userService.getUserByApiToken(token));
            mark.setReview(form.getFirst("review"));
            mark.setGame(gameService.getGameById(Integer.parseInt(form.getFirst("gameId"))));
            if (Integer.parseInt(form.getFirst("mark")) >= 1 && Integer.parseInt(form.getFirst("mark")) <= 5) {
                mark.setMark(Integer.parseInt(form.getFirst("mark")));
            } else {
                return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"INCORRECT MARK\"}";
            }
            markService.create(mark);
            return "{" +
                    " \"STATUS\": \"OK\"," +
                    "\"message\": \"SUCCESS\"}";
        } catch (Exception e) {
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }

    @DELETE
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Secure
    public String deleteReview(MultivaluedMap<String, String> form, @HeaderParam("token") String token) {
        try {
            Mark mark = markService.getMarkById(Integer.parseInt(form.getFirst("reviewId")));
            User user = userService.getUserByApiToken(token);
            if (mark.getUser().equals(user)) {
                markService.delete(Integer.parseInt(form.getFirst("reviewId")));
                return "{" +
                        " \"STATUS\": \"OK\"," +
                        "\"message\": \"SUCCESS\"}";
            } else {
                return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"INCORRECT USER\"}";
            }
        } catch (Exception e) {
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }
}
