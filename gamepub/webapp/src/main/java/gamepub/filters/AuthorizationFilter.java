package gamepub.filters;

import gamepub.beans.LoginBean;
import gamepub.beans.SessionBean;
import gamepub.beans.VKAuthorizationBean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Анатолий on 04.02.2016.
 */

public class AuthorizationFilter implements Filter {


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        try {
            HttpSession session = request.getSession();
            if(session.getAttribute("userid") != null)
                filterChain.doFilter(request, response);
            else
                ((HttpServletResponse)response).sendRedirect("/gamepub/registr.xhtml");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            ((HttpServletResponse)response).sendRedirect("/gamepub/registr.xhtml");
        }




    }


    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}