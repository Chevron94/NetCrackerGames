package gamepub.db.service;

import gamepub.db.dao.implementation.NewsDaoImplementation;
import gamepub.db.entity.News;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
@Stateless
public class NewsService extends NewsDaoImplementation {
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
    public News getNewsById(Integer id) {
        return super.getNewsById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<News> getNewsByName(String name) {
        return super.getNewsByName(name);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<News> getNewsByGameId(Integer id) {
        return super.getNewsByGameId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<News> getNewsByDate(Date date) {
        return super.getNewsByDate(date);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<News> getNewsOrderByDate() {
        return super.getNewsOrderByDate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public News getNewsByUid(String uid) {
        return super.getNewsByUid(uid);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<News> getNewsByCustomParams(List<HashMap.Entry<String, Object>> parameterList) {
        return super.getNewsByCustomParams(parameterList);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public News create(News news) {
        return super.create(news);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public News find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public News update(News news) {
        return super.update(news);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<News> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<News> t) {
        super.delete(t);
    }
}
