package gamepub.api.Interceptors;

import gamepub.api.annotations.Secure;
import gamepub.db.entity.User;
import gamepub.db.service.UserService;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by roman on 12.03.16.
 */
@Provider
@ServerInterceptor
@Stateless
public class ValidationInterceptor implements PreProcessInterceptor, AcceptedByMethod {
    @EJB
    UserService userService;

    public boolean accept(Class declaring, Method method) {
        if (method.getAnnotation(Secure.class)!=null)
            return true;
        return false;
    }

    public ServerResponse preProcess(HttpRequest request, ResourceMethodInvoker method) throws Failure, WebApplicationException {
        ServerResponse serverResponse = null;
        try {
            String token = request.getHttpHeaders().getHeaderString("token");
            if (token!=null){
                Context context = new InitialContext();
                userService = (UserService)context.lookup("java:app/gamepub/UserService");
                User user = userService.getUserByApiToken(token);
                if (user!=null){
                    if (user.getExpireDate().after(new Date())){
                        if (!user.getBanned()){
                            if(user.getUsedRequest()>0){
                                if(!user.getGold()){
                                    user.setUsedRequest(user.getUsedRequest()-1);
                                    userService.update(user);
                                }
                            }else{
                                serverResponse = new ServerResponse(
                                        "Too many requests",
                                        403, new Headers<Object>());
                            }
                        }else{
                            serverResponse = new ServerResponse(
                                    "Banned user",
                                    403, new Headers<Object>());
                        }
                    }else{
                        serverResponse = new ServerResponse(
                                "Token expired",
                                403, new Headers<Object>());
                    }
                }else{
                    serverResponse = new ServerResponse(
                            "Incorrect token",
                            403, new Headers<Object>());
                }
            }else{
                serverResponse = new ServerResponse(
                        "No token",
                        403, new Headers<Object>());
            }
        }catch (Exception e){
            serverResponse = new ServerResponse(
                    "ERROR",
                    500, new Headers<Object>());

        }
        return serverResponse;
    }
}
