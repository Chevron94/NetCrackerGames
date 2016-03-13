package gamepub.api.resources;

import gamepub.api.Interceptors.ValidationInterceptor;
import gamepub.api.annotations.Secure;
import gamepub.db.entity.Comment;
import gamepub.db.entity.Mark;
import gamepub.db.entity.User;
import gamepub.db.service.CityService;
import gamepub.db.service.CommentService;
import gamepub.db.service.MarkService;
import gamepub.db.service.UserService;
import gamepub.encode.shaCode;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
    @Path("/")
    @Produces("application/json")
    @Secure
    public List<User> getAll(@QueryParam("token") String token){
        return userService.findAll();

    }

    @GET
    @Path("{userId}")
    @Produces("application/json")
    @Secure
    public User getOne(@PathParam("userId") Integer userId, @QueryParam("token") String token){
        return userService.getUserById(userId);
    }

    @GET
    @Path("{userId}/comments")
    @Produces("application/json")
    @Secure
    public List<Comment> getComments(@PathParam("userId") Integer userId, @QueryParam("token") String token){
        return commentService.getCommentsByUserId(userId);
    }

    @GET
    @Path("{userId}/reviews")
    @Produces("application/json")
    @Secure
    public List<Mark> getReviews(@PathParam("userId") Integer userId, @QueryParam("token") String token){
        return markService.getMarksByUserId(userId);
    }

    @POST
    @Path("{userID}")
    @Consumes("application/x-www-form-urlencoded")
    @Secure
    public String updateProfile(@PathParam("userID") Integer userId,
                                MultivaluedMap<String, String> form){
        try {
            String token = form.getFirst("token");
            if (userService.getUserByApiToken(token).getId() == userId) {
                User user = userService.getUserById(userId);
                String password = form.getFirst("password");
                String avatar = form.getFirst("avatar");
                String cityId = form.getFirst("cityId");
                if (password != null && !password.trim().equals("")) {
                    try {
                        String hashPass = shaCode.code(shaCode.code(user.getLogin()) + password);
                        password = hashPass;
                        user.setPassword(password);
                    } catch (NoSuchAlgorithmException e) {

                    } catch (UnsupportedEncodingException e) {

                    }
                }
                if (avatar != null && !avatar.trim().equals(""))
                    user.setAvatarUrl(avatar);
                if (cityId != null && cityId.trim().equals("")) {
                    user.setCity(cityService.getCityById(Integer.valueOf(cityId)));
                }
                userService.update(user);
                return "{" +
                        " \"STATUS\": \"OK\"," +
                        "\"message\": \"SUCCESS\"}";
            }return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"ACCESS DENIED\"}";
        }catch (Exception e){
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }

    }
}
