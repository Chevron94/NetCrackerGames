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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.ClientParamsStack;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ivan
 */
@ManagedBean
@SessionScoped
public class FaceBookAuthorizationBean implements Serializable {

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
    public String fBInfo;
    boolean logged = false;

    public boolean getLogged() {
        return logged;
    }

    public void doRegistration() throws IOException, ParseException, ServletException, NoSuchAlgorithmException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        User user = createUser();
        if (user != null) {

            fBInfo = user.getFbInfo();
            User userInBase = null;
            try {
                userInBase = userService.getUserByFbInfo(user.getFbInfo());
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
        User user = userService.getUserByFbInfo(fBInfo);
        if(user.getBanned() != true)
        {
            session.setAttribute("userid", user.getId());
            session.setAttribute("username", user.getLogin());
            context.redirect("http://localhost:8080/gamepub/");
            logged = true;
        }
        else
        {
            session.setAttribute("userid", user.getId());
            context.redirect("/gamepub/banned.xhtml");
        }
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

    private static final String FACEBOOK_AUTHORIZATION_URL = "https://www.facebook.com/dialog/oauth";
    private static final String ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token";

    public FaceBookAuthorizationBean() {
        clientId = "1689596507984950";
        clientSecret = "e5b0079865e552362437c361aeea01b7";
        redirectUri = "http://localhost:8080/gamepub/facebook.xhtml";
        userInfoUrl = "https://graph.facebook.com/me";
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
        String url = FACEBOOK_AUTHORIZATION_URL + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
        userUrl = url;
    }

    public String fetchPersonalInfo() throws IOException, ParseException {
        String urlAccessToken = ACCESS_TOKEN_URL
                + "?client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&code=" + authCode
                + "&redirect_uri=" + redirectUri;
        String response = getResponseString(urlAccessToken);
        try {
            if (getJsonValue(response, "error") != null) {
                isError = true;
            }
        } catch (ParseException e) {
        }
        if (!isError) {
            String accessToken = getAccessToken(response);
            //        String userId=getAccessToken();
            String urlUserInfo = userInfoUrl
                    + "?access_token=" + accessToken;
            String json = getResponseString(urlUserInfo);
            String personalFacebookId = getJsonValue(json, "id");
            String pictureUrl = "https://graph.facebook.com/" + personalFacebookId + "/picture?type=large";
            json = addPictureUrl(json, pictureUrl);
            return json;
        }
        return "";
    }

    private String getAccessToken(String response) {
        int first = response.indexOf("=") + 1;
        int last = response.indexOf("&expires");
        return response.substring(first, last);
    }

    private String getResponseString(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        ClientParamsStack httpParams = (ClientParamsStack) response.getParams();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    private String addPictureUrl(String json, String pictureUrl) {
        JSONParser jsonParser = new JSONParser();
        String result = "";
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            jsonObject.put("picture", pictureUrl);
            result = jsonObject.toJSONString();
//            String jsonComponent=array.get(index).toString();
        } catch (ParseException ex) {
        }
        return result;
    }

    public User createUser() throws IOException, ParseException, NoSuchAlgorithmException {
        String startJson = fetchPersonalInfo();
        if (!startJson.equals("")) {
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(startJson);
//        String error=getJsonValue(json, "error");
            String id = getJsonValue(json, "id");
//        return id;
            String nickname = "Facebook" + id;
            String name = getJsonValue(json, "name");
            String photo = getJsonValue(json, "picture");

            Integer idLoggedUser = null;
            try {
                idLoggedUser = SessionBean.getUserId();
            } catch (Exception e) {

            }
            User user;
            if (idLoggedUser != null) {
                user = userService.getUserById(idLoggedUser);
                user.setFbInfo(id);
                userService.update(user);
            } else {

                user = new User();
                UserRole ur = userRoleService.getUserRoleById(1);
                City city = cityService.getCityById(2235395);

                user.setAvatarUrl(photo);
                String password= passwordGenerator.generatePassword(10);
                user.setPassword(shaCode.code(shaCode.code(name) + password));
                user.setEmail("default email");
                user.setActive(true);
                user.setBanned(false);
                user.setFbInfo(id);
                user.setFine(0);
                user.setGold(false);
                user.setReputation(0);
                user.setLogin(nickname);
                user.setCity(city);
                user.setUserRole(ur);
            }
            return user;
        }
        return null;
    }
}
