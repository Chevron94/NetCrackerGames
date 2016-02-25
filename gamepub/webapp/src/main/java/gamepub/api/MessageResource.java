/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.api;

import gamepub.beans.SessionBean;
import gamepub.db.entity.PrivateMessage;
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

    @GET
    @Path("/{userId}/allMessage")
    @Produces("application/json")
    public List<PrivateMessage> receiveMessage(@PathParam("userId") Integer userId) {
        if (userId == SessionBean.getUserId()) {
            return privateMessageService.getSendedPrivateMessagesBySenderId(userId);
        } else {
            return null;
        }
    }

    @POST
    @Path("/add")
    public String sendMessage(MultivaluedMap<String, String> form) {
        try {
            if (userService.getUserByLogin(form.getFirst("senderLogin")).getId() == SessionBean.getUserId()) {
                PrivateMessage privateMessage = new PrivateMessage();
                privateMessage.setSender(userService.getUserByLogin(form.getFirst("senderLogin")));
                privateMessage.setReceiver(userService.getUserByLogin(form.getFirst("receiverLogin")));
                privateMessage.setDate(new Date());
                privateMessage.setText(form.getFirst("message"));
                privateMessageService.create(privateMessage);
                return "ok";
            } else {
                return "no rights to send message";
            }
        } catch (Exception e) {
            return "error";
        }
    }
}
