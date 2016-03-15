package gamepub.db.dao;

import gamepub.db.entity.Mark;

import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
public interface MarkDao extends BaseDao<Mark,Integer> {
    public Mark getMarkById(Integer id);
    public Mark getMarkByUserAndGameId(Integer userId, Integer gameId);
    public Double getAvgMarkByGameId(Integer gameId);
    public List<Mark> getMarksByUserId(Integer id);
    public List<Mark> getMarksByGameId(Integer id);
}
