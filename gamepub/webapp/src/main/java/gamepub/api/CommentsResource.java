package gamepub.api;

import gamepub.beans.SessionBean;
import gamepub.db.entity.Comment;
import gamepub.db.entity.User;
import gamepub.db.service.CommentService;
import gamepub.db.service.NewsService;
import gamepub.db.service.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Date;

/**
 * Created by roman on 23.02.16.
 */
@Path("/comments")
@Stateless
public class CommentsResource {
    @EJB
    NewsService newsService;
    @EJB
    CommentService commentService;
    @EJB
    UserService userService;
    @EJB
    AuthResource authResource;

    @POST
    @Path("add")
    @Consumes("application/x-www-form-urlencoded")
    public String add(MultivaluedMap<String, String> form){
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)){
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
                case OK:{
                    Comment comment = new Comment();
                    comment.setDate(new Date());
                    comment.setUser(userService.getUserByApiToken(token));
                    comment.setText(form.getFirst("text"));
                    comment.setNews(newsService.getNewsById(Integer.valueOf(form.getFirst("newsId"))));
                    commentService.create(comment);
                    return "{" +
                            " \"STATUS\": \"OK\"," +
                            "\"message\": \"SUCCESS\"}";
                }
                default: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"UNKNOWN ERROR\"}";
            }
        }catch (Exception e){
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }
    @POST
    @Path("delete")
    @Consumes("application/x-www-form-urlencoded")
    public String delete(MultivaluedMap<String, String> form){
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)){
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
                case OK:{
                    Integer commentId = Integer.valueOf(form.getFirst("commentId"));
                    Comment comment = commentService.getCommentById(commentId);
                    User user = userService.getUserByApiToken(token);
                    if (comment.getUser().equals(user)){
                        commentService.delete(commentId);
                        return "{" +
                                " \"STATUS\": \"OK\"," +
                                "\"message\": \"SUCCESS\"}";
                    }else return "{" +
                            " \"STATUS\": \"ERROR\"," +
                            "\"message\": \"UNKNOWN ERROR\"}";
                }
                default: return "{" +
                        " \"STATUS\": \"ERROR\"," +
                        "\"message\": \"UNKNOWN ERROR\"}";
            }
        }catch (Exception e){
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }
}
