package gamepub.beans;

import gamepub.db.entity.*;
import gamepub.db.service.*;
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
import java.util.List;
import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;

/**
 * Created by �������� on 06.01.2016.
 */
@ManagedBean
@SessionScoped
public class ProfileBean {

    private int id;
    List<Game> myGames;
    List<UserGame> userGame;
    private boolean isEdit;
    private boolean isMy;
    private int cityId;
    private String email, fbInfo, name, password;

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
        id = SessionBean.getUserId();
        if (!userId.equals("my")) {
            id = Integer.parseInt(userId);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (!context.getExternalContext().getSessionMap().containsKey("edit")) //context.getExternalContext().getSessionMap().remove("edit");
        {
            context.getExternalContext().getSessionMap().put("edit", false);
        }

        User user = userService.getUserById(id);
        isEdit = false;
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
        /*Country country = countryService.getCountryById(7);
        country.setName("12");
        countryService.update(country);*/
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
        //user.setFbInfo("11111111");

        //userService.delete(user.getId());
        userService.update(user);
        /*Country country = countryService.getCountryById(7);
        country.setName("22");
        countryService.update(country);*/

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

    public boolean getIsSubscribedTo() {
        return friendService.getFriendBySubIdToId(SessionBean.getUserId(), id) != null;
    }


    public String goToMessage() {
        HttpSession session = SessionBean.getSession();
        session.setAttribute("receiverId", id);
        return "message";
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
}
