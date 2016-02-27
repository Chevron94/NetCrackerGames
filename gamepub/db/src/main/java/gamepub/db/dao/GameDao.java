package gamepub.db.dao;

import gamepub.db.entity.Game;

import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
public interface GameDao extends BaseDao<Game,Integer> {
    public Game getGameById(Integer id);
    public Game getGameByUid(String uid);
    public List<Game> getGamesWhichHaveNews();
    public List<Game> getGamesByName(String name, boolean all, Integer start, Integer count);
    public List<Game> getGamesByCustomParams(List<HashMap.Entry<String, Object>> parameters, boolean all, Integer start, Integer count);
    public List<Game> getGamesOrderByMarks(int maxValue);
}
