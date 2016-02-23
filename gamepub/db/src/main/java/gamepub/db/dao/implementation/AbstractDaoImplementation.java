package gamepub.db.dao.implementation;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by roman on 05.12.15.
 */
public abstract class AbstractDaoImplementation<T, PK> {
    protected EntityManager em;
    protected abstract EntityManager getEntityManager();
    protected abstract void closeEntityManager();

    protected List<T> executeQuery(String jpql, Map<String, Object> parameters){
        em = getEntityManager();
        Query query = em.createQuery(jpql);
        for(Map.Entry<String,Object> entry : parameters.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        List<T> result = query.getResultList();
        closeEntityManager();
        return result;
    }

    protected List<T> executeQuery(String jpql){
        em = getEntityManager();
        List<T> result = em.createQuery(jpql).getResultList();
        closeEntityManager();
        return result;
    }

    protected List<T> executeQuery(String jpql, Map<String, Object> parameters, int maxResult){
        em = getEntityManager();
        Query query = em.createQuery(jpql).setMaxResults(maxResult);
        for(Map.Entry<String,Object> entry : parameters.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        List<T> result = query.getResultList();
        closeEntityManager();
        return result;
    }
}