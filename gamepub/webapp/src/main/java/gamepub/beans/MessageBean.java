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

    String userLogin;

    @EJB
    PrivateMessageService privateMessageService;
    @EJB
    UserService userService;

    int receiverId;
    String message;
    User receiver;
    public String getUserLogin(){
        return userLogin;
    }

    public void setUserLogin(String userLogin){
        this.userLogin  = userLogin;
    }

    public int getReceiverId(){
        return receiverId;
    }

    public void setReceiverId(int receiverId){
        this.receiverId = receiverId;
        receiver = userService.getUserById(receiverId);
    }

    public void lifeSearch() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if(userLogin != null){
            facesContext.getExternalContext().getSessionMap().put("userMes", userLogin);
        }
    }
    public List<User> getUsers() {
        List<HashMap.Entry<String, Object>> parametersList = new ArrayList<HashMap.Entry<String, Object>>();
        Map.Entry<String, Object> param;
        if (userLogin != null && userLogin.length()>0) {
            param = new HashMap.SimpleEntry<String, Object>("login", userLogin);
            parametersList.add(param);
            return userService.getUsersByCustomParams(parametersList);
        }
        return null;
    }

    public void findUser(User user){
        userLogin = user.getLogin();
        receiverId = user.getId();
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
        userLogin="";
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
        receiverId = 0;

    }
}
