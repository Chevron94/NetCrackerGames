package gamepub.db.service;

import gamepub.db.dao.implementation.GamePlatformDaoImplementation;
import gamepub.db.entity.GamePlatform;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
@Stateless
public class GamePlatformService extends GamePlatformDaoImplementation {
    @PersistenceContext(unitName = "PERSISTENCE_WEB")
    protected EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected void closeEntityManager() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public GamePlatform getGamePlatformById(Integer id) {
        return super.getGamePlatformById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<GamePlatform> getGamePlatformsByGameId(Integer id) {
        return super.getGamePlatformsByGameId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<GamePlatform> getGamePlatformsByPlatformId(Integer id) {
        return super.getGamePlatformsByPlatformId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public GamePlatform getGamePlatformByGameIdAndPlatformId(Integer gameId, Integer platformId) {
        return super.getGamePlatformByGameIdAndPlatformId(gameId, platformId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public GamePlatform create(GamePlatform gamePlatform) {
        return super.create(gamePlatform);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public GamePlatform find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public GamePlatform update(GamePlatform gamePlatform) {
        return super.update(gamePlatform);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<GamePlatform> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<GamePlatform> t) {
        super.delete(t);
    }
}
