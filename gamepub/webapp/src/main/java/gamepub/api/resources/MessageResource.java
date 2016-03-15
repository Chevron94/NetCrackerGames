/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api.resources;

import gamepub.api.Interceptors.ValidationInterceptor;
import gamepub.api.annotations.Secure;
import gamepub.db.entity.PrivateMessage;
import gamepub.db.entity.User;
import gamepub.db.service.PrivateMessageService;
import gamepub.db.service.UserService;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
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

    @GET
    @Path("/sentMessages")
    @Produces("application/json")
    @Secure
    public List<PrivateMessage> getSentMessages(@HeaderParam("token") String token) {
        User user= userService.getUserByApiToken(token);
        return privateMessageService.getSendedPrivateMessagesBySenderId(user.getId());
    }
    
    @GET
    @Path("/receivedMessages")
    @Produces("application/json")
    @Secure
    public List<PrivateMessage> getReceivedMessages(@HeaderParam("token") String token) {
        User user= userService.getUserByApiToken(token);
        return privateMessageService.getReceivedPrivateMessagesByReceiverId(user.getId());
    }

    @POST
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Secure
    public String sendMessage(MultivaluedMap<String, String> form,@HeaderParam("token") String token) {
        try {
            PrivateMessage privateMessage = new PrivateMessage();
            privateMessage.setSender(userService.getUserByApiToken(token));
            privateMessage.setReceiver(userService.getUserByLogin(form.getFirst("receiverLogin")));
            privateMessage.setDate(new Date());
            privateMessage.setText(form.getFirst("message"));
            privateMessageService.create(privateMessage);
            return "{" +
                    " \"STATUS\": \"OK\"," +
                    "\"message\": \"SUCCESS\"}";
        } catch (Exception e) {
            return "{" +
                    " \"STATUS\": \"ERROR\"," +
                    "\"message\": \"UNKNOWN ERROR\"}";
        }
    }
}
