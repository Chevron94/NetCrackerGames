package gamepub.api;

import gamepub.beans.SessionBean;
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

    AuthResource authResource = new AuthResource();

    @GET
    @Path("all")
    @Produces("application/json")
    public List<User> getAll(@QueryParam("token") String token){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth) {
            case OK: {
                return userService.findAll();
            }
            default: {
                return null;
            }
        }

    }

    @GET
    @Path("{userId}")
    @Produces("application/json")
    public User getOne(@PathParam("userId") Integer userId, @QueryParam("token") String token){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth) {
            case OK: {
                return userService.getUserById(userId);
            }
            default: {
                return null;
            }
        }
    }

    @GET
    @Path("{userId}/comments")
    @Produces("application/json")
    public List<Comment> getComments(@PathParam("userId") Integer userId, @QueryParam("token") String token){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth) {
            case OK: {
                return commentService.getCommentsByUserId(userId);
            }
            default: {
                return null;
            }
        }
    }

    @GET
    @Path("{userId}/reviews")
    @Produces("application/json")
    public List<Mark> getReviews(@PathParam("userId") Integer userId, @QueryParam("token") String token){
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth) {
            case OK: {
                return markService.getMarksByUserId(userId);
            }
            default: {
                return null;
            }
        }
    }

    @POST
    @Path("{userID}/update")
    @Consumes("application/x-www-form-urlencoded")
    public String updateProfile(@PathParam("userID") Integer userId,
                                MultivaluedMap<String, String> form){
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)){
                case ALL_REQUESTS_ARE_USED: return "ALL REQUESTS FOR TODAY ARE USED";
                case WRONG_TOKEN: return "WRONG TOKEN";
                case BANNED: return "YOU WAS BANNED";
                case TOKEN_EXPIRED: return "TOKEN EXPIRED";
                case OK:{
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
                        return "OK";
                    }return "FAIL";
                }
                default: return "FAIL";
            }
        }catch (Exception e){
            return "FAIL";
        }

    }
}
