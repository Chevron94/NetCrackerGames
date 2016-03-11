/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;

import gamepub.db.entity.City;
import gamepub.db.entity.User;
import gamepub.db.entity.UserRole;
import gamepub.db.service.CityService;
import gamepub.db.service.UserRoleService;
import gamepub.db.service.UserService;
import gamepub.encode.shaCode;
import gamepub.encode.passwordGenerator;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Ivan
 */
@ManagedBean
@SessionScoped
public class GoogleAuthorizationBean implements Serializable {

    @EJB
    private UserService userService;
    @EJB
    private UserRoleService userRoleService;
    @EJB
    private CityService cityService;

    protected String clientId;
    protected String clientSecret;
    protected String userUrl;
    protected String authCode;
    protected String redirectUri;
    protected String userInfoUrl;
    protected boolean isError;
    public String googleInfo;
    public String login;
    boolean logged = false;

    public boolean getLogged() {
        return logged;
    }

    public void doRegistration() throws IOException, ParseException, ServletException, NoSuchAlgorithmException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        User user = createUser();
        if (user != null) {

            googleInfo = user.getSteamInfo();

            User userInBase = null;
            try {
                userInBase = userService.getUserBySteamInfo(user.getSteamInfo());
            } catch (Exception e) {

            }
            if (userInBase != null) {
                userInBase.setAvatarUrl(user.getAvatarUrl());
                userService.update(userInBase);
            } else {
                userService.create(user);
            }
        } else {
            context.redirect("http://localhost:8080/gamepub/registr.xhtml");
        }
    }

    public void doLogin() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = SessionBean.getSession();
        User user = userService.getUserBySteamInfo(googleInfo);
        session.setAttribute("userid", user.getId());
        session.setAttribute("username", user.getLogin());
        context.redirect("http://localhost:8080/gamepub/");
        logged = true;
    }

    public String getJsonValue(String json, String parameter) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        Object obj = jsonObject.get(parameter);
        if (obj == null) {
            return null;
        }
        String result = obj.toString();
        return result;
    }

    public String getJsonValue(JSONObject jsonObject, String parameter) throws ParseException {
        Object obj = jsonObject.get(parameter);
        if (obj == null) {
            return null;
        }
        String json = obj.toString();
        return json;
    }

    private static final Iterable<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private GoogleAuthorizationCodeFlow flow;

    public GoogleAuthorizationBean() {
        clientId = "270725871227-jpi8j7oo4qj85b3ngd6fat60n800md8u.apps.googleusercontent.com";
        clientSecret = "tuwSM6l3VKpT7cpxnNEoBr70";
        redirectUri = "http://localhost:8080/gamepub/google.xhtml";
        userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";
        isError = false;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @PostConstruct
    public void buildUserUrl() {
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientId,
                clientSecret, SCOPE).build();
        final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        userUrl = url.setRedirectUri(redirectUri).build();
    }

    public String fetchPersonalInfo() throws IOException {

        if (authCode != null) {
            final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(redirectUri).execute();
            final Credential credential = flow.createAndStoreCredential(response, null);
            final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
            // Make an authenticated request
            final GenericUrl url = new GenericUrl(userInfoUrl);
            final HttpRequest request = requestFactory.buildGetRequest(url);
            request.getHeaders().setContentType("application/json");
            final String jsonIdentity = request.execute().parseAsString();
            return jsonIdentity;
        }
        return "";
    }

    public User createUser() throws IOException, ParseException, NoSuchAlgorithmException {
        String startJson = fetchPersonalInfo();
        if (!startJson.equals("")) {
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(startJson);
            String id = getJsonValue(json, "id");
            String nickname = "Google" + id;
            String name = getJsonValue(json, "name");
            String photo = getJsonValue(json, "picture");
            String email = getJsonValue(json, "email");

            Integer idLoggedUser = null;
            try {
                idLoggedUser = SessionBean.getUserId();
            } catch (Exception e) {

            }
            User user;
            if (idLoggedUser != null) {
                user = userService.getUserById(idLoggedUser);
                user.setSteamInfo(id);
                userService.update(user);
            } else {

                user = new User();
                UserRole ur = userRoleService.getUserRoleById(1);
                City city = cityService.getCityById(1);

                user.setAvatarUrl(photo);
                String password= passwordGenerator.generatePassword(10);
                user.setPassword(shaCode.code(shaCode.code(name) + password));
                user.setEmail(email);
                user.setActive(true);
                user.setBanned(false);
                user.setSteamInfo(id);
                user.setLogin(nickname);
                user.setCity(city);
                user.setUserRole(ur);
            }
            return user;
        }
        return null;
    }
}
