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
import javax.ejb.EJB;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.ClientParamsStack;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
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
public class VKAuthorizationBean implements Serializable {

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
    public String vKInfo;
    boolean logged = false;

    public boolean getLogged() {
        return logged;
    }

    public void doRegistration() throws IOException, ParseException, ServletException, NoSuchAlgorithmException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        User user = createUser();
        if (user != null) {

            vKInfo = user.getVkInfo();

            User userInBase = null;
            try {
                userInBase = userService.getUserByVkInfo(user.getVkInfo());
            } catch (Exception e) {

            }
            if (userInBase != null) {
                userInBase.setAvatarUrl(user.getAvatarUrl());
                userService.update(userInBase);
            } else {
                userService.create(user);
            }
        } else {
            context.redirect("https://gamepub.tk/registr.xhtml");
        }
    }

    public void doLogin() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = SessionBean.getSession();
        User user = userService.getUserByVkInfo(vKInfo);
        if(user.getBanned() != true)
        {
            session.setAttribute("userid", user.getId());
            session.setAttribute("username", user.getLogin());
            context.redirect("https://gamepub.tk/");
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

    private static final String VK_AUTHORIZATION_URL = "https://oauth.vk.com/authorize";
    private static final String ACCESS_TOKEN_URL = "https://oauth.vk.com/access_token";

    public VKAuthorizationBean() {
        clientId = "5282358";
        clientSecret = "0zKP5VC4jAfbq7w7gYsI";
        redirectUri = "https://gamepub.tk/vk.xhtml";
        userInfoUrl = "https://api.vk.com/method/users.get";
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
        String url = VK_AUTHORIZATION_URL + "?client_id=" + clientId
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
        String json = getResponseJson(urlAccessToken);
        if (getJsonValue(json, "error") != null) {
            isError = true;
        } else {
            String accessToken = getJsonValue(json, "access_token");
            String userId = getJsonValue(json, "user_id");
            String urlUserInfo = userInfoUrl
                    + "?uids=" + userId
                    + "&fields=uid,first_name,last_name,nickname,screen_name,sex,bdate,city,country,timezone,photo_max"
                    + "&access_token=" + accessToken;
            json = getResponseJson(urlUserInfo);
            return json;
        }
        return "";
    }

    private String getResponseJson(String url) throws IOException {
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

    public User createUser() throws IOException, ParseException, NoSuchAlgorithmException {
        String startJson = fetchPersonalInfo();
        if (!startJson.equals("")) {

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(startJson);
            JSONArray jsonArray = (JSONArray) jsonObject.get("response");
            JSONObject json = (JSONObject) jsonArray.get(0);
            String id = getJsonValue(json, "uid");
            //        return id;
            String nickname = "VK" + id;
            String firstName = getJsonValue(json, "first_name");
            String lastName = getJsonValue(json, "last_name");
            String name = firstName + " " + lastName;
            String photo = getJsonValue(json, "photo_max");

            Integer idLoggedUser = null;
            try {
                idLoggedUser = SessionBean.getUserId();
            } catch (Exception e) {

            }
            User user;
            if (idLoggedUser != null) {
                user = userService.getUserById(idLoggedUser);
                user.setVkInfo(id);
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
                user.setVkInfo(id);
                user.setLogin(nickname);
                user.setTradesLeft(3);
                user.setCity(city);
                user.setFine(0);
                user.setGold(false);
                user.setReputation(0);
                user.setUserRole(ur);
            }
            return user;
        }
        return null;
    }
}
