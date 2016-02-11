package gamepub.beans;

import gamepub.db.entity.PrivateMessage;
import gamepub.db.entity.User;
import gamepub.db.service.PrivateMessageService;
import gamepub.db.service.UserService;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.rating.Rating;

import javax.ejb.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.*;

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


    public int getReceiverId(){
        return receiverId;
    }

    public void setReceiverId(int receiverId){
        this.receiverId = receiverId;
        receiver = userService.getUserById(receiverId);
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

    public boolean getsReceiverIsNull(){
        receiverId=0;
        return true;
    }

    //dialog
    public List<PrivateMessage> getDialog(){
        return privateMessageService.getAllPrivateMessagesBySenderIdAndReceiverId(SessionBean.getUserId(),receiverId);
    }

    public void sendDialog() {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = context.getViewRoot();
        InputTextarea inputText = (InputTextarea) uiViewRoot.findComponent("messageForm:inputMes");
        String message = (String) inputText.getValue();
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(userService.getUserById(SessionBean.getUserId()));
        privateMessage.setReceiver(userService.getUserById(receiverId));
        privateMessage.setDate(new Date());
        privateMessage.setText(message);
        privateMessageService.create(privateMessage);
        inputText.resetValue();


    }

    public User getUser(){
        return userService.getUserById( SessionBean.getUserId());
    }
}
