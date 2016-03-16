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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import sun.security.rsa.RSASignature;

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
    
    public void check() throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException{
        
        String hashPass = shaCode.code(shaCode.code(name)+password);
        HttpSession ses = SessionBean.getSession();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
       
        if(userService.getUserByLoginAndPassword(name,hashPass) != null){
            user = userService.getUserByLoginAndPassword(name, hashPass);
            if(user.getBanned() != true)
            {
                 
                setLogged(true);
                ses.setAttribute("userid", user.getId());
                ses.setAttribute("username", getName());  
            }
            else
            {
                ses.setAttribute("userid", user.getId());
                context.redirect("/gamepub/banned.xhtml");
            }
         }        
       
        else{
            
        FacesMessage failMes= new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Error",
                    "Failed to login!");
               RequestContext.getCurrentInstance().showMessageInDialog(failMes);
        }
    }

    
    public String logout(){
        try {
            setLogged(false);
            SessionBean.getSession().invalidate();
            return "main";
        }
        catch (Exception e){
            System.out.print(e.getMessage());
            return "main";
        }
    }

    public String goToMain(){
        return "main";
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

    public String registrLogin(){
        HttpSession ses = SessionBean.getSession();
        if(ses.getAttribute("userid")!=null)
        {
            logged = true;
            return "profile.xhtml?userId=my";
        }
        return "registr";
    }
}

