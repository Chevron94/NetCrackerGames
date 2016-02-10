/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;
import com.sun.faces.util.Util;
import gamepub.db.entity.User;
import gamepub.db.service.UserService;
import gamepub.encode.shaCode;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author fitok
 */
@ManagedBean
@SessionScoped
public class LoginBean{    
    @EJB
    UserService userService; 
private User user;    
private String name;
private String password;
private boolean logged;
    public LoginBean() {
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public User getUser(){return user;}

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void check() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
    String hashPass = shaCode.code(shaCode.code(name)+password);
        HttpSession ses = SessionBean.getSession();
       
        if(userService.getUserByLoginAndPassword(name,hashPass)!=null){
        user = userService.getUserByLoginAndPassword(name, hashPass); 
            setLogged(true);
            ses.setAttribute("userid", user.getId());
            ses.setAttribute("username", getName());                  
         }        
       
        else{
            
        FacesMessage failMes= new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Error",
                    "Failed to login!");
               RequestContext.getCurrentInstance().showMessageInDialog(failMes);
        }
    }

    
    public void logout(){
        try {
            setLogged(false);
            SessionBean.getSession().invalidate();
        }
        catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
    /**
     * @return the logged
     */
    public boolean isLogged() {   
        return logged;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}

