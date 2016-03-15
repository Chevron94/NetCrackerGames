package gamepub.beans;

import gamepub.db.entity.Game;
import gamepub.db.service.GameService;
import gamepub.parse.Match;
import gamepub.parse.Tournament;
import java.io.IOException;
import java.net.UnknownHostException;
import sun.util.resources.cldr.es.CalendarData_es_GQ;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by �������� on 17.12.2015.
 */
@ManagedBean
@SessionScoped
public class MainPageBean {
    List<Game> myGames;
    @EJB
    GameService gameService;
    @EJB
    Tournament tournamentService;

    public List<Game> getTopMetacriticGames() {
        /*myGames = gameService.findAll();

        List<Game> tmp= new ArrayList<Game>(4);
        for (int i=0; i<4; i++){
            tmp.add(myGames.get(i));
        }*/
        List<Game> games = gameService.getGamesOrderByMarks(6);
        List<Game> result = new ArrayList<Game>();
        for(int i = 0; result.size()<6; i++){
            if (!result.contains(games.get(i)))
                result.add(games.get(i));
        }
        return result;
    }

    public List<Game> getTopUserGames(){
        List<Game> test = gameService.getGamesOrderByUserMarks(6);
        return gameService.getGamesOrderByUserMarks(6);
    }

    public String goToConcreteGame() {
        return "game?faces-redirect=true";
    }

    /**
     * @return the matches
     * @throws java.net.UnknownHostException
     */
    public List<Match> getMatches() throws Exception {       
        return tournamentService.getMatches();        
    }
    public List<Match> getCsMatches() throws Exception {       
        return tournamentService.getCsMatches();        
    }
}