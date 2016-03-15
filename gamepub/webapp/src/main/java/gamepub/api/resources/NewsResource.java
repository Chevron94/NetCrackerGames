package gamepub.api.resources;

import gamepub.api.Interceptors.ValidationInterceptor;
import gamepub.api.annotations.Secure;
import gamepub.db.entity.Comment;
import gamepub.db.entity.News;
import gamepub.db.service.CommentService;
import gamepub.db.service.GameService;
import gamepub.db.service.NewsService;
import gamepub.db.service.UserService;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import java.util.*;

/**
 * Created by roman on 23.02.16.
 */
@Path("/news")
@Stateless
public class NewsResource {
    @EJB
    NewsService newsService;
    @EJB
    CommentService commentService;
    @EJB
    UserService userService;
    @EJB
    GameService gameService;

    @GET
    @Path("/")
    @Produces("application/json")
    @Secure
    public List<News> getCustom(@DefaultValue("") @QueryParam("name") String name,
                                @DefaultValue("") @QueryParam("game") String game,
                                @DefaultValue("") @QueryParam("date") String date,
                                @DefaultValue("0") @QueryParam("start") String startStr,
                                @HeaderParam("token") String token){
        try {
            Integer start = Integer.valueOf(startStr);
            List<Map.Entry<String, Object>> params = new ArrayList<Map.Entry<String, Object>>();
            if (name != null && !name.trim().equals("")) {
                params.add(new HashMap.SimpleEntry<String, Object>("name", name));
            }
            if (date != null && !date.trim().equals("")){
                params.add(new HashMap.SimpleEntry<String, Object>("date",new Date(date)));
            }
            if (game != null && !game.trim().equals("")){
                params.add(new HashMap.SimpleEntry<String, Object>("game",gameService.getGameById(Integer.valueOf(game))));
            }
            return newsService.getNewsByCustomParams(params, false, start, 100);
        }catch (Exception e){
            return null;
        }
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    @Secure
    public News getOne(@PathParam("id") Integer id, @HeaderParam("token") String token){
        return newsService.find(id);
    }

    @GET
    @Path("{id}/comments")
    @Produces("application/json")
    @Secure
    public List<Comment> getComments(@PathParam("id") Integer id, @HeaderParam("token") String token){
        return commentService.getCommentsByNewsId(id);
    }

}
