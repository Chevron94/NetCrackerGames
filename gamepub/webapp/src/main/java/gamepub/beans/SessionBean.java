/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * @author fitok
 */
@ManagedBean
public class SessionBean {

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }

    public static String getUserName() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        return session.getAttribute("username").toString();
    }

    public static Integer getUserId() {
        HttpSession session = getSession();
        if (session != null)
            return (Integer) session.getAttribute("userid");
        else
            return null;
    }

    public static Integer getUserForTradeId() {
        HttpSession session = getSession();
        if (session != null)
            return (Integer) session.getAttribute("tradeuser");
        else
            return null;
    }
       public static Integer getGameId() {

        HttpSession session = getSession();
        if (session != null)
            return (Integer) session.getAttribute("gameid");
        else
            return null;
    }

    public boolean getIsLogged() {
        return getUserId() != null;
    }

    public int getMainWidth() {
        if (getIsLogged())
            return 5;
        else
            return 6;
    }
    public int getMainOffset() {
        if (getIsLogged())
            return 1;
        else
            return 0;
    }
}
