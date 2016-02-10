package gamepub.db.service;

import gamepub.db.dao.implementation.GameDaoImplementation;
import gamepub.db.entity.Game;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
@Stateless
public class GameService extends GameDaoImplementation {
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
    public Game getGameById(Integer id) {
        return super.getGameById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Game> getGamesByName(String name) {
        return super.getGamesByName(name);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Game> getGamesByCustomParams(List<HashMap.Entry<String, Object>> parameterList) {
        return super.getGamesByCustomParams(parameterList);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Game getGameByUid(String uid) {
        return super.getGameByUid(uid);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Game> getGamesOrderByMarks(int maxValue) {
        return super.getGamesOrderByMarks(maxValue);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Game create(Game game) {
        return super.create(game);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Game find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Game update(Game game) {
        return super.update(game);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Game> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<Game> t) {
        super.delete(t);
    }
}
