/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;

import gamepub.dto.GameDto;
import gamepub.db.entity.Comment;
import gamepub.db.entity.Game;
import gamepub.db.entity.GameGenre;
import gamepub.db.entity.GamePlatform;
import gamepub.db.entity.GameScreenshot;
import gamepub.db.entity.GameStatus;
import gamepub.db.entity.Mark;
import gamepub.db.entity.Platform;
import gamepub.db.entity.User;
import gamepub.db.entity.UserGame;
import gamepub.db.service.CommentService;
import gamepub.db.service.GameGenreService;
import gamepub.db.service.GamePlatformService;
import gamepub.db.service.GameScreenshotService;
import gamepub.db.service.GameService;
import gamepub.db.service.GameStatusService;
import gamepub.db.service.MarkService;
import gamepub.db.service.PlatformService;
import gamepub.db.service.UserGameService;
import gamepub.db.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.rating.Rating;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;

/**
 * @author ����
 */
@ManagedBean
@SessionScoped
@RequestScoped
public class AboutGameBean {

    @ManagedProperty(value = "#{param.id}")
    private String id;

    @EJB
    GameService gameService;
    @EJB
    UserGameService userGameService;
    @EJB
    UserService userService;
    @EJB
    GameStatusService gameStatusService;
    @EJB
    MarkService markService;
    @EJB
    GamePlatformService gamePlatformService;
    @EJB
    GameGenreService gameGenreService;
    @EJB
    GameScreenshotService gameScreenshotService;

    Game game;
    List<Mark> marksAndReviews;

    boolean drawStatus;

    public List<GameStatus> getStatus() {
        return gameStatusService.findAll();
    }

    public Game getGame() {
        HttpSession ses = SessionBean.getSession();
        Game g = gameService.getGameByUid(id);
        ses.setAttribute("gameid", g.getId());
        UserGame userGame = getUserGame();
        if (userGame != null) {
            disableButtons(userGame);
            drawStatus = true;
            notStatusButton();
            disableStatusButtons(userGame);
        } else {
            drawStatus = false;
        }
        //drawStatusButtons(drawStatus);
        return g;
    }

    public List<String> getImages() {
        List<GameScreenshot> gameScreenshots = gameScreenshotService.getScreenshotsByGameId(SessionBean.getGameId());
        List<String> images = new ArrayList<String>();
        for (GameScreenshot gm : gameScreenshots) {
            images.add(gm.getLink());
        }
        return images;
    }

    public List<Mark> getMarksAndReviews() {
        Integer id = SessionBean.getGameId();
        marksAndReviews = markService.getMarksByGameId(id);
        return marksAndReviews;
    }

    public List<GamePlatform> getGamePlatforms() {
        return gamePlatformService.getGamePlatformsByGameId(SessionBean.getGameId());
    }

    public List<GameGenre> getGameGenres() {
        return gameGenreService.getGameGenresByGameId(SessionBean.getGameId());
    }

    public void addMarkAndReview() {
        FacesContext context = FacesContext.getCurrentInstance();

        UIViewRoot uiViewRoot = context.getViewRoot();
        InputTextarea inputText = null;
        Rating rating = null;
        inputText = (InputTextarea) uiViewRoot.findComponent("markAdderForm:markAdderNewComment");
        rating = (Rating) uiViewRoot.findComponent("markAdderForm:markAdderNewMark");
        int mrk = Integer.valueOf((String) rating.getValue());
        String review = (String) inputText.getValue();

        FacesMessage errMes;
        if (review == null || review.isEmpty() || review.length() >= 501) {
            errMes = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "write a riview");
            RequestContext.getCurrentInstance().showMessageInDialog(errMes);
            return;
        }
        if (mrk == 0) {
            errMes = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "rate this game");
            RequestContext.getCurrentInstance().showMessageInDialog(errMes);
            return;
        }
        Mark m = null;
        try {
            m = markService.getMarkByUserAndGameId(SessionBean.getUserId(),
                    SessionBean.getGameId());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        if (m != null) {
            errMes = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "you've already left a review");
            RequestContext.getCurrentInstance().showMessageInDialog(errMes);
            return;
        }
        Mark mark = new Mark();
        mark.setDate(new java.util.Date());
        mark.setGame(gameService.getGameById(SessionBean.getGameId()));
        mark.setMark(mrk);
        mark.setReview(review);
        mark.setUser(userService.getUserById(SessionBean.getUserId()));
        markService.create(mark);

        inputText.setValue("");
        rating.setValue(0);
        //marksAndReviews = markService.getMarksByGameId(id);
    }

    public void deleteMarkAndReview(Mark mark) {
        if (mark.getUser().getId() == SessionBean.getUserId()) {//��� ����� ������������ ���������
            markService.delete(mark.getId());
        } else {
            FacesMessage errMes = new FacesMessage(FacesMessage.SEVERITY_WARN, "error", "no rights to delete");
            RequestContext.getCurrentInstance().showMessageInDialog(errMes);
        }
    }

    public void addToMyGames() {

        boolean exist = true;
        UserGame userGame = userGameService.getUserGameByUserIdAndGameId(SessionBean.getUserId(), SessionBean.getGameId());
        if (userGame == null) {
            exist = false;
            userGame = new UserGame();
        }
        userGame.setGame(gameService.getGameById(SessionBean.getGameId()));
        userGame.setUser(userService.getUserById(SessionBean.getUserId()));

        int gameStatusId = 0;
        try {
            gameStatusId = userGame.getGameStatus().getId();
        } catch (Exception e) {

        }

        userGame.setCanExchange(false);
        userGame.setWanted(false);
        userGame.setFavorite(false);

        if (!exist) {
            userGame.setGameStatus(gameStatusService.getGameStatusById(1));
            userGameService.create(userGame);
        } else {
            userGame.setGameStatus(gameStatusService.getGameStatusById(gameStatusId));
            userGameService.delete(userGame.getId());
            userGameService.update(userGame);
        }

        notStatusButton();
        disableButtons(userGame);
        drawStatus = true;
        disableStatusButtons(userGame);
    }

    public void addToFavourite() {

        boolean exist = true;
        UserGame userGame = userGameService.getUserGameByUserIdAndGameId(SessionBean.getUserId(), SessionBean.getGameId());
        if (userGame == null) {
            exist = false;
            userGame = new UserGame();
        }
        userGame.setGame(gameService.getGameById(SessionBean.getGameId()));
        userGame.setUser(userService.getUserById(SessionBean.getUserId()));

        int gameStatusId = 0;
        try {
            gameStatusId = userGame.getGameStatus().getId();
        } catch (Exception e) {

        }

        if (!exist) {
            userGame.setGameStatus(gameStatusService.getGameStatusById(1));
            userGame.setCanExchange(false);
            userGame.setWanted(false);
            userGame.setFavorite(true);
            userGameService.create(userGame);
        } else {
            userGame.setGameStatus(gameStatusService.getGameStatusById(gameStatusId));
            userGame.setFavorite(true);
            userGameService.delete(userGame.getId());
            userGameService.update(userGame);
        }

        notStatusButton();
        disableButtons(userGame);
        drawStatus = true;
        disableStatusButtons(userGame);
    }

    public void addToWanted() {

        boolean exist = true;
        UserGame userGame = null;
        try {
            userGame = userGameService.getUserGameByUserIdAndGameId(SessionBean.getUserId(), SessionBean.getGameId());
        } catch (Exception e) {

        }
        if (userGame == null) {
            exist = false;
            userGame = new UserGame();
        }
        userGame.setGame(gameService.getGameById(SessionBean.getGameId()));
        userGame.setUser(userService.getUserById(SessionBean.getUserId()));

        int gameStatusId = 0;
        try {
            gameStatusId = userGame.getGameStatus().getId();
        } catch (Exception e) {

        }

        if (!exist) {
            userGame.setGameStatus(gameStatusService.getGameStatusById(1));
            userGame.setCanExchange(false);
            userGame.setWanted(true);
            userGame.setFavorite(false);
            userGameService.create(userGame);
        } else {
            if (userGame.isCanExchange()) {
                FacesMessage errMes = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "the game can not be directly on the exchange and wanted");
                RequestContext.getCurrentInstance().showMessageInDialog(errMes);
                return;
            }
            userGame.setGameStatus(gameStatusService.getGameStatusById(gameStatusId));
            userGame.setWanted(true);
            userGameService.delete(userGame.getId());
            userGameService.update(userGame);
        }

        notStatusButton();
        disableButtons(userGame);
        drawStatus = true;
        disableStatusButtons(userGame);
    }

    public void addToExchange() {

        boolean exist = true;
        UserGame userGame = null;
        try {
            userGame = userGameService.getUserGameByUserIdAndGameId(SessionBean.getUserId(), SessionBean.getGameId());
        } catch (Exception e) {

        }
        if (userGame == null) {
            exist = false;
            userGame = new UserGame();
        }
        userGame.setGame(gameService.getGameById(SessionBean.getGameId()));
        userGame.setUser(userService.getUserById(SessionBean.getUserId()));

        int gameStatusId = 0;
        try {
            gameStatusId = userGame.getGameStatus().getId();
        } catch (Exception e) {

        }

        if (!exist) {
            userGame.setGameStatus(gameStatusService.getGameStatusById(1));
            userGame.setCanExchange(true);
            userGame.setWanted(false);
            userGame.setFavorite(false);
            userGameService.create(userGame);
        } else {
            if (userGame.isWanted()) {
                FacesMessage errMes = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "the game can not be directly on the exchange and wanted");
                RequestContext.getCurrentInstance().showMessageInDialog(errMes);
                return;
            }
            userGame.setGameStatus(gameStatusService.getGameStatusById(gameStatusId));
            userGame.setCanExchange(true);
            userGameService.delete(userGame.getId());
            userGameService.update(userGame);
        }

        disableButtons(userGame);
        drawStatus = true;
        notStatusButton();
        disableStatusButtons(userGame);
    }

    public void addStatus(int statusId) {
        UserGame userGame = userGameService.getUserGameByUserIdAndGameId(SessionBean.getUserId(), SessionBean.getGameId());
        userGame.setGameStatus(gameStatusService.getGameStatusById(statusId));
        userGameService.update(userGame);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private UserGame getUserGame() {

        UserGame userGame = null;
        try {
            userGame = userGameService.getUserGameByUserIdAndGameId(SessionBean.getUserId(), SessionBean.getGameId());
        } catch (Exception e) {

        }
        return userGame;
    }

    private void disableButtons(UserGame userGame) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = context.getViewRoot();
        CommandButton commandButton;
        if (userGame.isFavorite()) {
            commandButton = (CommandButton) uiViewRoot.findComponent("comForm:favourite");
            commandButton.setDisabled(true);
        }
        if (userGame.isWanted()) {
            commandButton = (CommandButton) uiViewRoot.findComponent("comForm:wanted");
            commandButton.setDisabled(true);
        }
        if (userGame.isCanExchange()) {
            commandButton = (CommandButton) uiViewRoot.findComponent("comForm:exchange");
            commandButton.setDisabled(true);
        }
        if (!userGame.isCanExchange() && !userGame.isWanted() && !userGame.isFavorite()) {
            CommandButton fav=(CommandButton) uiViewRoot.findComponent("comForm:favourite");
            CommandButton wan=(CommandButton) uiViewRoot.findComponent("comForm:wanted");
            CommandButton exc=(CommandButton) uiViewRoot.findComponent("comForm:exchange");
            fav.setDisabled(false);
            wan.setDisabled(false);
            exc.setDisabled(false);
            commandButton = (CommandButton) uiViewRoot.findComponent("comForm:mygame");
            commandButton.setDisabled(true);
        }

    }

    private void disableStatusButtons(UserGame userGame) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = context.getViewRoot();
        CommandButton commandButton;
        if (userGame.getGameStatus().getId() == 1) {
            commandButton = (CommandButton) uiViewRoot.findComponent("statusForm:notstarted");
            commandButton.setDisabled(true);
        }
        if (userGame.getGameStatus().getId() == 2) {
            commandButton = (CommandButton) uiViewRoot.findComponent("statusForm:passed");
            commandButton.setDisabled(true);
        }
        if (userGame.getGameStatus().getId() == 3) {
            commandButton = (CommandButton) uiViewRoot.findComponent("statusForm:duringpassage");
            commandButton.setDisabled(true);
        }
        if (userGame.getGameStatus().getId() == 4) {
            commandButton = (CommandButton) uiViewRoot.findComponent("statusForm:abandoned");
            commandButton.setDisabled(true);
        }
    }

    private void notStatusButton() {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = context.getViewRoot();
        CommandButton b1, b2, b3, b4;
        b1 = (CommandButton) uiViewRoot.findComponent("statusForm:notstarted");
        b2 = (CommandButton) uiViewRoot.findComponent("statusForm:passed");
        b3 = (CommandButton) uiViewRoot.findComponent("statusForm:duringpassage");
        b4 = (CommandButton) uiViewRoot.findComponent("statusForm:abandoned");
        b1.setDisabled(false);
        b2.setDisabled(false);
        b3.setDisabled(false);
        b4.setDisabled(false);
    }

    public boolean getDrawStatus() {
        UserGame userGame = getUserGame();
        if (getUserGame() != null) {
            notStatusButton();
            disableStatusButtons(userGame);
            return true;
        }
        return false;
    }

    private void drawStatusButtons(boolean drawStatus) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = context.getViewRoot();
        CommandButton b1, b2, b3, b4;
        b1 = (CommandButton) uiViewRoot.findComponent("statusForm:notstarted");
        b2 = (CommandButton) uiViewRoot.findComponent("statusForm:passed");
        b3 = (CommandButton) uiViewRoot.findComponent("statusForm:duringpassage");
        b4 = (CommandButton) uiViewRoot.findComponent("statusForm:abandoned");
        b1.setRendered(drawStatus);
        b2.setRendered(drawStatus);
        b3.setRendered(drawStatus);
        b4.setRendered(drawStatus);
    }
}
