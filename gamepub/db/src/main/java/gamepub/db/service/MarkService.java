package gamepub.db.service;

import gamepub.db.dao.implementation.MarkDaoImplementation;
import gamepub.db.entity.Game;
import gamepub.db.entity.GameScreenshot;
import gamepub.db.entity.Mark;

import javax.ejb.EJB;
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
public class MarkService extends MarkDaoImplementation {
    @PersistenceContext(unitName = "PERSISTENCE_WEB")
    protected EntityManager em;

    @EJB
    GameService gameService;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected void closeEntityManager() {
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Mark getMarkById(Integer id) {
        return super.getMarkById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Mark getMarkByUserAndGameId(Integer userId, Integer gameId) {
        return super.getMarkByUserAndGameId(userId, gameId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Mark> getMarksByUserId(Integer id) {
        return super.getMarksByUserId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Mark> getMarksByGameId(Integer id) {
        return super.getMarksByGameId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Double getAvgMarkByGameId(Integer gameId) {
        return super.getAvgMarkByGameId(gameId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Mark create(Mark mark) {

        Mark m = super.create(mark);
        Game g = m.getGame();
        g.setAvgMark(getAvgMarkByGameId(g.getId()));
        gameService.update(g);
        return m;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Mark find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Mark update(Mark mark) {
        return super.update(mark);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Mark> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<Mark> t) {
        super.delete(t);
    }
}
