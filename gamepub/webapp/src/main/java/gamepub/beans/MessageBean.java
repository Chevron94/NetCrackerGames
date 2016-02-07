package gamepub.beans;

import gamepub.db.entity.PrivateMessage;
import gamepub.db.entity.User;
import gamepub.db.service.PrivateMessageService;
import gamepub.db.service.UserService;

import javax.ejb.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by Анатолий on 03.02.2016.
 */
@ManagedBean
@SessionScoped
public class MessageBean {

    @EJB
    PrivateMessageService privateMessageService;
    @EJB
    UserService userService;

    int receiverId;
    String message;
    User receiver;

    public int getMyReceiver() {
        return receiverId;
    }

    public void setMyReceiver(int id) {
        receiverId = id;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMyMessage() {
        return message;
    }

    public void setMyMessage(String message) {
        this.message = message;
    }

    public List<PrivateMessage> getReceivedMessages() {
        return privateMessageService.getReceivedPrivateMessagesByReceiverId(SessionBean.getUserId());
    }

    public List<PrivateMessage> getSendedMessages() {
        return privateMessageService.getSendedPrivateMessagesBySenderId(SessionBean.getUserId());
    }

    public String sendMessage() {
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(userService.getUserById(SessionBean.getUserId()));
        privateMessage.setReceiver(userService.getUserById(receiverId));
        privateMessage.setDate(new Date());
        privateMessage.setText(message);
        privateMessageService.create(privateMessage);
        receiverId = 0;
        message = "";
        return "allMessages";
    }

    public boolean getReceiverIsNull() {
        HttpSession session = SessionBean.getSession();
        try {
            receiverId = (Integer) session.getAttribute("receiverId");
            receiver = userService.getUserById(receiverId);
            session.removeAttribute("receiverId");
        } catch (Exception e) {

        }
        return receiverId == 0;
    }
    public String goToMessage(){
        return "message";
    }
}
