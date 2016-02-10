package gamepub.db.dao;

import gamepub.db.entity.GamePlatform;

import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
public interface GamePlatformDao extends BaseDao<GamePlatform,Integer> {
    public GamePlatform getGamePlatformById(Integer id);
    public GamePlatform getGamePlatformByGameIdAndPlatformId(Integer gameId, Integer platformId);
    public List<GamePlatform> getGamePlatformsByGameId(Integer id);
    public List<GamePlatform> getGamePlatformsByPlatformId(Integer id);
}
