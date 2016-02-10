package gamepub.db.dao;

import gamepub.db.entity.News;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
public interface NewsDao extends BaseDao<News,Integer> {
    public News getNewsById(Integer id);
    public News getNewsByUid(String uid);
    public List<News> getNewsByName(String name);
    public List<News> getNewsByGameId(Integer id);
    public List<News> getNewsByDate(Date date);
    public List<News> getNewsOrderByDate();
    public List<News> getNewsByCustomParams(List<HashMap.Entry<String, Object>> parameterList);
}
