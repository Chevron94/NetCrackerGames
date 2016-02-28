package gamepub.api;

import gamepub.db.entity.User;
import gamepub.db.service.UserService;
import gamepub.encode.shaCode;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by roman on 28.02.16.
 */


@Path("/login")
@Stateless
public class AuthResource {

    public enum AUTH {OK, WRONG_TOKEN, BANNED, ALL_REQUESTS_ARE_USED, TOKEN_EXPIRED}

    @EJB
    UserService userService;

    @Path("/")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String authorization(MultivaluedMap<String, String> form){
        String login = form.getFirst("login");
        String password = form.getFirst("password");
        try {
            String hashPass = shaCode.code(shaCode.code(login) + password);
            password = hashPass;
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {

        }
        User user = userService.getUserByLoginAndPassword(login,password);
        if(user!= null && !user.getBanned())
        {
            String token = UUID.randomUUID().toString();
            user.setApiToken(token);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 1);
            user.setExpireDate(calendar.getTime());
            userService.update(user);
            return token;
        }return "Incorrect login, or password, or you was banned";
    }

    public AUTH checkToken(String token){
        User user = userService.getUserByApiToken(token);
        if(user == null){
            return AUTH.WRONG_TOKEN;
        }else{
            user.setUsedRequest(user.getUsedRequest()-1);
        }
        if(user.getBanned()){
            return AUTH.BANNED;
        }
        if (user.getUsedRequest() == 0){
            return AUTH.ALL_REQUESTS_ARE_USED;
        }
        if (user.getExpireDate().before(new Date())){
            return AUTH.TOKEN_EXPIRED;
        }
        return AUTH.OK;
    }

    @Path("token")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String updateToken(MultivaluedMap<String, String> form){
        String token = form.getFirst("token");
        User user = userService.getUserByApiToken(token);
        if (user != null && !user.getBanned()) {
            String newToken = UUID.randomUUID().toString();
            user.setApiToken(newToken);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 1);
            user.setExpireDate(calendar.getTime());
            userService.update(user);
            return token;
        }return "Incorrect token or you was banned";
    }
}
