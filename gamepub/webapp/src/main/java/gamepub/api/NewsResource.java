package gamepub.api;

import gamepub.db.entity.Comment;
import gamepub.db.entity.News;
import gamepub.db.service.CommentService;
import gamepub.db.service.GameService;
import gamepub.db.service.NewsService;
import gamepub.db.service.UserService;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    @Path("all")
    @Produces("application/json")
    public List<News> getAll(){
        return newsService.findAll();
    }

    @GET
    @Path("custom")
    @Produces("application/json")
    public List<News> getCustom(@DefaultValue("") @QueryParam("name") String name,
                                @DefaultValue("") @QueryParam("game") String game,
                                @DefaultValue("") @QueryParam("date") String date){
        try {
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
                return newsService.getNewsByCustomParams(params, true, 0, 0);
        }catch (Exception e){
            return null;
        }
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public News getOne(@PathParam("id") Integer id){
        return newsService.find(id);
    }

    @GET
    @Path("{id}/comments")
    @Produces("application/json")
    public List<Comment> getComments(@PathParam("id") Integer id){
        return commentService.getCommentsByNewsId(id);
    }

}
