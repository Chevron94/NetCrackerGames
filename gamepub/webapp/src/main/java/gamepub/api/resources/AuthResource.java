package gamepub.api.resources;

import gamepub.db.entity.User;
import gamepub.db.service.UserService;
import gamepub.encode.shaCode;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
    @EJB
    UserService userService;

    @Context
    org.jboss.resteasy.spi.HttpResponse response;

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
            response.getOutputHeaders().putSingle("token",token);
            return "{" +
                    " \"status\": \"OK\"}";
        }return "{" +
                " \"status\": \"ERROR\"," +
                "\"message\": \"Incorrect login, or password, or you was banned\"}";
    }

}
