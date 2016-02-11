package gamepub.beans;

import gamepub.db.entity.PrivateMessage;
import gamepub.db.entity.User;
import gamepub.db.service.PrivateMessageService;
import gamepub.db.service.UserService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Created by Анатолий on 11.02.2016.
 */
@ManagedBean
@SessionScoped
public class OneMessageBean {


    String userLogin;
    int receiverId;
    String message;
    @EJB
    UserService userService;
    @EJB
    PrivateMessageService privateMessageService;

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
    public String getMyMessage() {
        return message;
    }

    public void setMyMessage(String message) {
        this.message = message;
    }

    public String getUserLogin(){
        return userLogin;
    }

    public void setUserLogin(String userLogin){
        this.userLogin  = userLogin;
    }

    public User getReceiver() {
        if (receiverId != 0)
            return userService.getUserById(receiverId);
        return null;
    }

    public boolean getReceiverIsNull(){
        return receiverId == 0;
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


    public String sendMessage() {
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(userService.getUserById(SessionBean.getUserId()));
        privateMessage.setReceiver(userService.getUserById(receiverId));
        privateMessage.setDate(new Date());
        privateMessage.setText(message);
        privateMessageService.create(privateMessage);
        receiverId = 0;
        message = "";
        userLogin = "";
        return "allMessages";
    }

}
