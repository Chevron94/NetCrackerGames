package gamepub.api;

import gamepub.beans.SessionBean;
import gamepub.db.entity.Comment;
import gamepub.db.entity.Mark;
import gamepub.db.entity.User;
import gamepub.db.service.CityService;
import gamepub.db.service.CommentService;
import gamepub.db.service.MarkService;
import gamepub.db.service.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

/**
 * Created by roman on 23.02.16.
 */
@Path("/users")
@Stateless
public class ProfileResource {
    @EJB
    UserService userService;
    @EJB
    CommentService commentService;
    @EJB
    MarkService markService;
    @EJB
    CityService cityService;

    @GET
    @Path("all")
    @Produces("application/json")
    public List<User> getAll(){
        return userService.findAll();
    }

    @GET
    @Path("{userId}")
    @Produces("application/json")
    public User getOne(@PathParam("userId") Integer userId){
        return userService.getUserById(userId);
    }

    @GET
    @Path("{userId}/comments")
    @Produces("application/json")
    public List<Comment> getComments(@PathParam("userId") Integer userId){
        return commentService.getCommentsByUserId(userId);
    }

    @GET
    @Path("{userId}/reviews")
    @Produces("application/json")
    public List<Mark> getReviews(@PathParam("userId") Integer userId){
        return markService.getMarksByUserId(userId);
    }

    @POST
    @Path("{userID}/update")
    @Consumes("application/x-www-form-urlencoded")
    public String updateProfile(@PathParam("userID") Integer userId,
                                MultivaluedMap<String, String> form){
        try {
            if (userId == SessionBean.getUserId()) {
                User user = userService.getUserById(userId);
                String password = form.getFirst("password");
                String avatar = form.getFirst("avatar");
                String cityId = form.getFirst("cityId");
                if (password != null && !password.trim().equals(""))
                    user.setPassword(password);
                if (avatar != null && !avatar.trim().equals(""))
                    user.setAvatarUrl(avatar);
                if (cityId != null && cityId.trim().equals("")) {
                    user.setCity(cityService.getCityById(Integer.valueOf(cityId)));
                }
                userService.update(user);
                return "OK";
            }return "FAIL";
        }catch (Exception e){
            return "FAIL";
        }

    }
}
