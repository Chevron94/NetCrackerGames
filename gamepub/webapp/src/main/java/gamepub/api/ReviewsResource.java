/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api;
//TODO Ограничение API: неавторизован - 100/день, авторизован - 1000/день, прем - неограниченно
//TODO Добавить OAuth2
import gamepub.beans.SessionBean;
import gamepub.db.entity.Mark;
import gamepub.db.service.GameService;
import gamepub.db.service.MarkService;
import gamepub.db.service.UserService;
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

    @POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    public String addMark(MultivaluedMap<String, String> form) {
        try {
 //           if (Integer.parseInt(form.getFirst("userId")) == SessionBean.getUserId()) {
                Mark mark = new Mark();
                mark.setDate(new java.util.Date());
                mark.setMark(Integer.parseInt(form.getFirst("mark")));
                mark.setReview(form.getFirst("review"));
                mark.setGame(gameService.getGameById(Integer.parseInt(form.getFirst("gameId"))));
                mark.setUser(userService.getUserById(Integer.parseInt(form.getFirst("userId"))));
                markService.create(mark);
                return "ok";
   //         } else {
    //            return "no rights to add mark and review";
     //       }
        } catch (Exception e) {
            return "error";
        }
    }

    @POST
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    public String deleteReview(MultivaluedMap<String, String> form) {
        try {
       //     if (markService.getMarkById(Integer.parseInt(form.getFirst("markId"))).getUser().getId() == SessionBean.getUserId()) {
                markService.delete(Integer.parseInt(form.getFirst("markId")));
                return "ok";
         //   } else {
          //      return "no rigthts to delete";
          //  }
        } catch (Exception e) {
            return "error";
        }
    }
}
