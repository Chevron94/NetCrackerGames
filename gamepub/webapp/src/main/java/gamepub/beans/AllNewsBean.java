package gamepub.beans;

import gamepub.db.entity.Game;
import gamepub.db.entity.News;
import gamepub.db.service.GameService;
import gamepub.db.service.NewsService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Created by roman on 03.12.15.
 */
@ManagedBean
@SessionScoped
@ViewScoped
public class AllNewsBean {

    String newsName;
    String gameId;
    Date date;
    int startNews = 0, endNews = 10;

    List<News> news;

    @EJB
    NewsService newsService;

    @EJB
    GameService gameService;

    @PostConstruct
    public void init() {
        news = getNewses();
    }

    public List<News> getNewses() {
        List<HashMap.Entry<String, Object>> parametersList = new ArrayList<HashMap.Entry<String, Object>>();
        Map.Entry<String, Object> param;
        FacesContext context = FacesContext.getCurrentInstance();
        if(newsName != null && newsName.length()>0) {
            param = new HashMap.SimpleEntry<String, Object>("name", newsName);
            parametersList.add(param);
        }
        if(gameId != null && gameId.length()>0) {
            param = new HashMap.SimpleEntry<String, Object>("game", gameService.getGameByUid(gameId));
            parametersList.add(param);
        }
        if(date != null) {
            param = new HashMap.SimpleEntry<String, Object>("date", date);
            parametersList.add(param);
        }
        return newsService.getNewsByCustomParams(parametersList,false,startNews,endNews);
    }

    public List<News> getTNews(){
        return news;
    }

    public void search(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if(newsName != null){
            facesContext.getExternalContext().getSessionMap().put("name", newsName);
        }

        if(gameId != null){
            facesContext.getExternalContext().getSessionMap().put("gameId", gameId);
        }

        if(date != null) {
            facesContext.getExternalContext().getSessionMap().put("date", date);
        }
        news=getNewses();
    }

    public void loadMore() {
        startNews+=4;
        endNews+=4;
        news.addAll(getNewses());

    }

    public String goToConcreteNews() {
        return "news?faces-redirect=true";
    }

    public String getNewsName() {
        return newsName;
    }

    public List<Game> getGames() {
        List<Game> res = new ArrayList<Game>();
        Game g = new Game();
        g.setName("None");
        g.setId(0);
        g.setUid("");
        res.add(g);
        res.addAll(gameService.getGamesWhichHaveNews());
        return res;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
