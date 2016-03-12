/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api;

import gamepub.db.entity.Mark;
import gamepub.db.entity.User;
import gamepub.db.service.GameService;
import gamepub.db.service.MarkService;
import gamepub.db.service.UserService;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    @EJB
    AuthResource authResource;

    @POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    public String addMark(MultivaluedMap<String, String> form) {
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)) {
                case ALL_REQUESTS_ARE_USED: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"ALL REQUESTS FOR TODAY ARE USED\"}";
                case WRONG_TOKEN: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"WRONG TOKEN\"}";
                case BANNED: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"YOU WAS BANNED\"}";
                case TOKEN_EXPIRED: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"TOKEN EXPIRED\"}";
                case OK: {
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
                }
                default:
                    return "{" +
                            " \"STATUS\": \"ERROR\"," +
                            "\"message\": \"UNKNOWN ERROR\"}";
            }
        } catch (Exception e) {
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }

    @POST
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    public String deleteReview(MultivaluedMap<String, String> form) {
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)) {
                case ALL_REQUESTS_ARE_USED: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"ALL REQUESTS FOR TODAY ARE USED\"}";
                case WRONG_TOKEN: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"WRONG TOKEN\"}";
                case BANNED: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"YOU WAS BANNED\"}";
                case TOKEN_EXPIRED: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"TOKEN EXPIRED\"}";
                case OK: {
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
                }
                default:
                    return "{" +
                            " \"STATUS\": \"ERROR\"," +
                            "\"message\": \"UNKNOWN ERROR\"}";
            }
        } catch (Exception e) {
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }
}
