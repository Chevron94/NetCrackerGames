package gamepub.api.resources;

import gamepub.api.Interceptors.ValidationInterceptor;
import gamepub.api.annotations.Secure;
import gamepub.db.entity.Comment;
import gamepub.db.entity.User;
import gamepub.db.service.CommentService;
import gamepub.db.service.NewsService;
import gamepub.db.service.UserService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
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

    @POST
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Secure
    public String add(MultivaluedMap<String, String> form){
        try {
            String token = form.getFirst("token");
            Comment comment = new Comment();
            comment.setDate(new Date());
            comment.setUser(userService.getUserByApiToken(token));
            comment.setText(form.getFirst("text"));
            comment.setNews(newsService.getNewsById(Integer.valueOf(form.getFirst("newsId"))));
            commentService.create(comment);
            return "{" +
                    " \"STATUS\": \"OK\"," +
                    "\"message\": \"SUCCESS\"}";
        }catch (Exception e){
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }
    @DELETE
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Secure
    public String delete(MultivaluedMap<String, String> form){
        try {
            String token = form.getFirst("token");
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
                    "\"message\": \"PERMISSION DENIED\"}";
        }catch (Exception e){
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }
}
