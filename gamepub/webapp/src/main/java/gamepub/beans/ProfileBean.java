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
    @EJB
    TradeService tradeService;
    @EJB 
    OfferingUserTradeService offeringUserTradeService;
    @EJB 
    ReceivingUserTradeService receivingUserTradeService;
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

    public boolean getIsSubscribedTo() {
        return friendService.getFriendBySubIdToId(SessionBean.getUserId(), id) != null;
    }

    public boolean getHaveFbInfo() {
        return userService.getUserById(id).getFbInfo() != null && userService.getUserById(id).getFbInfo().length() > 1;
    }

    //Games
    public List<UserGame> getSimpleAndFavouriteGames(){
        List<UserGame> allMyGames= userGameService.getUserGamesByUserId(id);
        List<UserGame> simpleGames=new ArrayList<UserGame>();
        for(UserGame usergame: allMyGames){
            if(!usergame.isCanExchange() && !usergame.isWanted())
                simpleGames.add(usergame);
        }
        return simpleGames;
    }
    
    public List<UserGame> getWantedGames() {
        return userGameService.getWantedUserGamesByUserId(id);
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

    public boolean isFacebook() {
        if (userService.getUserById(SessionBean.getUserId()).getFbInfo() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isVk() {
        if (userService.getUserById(SessionBean.getUserId()).getVkInfo() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGoogle() {
        if (userService.getUserById(SessionBean.getUserId()).getSteamInfo() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void upload(FileUploadEvent event) throws IOException {
        if (event.getFile() != null) {
            cloudUpload upload = new cloudUpload(event.getFile());
            User u = userService.getUserById(SessionBean.getUserId());
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
    public List<Trade> getSentTradeOffers(){
        return tradeService.getTradesByOfferingUserId(SessionBean.getUserId());
    }
    public List<Trade> getReceivedTradeOffers(){
        return tradeService.getTradesByReceivingUserId(SessionBean.getUserId());
    }
    public List<OfferingUserTrade> getOfferedGames(Trade trade){
        return offeringUserTradeService.getOfferingUserTradesByTradeId(trade.getId());
    }
    public List<ReceivingUserTrade> getRecievedGames(Trade trade){
        return receivingUserTradeService.getReceivingUserTradesByTradeId(trade.getId());
    }
    public boolean checkForOpened(Trade trade){
        return trade.getStatus().equals("opened");
    }
    public boolean checkForInProgress(Trade trade){
        return trade.getStatus().equals("inProgress");
    }
    public boolean checkForConfirmed(Trade trade){
        return trade.getStatus().equals("confirmed");
    }

    public void setInProgress(Trade trade){
        trade.setStatus("inProgress");
        tradeService.update(trade);
    }
    public void setConfirmed(Trade trade){
        trade.setStatus("confirmed");
        tradeService.update(trade);
    }
    public void declineOffer(Trade trade){
        receivingUserTradeService.delete(receivingUserTradeService.getReceivingUserTradesByTradeId(trade.getId()));
        offeringUserTradeService.delete(offeringUserTradeService.getOfferingUserTradesByTradeId(trade.getId()));
        tradeService.delete(trade.getId());
    }
}
