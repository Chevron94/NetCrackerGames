package gamepub.api;

import gamepub.beans.SessionBean;
import gamepub.db.entity.Comment;
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



    @POST
    @Path("add")
    @Consumes("application/x-www-form-urlencoded")
    public String add(MultivaluedMap<String, String> form){
        try {
            if (SessionBean.getUserId() == Integer.valueOf(form.getFirst("userId"))) {
                Comment comment = new Comment();
                comment.setDate(new Date());
                comment.setUser(userService.getUserById(Integer.valueOf(form.getFirst("userId"))));
                comment.setText(form.getFirst("text"));
                comment.setNews(newsService.getNewsById(Integer.valueOf(form.getFirst("newsId"))));
                commentService.create(comment);
                return "OK";
            }return "FAIL";
        }catch (Exception e){
            return "FAIL";
        }
    }
    @POST
    @Path("delete")
    @Consumes("application/x-www-form-urlencoded")
    public String delete(MultivaluedMap<String, String> form){
        try {

            if (SessionBean.getUserId() == commentService.getCommentById(Integer.valueOf(form.getFirst("commentId"))).getUser().getId()) {
                commentService.delete(Integer.valueOf(form.getFirst("commentId")));
                return "OK";
            }return "FAIL";
        }catch (Exception e){
            return "FAIL";
        }
    }
}
