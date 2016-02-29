/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api;

import gamepub.db.entity.PrivateMessage;
import gamepub.db.entity.User;
import gamepub.db.service.PrivateMessageService;
import gamepub.db.service.UserService;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Ivan
 */
@Stateless
@Path("/message")
public class MessageResource {

    @EJB
    UserService userService;
    @EJB
    PrivateMessageService privateMessageService;

    AuthResource authResource = new AuthResource();

    @GET
    @Path("/sendedMessages")
    @Produces("application/json")
    public List<PrivateMessage> getSendedMessages(@QueryParam("token") String token) {
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth) {
            case OK: {
                User user= userService.getUserByApiToken(token);
                return privateMessageService.getSendedPrivateMessagesBySenderId(user.getId());
            }
            default: {
                return null;
            }
        }
    }
    
    @GET
    @Path("/receivedMessages")
    @Produces("application/json")
    public List<PrivateMessage> getReceivedMessages(@QueryParam("token") String token) {
        AuthResource.AUTH auth = authResource.checkToken(token);
        switch (auth) {
            case OK: {
                User user= userService.getUserByApiToken(token);
                return privateMessageService.getReceivedPrivateMessagesByReceiverId(user.getId());
            }
            default: {
                return null;
            }
        }
    }

    @POST
    @Path("/send")
    @Consumes("application/x-www-form-urlencoded")
    public String sendMessage(MultivaluedMap<String, String> form) {
        try {
            String token = form.getFirst("token");
            switch (authResource.checkToken(token)) {
                case ALL_REQUESTS_ARE_USED:
                    return "ALL REQUESTS FOR TODAY ARE USED";
                case WRONG_TOKEN:
                    return "WRONG TOKEN";
                case BANNED:
                    return "YOU WAS BANNED";
                case TOKEN_EXPIRED:
                    return "TOKEN EXPIRED";
                case OK: {
                    PrivateMessage privateMessage = new PrivateMessage();
                    privateMessage.setSender(userService.getUserByApiToken(token));
                    privateMessage.setReceiver(userService.getUserByLogin(form.getFirst("receiverLogin")));
                    privateMessage.setDate(new Date());
                    privateMessage.setText(form.getFirst("message"));
                    privateMessageService.create(privateMessage);
                    return "ok";
                }
                default:
                    return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }
}
