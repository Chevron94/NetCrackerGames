package gamepub.beans;

import gamepub.cloud.cloudUpload;
import gamepub.db.entity.*;
import gamepub.db.service.*;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.jpa.criteria.expression.function.AggregationFunction;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 * Created by �������� on 06.01.2016.
 */
@ManagedBean
@SessionScoped
public class ProfileBean {

    private Integer id;
    List<Game> myGames;
    List<UserGame> userGame;
    private boolean isEdit;
    private boolean isMy;
    private int cityId;
    private String email, fbInfo, login, password;

    @EJB
    UserService userService;
    @EJB
    GameService gameService;
    @EJB
    CityService cityService;
    @EJB
    UserGameService userGameService;
    @EJB
    CountryService countryService;
    @EJB
    FriendService friendService;

    private String userId;

    @PostConstruct
    public void init() {

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        isEdit = false;
        try {
            id = SessionBean.getUserId();
        } catch (NullPointerException e) {

        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (!context.getExternalContext().getSessionMap().containsKey("edit")) //context.getExternalContext().getSessionMap().remove("edit");
        {
            context.getExternalContext().getSessionMap().put("edit", false);
        }
        User user;
        if (!userId.equals("my")) {
            user = userService.getUserByUid(userId);
            id = user.getId();
        } else {
            user = userService.getUserById(id);
        }

        return user.getLogin();
    }

    public String getImage() {
        User user = userService.getUserById(id);
        return user.getAvatarUrl();
    }

    public String getCountry() {
        User user = userService.getUserById(id);
        return user.getCity().getCountry().getName();
    }

    public String getEmail() {
        User user = userService.getUserById(id);
        return user.getEmail();
    }

    public void setEmail(String uemail) {
        email = uemail;
    }

    public String getLogin() {
        return userService.getUserById(id).getLogin();
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFbInfo() {
        User user = userService.getUserById(id);
        if (user.getFbInfo() == null) {
            return " ";
        }
        return user.getFbInfo();
    }

    public void setFbInfo(String ufbInfo) {
        fbInfo = ufbInfo;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int ucityId) {
        cityId = ucityId;
    }

    public String getCity() {
        User user = userService.getUserById(id);
        return user.getCity().getName();
    }

    public List<City> getCities() {
        return cityService.findAll();
    }

    public boolean getIsEdit() {
        FacesContext context = FacesContext.getCurrentInstance();

        return (Boolean) context.getExternalContext().getSessionMap().get("edit");
    }

    public boolean getIsMy() {
        return userId.equals("my");
    }

    public List<Game> getRecomendGames() {
        return gameService.getGamesOrderByMarks(4);
    }

    public void edit() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("edit");
        context.getExternalContext().getSessionMap().put("edit", true);
    }

    public void confirmEdit() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove("edit");
        context.getExternalContext().getSessionMap().put("edit", false);
        isEdit = false;
        User user = userService.getUserById(id);
        if (cityId != 0) {
            user.setCity(cityService.getCityById(cityId));
        }
        if (fbInfo != null) {
            user.setFbInfo(fbInfo);
            System.out.println(fbInfo);
        }
        if (login != null && userService.getUserByLogin(login) == null) {
            user.setLogin(login);
        } else {
            FacesMessage failMes = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Error",
                    "Failed to login!");
            RequestContext.getCurrentInstance().showMessageInDialog(failMes);
        }

        userService.update(user);

    }

    //Friends
    public List<Friend> getSubscribedTo() {
        return friendService.getSubscribedToByUserId(SessionBean.getUserId());
    }

    public void follow() {
        Friend friend = new Friend();
        friend.setSubscribedTo(userService.getUserById(id));
        friend.setSubscriber(userService.getUserById(SessionBean.getUserId()));
        friendService.create(friend);
    }

    public void unfollow() {
        Friend friend = friendService.getFriendBySubIdToId(SessionBean.getUserId(), id);

        friendService.delete(friend.getId());
    }

    public void ban() {
        User user = userService.getUserById(id);
        user.setBanned(true);
        userService.update(user);
    }
    
    public void unban() {
        User user = userService.getUserById(id);
        user.setBanned(false);
        userService.update(user);
    }
    
    public boolean getIsSubscribedTo() {
        return friendService.getFriendBySubIdToId(SessionBean.getUserId(), id) != null;
    }
    
    public boolean getIsBanned() {
        return userService.getUserById(this.id).getBanned() == true;
    }

    public boolean getHaveFbInfo() {
        return userService.getUserById(id).getFbInfo() != null && userService.getUserById(id).getFbInfo().length() > 1;
    }

    //Games
    public List<UserGame> getMyGames() {
        return userGameService.getUserGamesByUserId(id);
    }

    public List<UserGame> getFavouriteGames() {
        return userGameService.getFavoriteUserGamesByUserId(id);
    }

    public List<UserGame> getExchangeGames() {
        return userGameService.getCanExchangeUserGamesByUserId(id);
    }

    public void deleteMyGame(UserGame myGame) {
        if (SessionBean.getUserId() == myGame.getUser().getId()) {
            userGameService.delete(myGame.getId());
        } else {
            FacesMessage errMes = new FacesMessage(FacesMessage.SEVERITY_WARN, "error", "no rights to delete");
            RequestContext.getCurrentInstance().showMessageInDialog(errMes);
        }

    }
    
    public boolean getIsAdmin() {
        return userService.getUserById(SessionBean.getUserId()).getUserRole().getId() == 2;
    }

    public boolean isFacebook() {
        return userService.getUserById(SessionBean.getUserId()).getFbInfo() != null;
    }

    public boolean isVk() {
        return userService.getUserById(SessionBean.getUserId()).getVkInfo() != null;
    }

    public boolean isGoogle() {
        return userService.getUserById(SessionBean.getUserId()).getSteamInfo() != null;
    }

    public void upload(FileUploadEvent event) throws IOException {
        if (event.getFile() != null) {
            cloudUpload upload = new cloudUpload(event.getFile());
            User u = userService.getUserById(id);
            u.setAvatarUrl((String) upload.getUploadResult().get("url"));
            userService.update(u);

        }
    }

    public String exchangeNotification() {
        int quantity = 0;
        List<UserGame> myWantedGames = userGameService.getWantedUserGamesByUserId(SessionBean.getUserId());
        for (UserGame ug : myWantedGames) {
            if (!userGameService.getCanExchangeUserGamesByGameId(ug.getGame().getId()).isEmpty()) {
                quantity++;
            }
        }
        if (quantity == 0) {
            return "Trading page";
        } else {
            return "[+" + Integer.toString(quantity) + "]TradingPage";
        }

    }

}
