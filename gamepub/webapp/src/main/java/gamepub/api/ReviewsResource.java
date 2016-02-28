/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api;
//TODO Ограничение API: неавторизован - 100/день, авторизован - 1000/день, прем - неограниченно
//TODO Добавить OAuth2

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

    AuthResource authResource = new AuthResource();

    @POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    public String addMark(MultivaluedMap<String, String> form) {
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)) {
                case ALL_REQUESTS_ARE_USED:
                    return "ALL REQUESTS FOR TODAY ARE USED";
                case WRONG_TOKEN:
                    return "WRONG TOKEN";
                case BANNED:
                    return "YOU WAS BANNED";
                case TOKEN_EXPIRED:
                    return "TOKEN EXPIRED";
                case OK: {
                    Mark mark = new Mark();
                    mark.setDate(new Date());
                    mark.setUser(userService.getUserByApiToken(token));
                    mark.setReview(form.getFirst("review"));
                    mark.setGame(gameService.getGameById(Integer.parseInt(form.getFirst("gameId"))));
                    if (Integer.parseInt(form.getFirst("mark")) >= 1 && Integer.parseInt(form.getFirst("mark")) <= 5) {
                        mark.setMark(Integer.parseInt(form.getFirst("mark")));
                    } else {
                        return "incorrect mark";
                    }
                    markService.create(mark);
                    return "ok";
                }
                default:
                    return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }

    @POST
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    public String deleteReview(MultivaluedMap<String, String> form) {
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)) {
                case ALL_REQUESTS_ARE_USED:
                    return "ALL REQUESTS FOR TODAY ARE USED";
                case WRONG_TOKEN:
                    return "WRONG TOKEN";
                case BANNED:
                    return "YOU WAS BANNED";
                case TOKEN_EXPIRED:
                    return "TOKEN EXPIRED";
                case OK: {
                    Mark mark = markService.getMarkById(Integer.parseInt(form.getFirst("reviewId")));
                    User user = userService.getUserByApiToken(token);
                    if (mark.getUser().equals(user)) {
                        markService.delete(Integer.parseInt(form.getFirst("reviewId")));
                        return "ok";
                    } else {
                        return "error";
                    }
                }
                default:
                    return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }
}
