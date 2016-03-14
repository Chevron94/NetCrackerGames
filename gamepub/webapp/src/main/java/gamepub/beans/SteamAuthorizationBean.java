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

import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ivan
 */
@ManagedBean
@SessionScoped
public class SteamAuthorizationBean implements Serializable {

    /*
    @EJB
    private UserService userService;
    @EJB
    private UserRoleService userRoleService;
    @EJB
    private CityService cityService;

    protected String clientId;
    protected String clientKey;
    protected String userUrl;
    protected String authCode;
    protected String redirectUri;
    protected String userInfoUrl;
    protected boolean isError;
    public String steamInfo;
    boolean logged = false;

    public boolean getLogged() {
        return logged;
    }

    public void doRegistration() throws IOException, ParseException, ServletException, NoSuchAlgorithmException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        User user = createUser();
        if (user != null) {

            steamInfo = user.getSteamInfo();

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
            context.redirect("http://193.124.180.79:8080/gamepub/registr.xhtml");
        }
    }

    public void doLogin() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = SessionBean.getSession();
        User user = userService.getUserBySteamInfo(steamInfo);
        if(user.getBanned() != true)
        {
            session.setAttribute("userid", user.getId());
            session.setAttribute("username", user.getLogin());
            context.redirect("http://193.124.180.79:8080/gamepub/");
            logged = true;
        }
        else
        {
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

    private static final String STEAM_URL = "http://steamcommunity.com/openid";
    private static final String API_URL = "https://oauth.vk.com/access_token";

    public SteamAuthorizationBean() {        
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        try {
            manager = new ConsumerManager();
        manager.setMaxAssocAttempts(0);
        
            discovered = manager.associate(manager.discover(STEAM_OPENID));
        } catch (Exception e) {
            e.printStackTrace();
            discovered = null;
        }
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
        String url = STEAM_URL + "?key=" + clientKey
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
        userUrl = url;
    }

    public String fetchPersonalInfo() throws IOException, ParseException {
        String urlAccessToken = API_URL
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
                City city = cityService.getCityById(1);

                user.setAvatarUrl(photo);
                user.setPassword(shaCode.code(shaCode.code(name) + id));
                user.setEmail("default email");
                user.setActive(true);
                user.setBanned(false);
                user.setVkInfo(id);
                user.setLogin(nickname);
                user.setCity(city);
                user.setUserRole(ur);
            }
            return user;
        }
        return null;
    }
    //////
        private static final String STEAM_OPENID = "http://steamcommunity.com/openid";
    private final ConsumerManager manager;
    private final Pattern STEAM_REGEX = Pattern.compile("(\\d+)");
    private DiscoveryInformation discovered;

    /**
     * Perform a login then redirect to the callback url. When the
     * callback url is opened, you are responsible for
     * verifying the OpenID login.
     *
     * @param callbackUrl A String of a url that this login page should
     *                    take you to. This should be an absolute URL.
     * @return Returns the URL of the OpenID login page. You should
     * redirect your user to this.
     */
    /*
    public String login(String callbackUrl) {
        if (this.discovered == null) {
            return null;
        }
        try {
            AuthRequest authReq = manager.authenticate(this.discovered, callbackUrl);
            return authReq.getDestinationUrl(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verify the Steam OpenID Login
     *
     * @param receivingUrl The url that received the Login (this should be the
     *                     same as the callbackUrl that you used in
     *                     the {@link #login(String)} method.
     * @param responseMap  A {@link Map} that contains the response values from the login.
     * @return Returns the Steam Community ID as a string.
     */
    /*
    public String verify(String receivingUrl, Map responseMap) {
        if (this.discovered == null) {
            return null;
        }
        ParameterList responseList = new ParameterList(responseMap);
        try {
            VerificationResult verification = manager.verify(receivingUrl, responseList, this.discovered);
            Identifier verifiedId = verification.getVerifiedId();
            if (verifiedId != null) {
                String id = verifiedId.getIdentifier();
                Matcher matcher = STEAM_REGEX.matcher(id);
                if (matcher.find()) {
                    System.out.println();
                    return matcher.group(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    */
}
