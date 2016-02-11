package gamepub.filters;

import gamepub.beans.SessionBean;

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
        //HttpSession session = ;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            if(SessionBean.getSession() != null && (Integer)SessionBean.getSession().getAttribute("userid")!=0)
                filterChain.doFilter(request, response);
            else
                ((HttpServletResponse)response).sendRedirect("/gamepub/registr.xhtml");
        }
        catch (Exception e){
            ((HttpServletResponse)response).sendRedirect("/gamepub/registr.xhtml");
        }




    }


    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}