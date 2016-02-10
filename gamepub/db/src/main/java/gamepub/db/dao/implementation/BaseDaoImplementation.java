package gamepub.db.dao.implementation;

import gamepub.db.dao.BaseDao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by roman on 05.12.15.
 */
public class BaseDaoImplementation<T,PK> extends AbstractDaoImplementation<T,PK> implements BaseDao<T,PK> {
    protected Class<T> instance;

    public BaseDaoImplementation(Class<T> instance)
    {
        this.instance = instance;
    }

    @Override
    protected EntityManager getEntityManager() {
        if (em==null || !em.isOpen())
            em = EntityManagerCreator.CreateEntityManager();
        if (em.getTransaction() == null || !em.getTransaction().isActive())
            em.getTransaction().begin();
        return em;
    }

    @Override
    protected void closeEntityManager() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
        if (em != null && em.isOpen())
            em.close();
    }

    public T create(T t) {
        em = getEntityManager();
        t = em.merge(t);
        closeEntityManager();
        return t;
    }

    public T find(PK id) {
        em = getEntityManager();
        try{
           return em.find(instance,id);
        }catch (Exception e){
            return null;
        }finally {
            closeEntityManager();
        }
    }

    public T update(T t) {
        em  = getEntityManager();
        t = em.merge(t);
        closeEntityManager();
        return t;
    }

    public void delete(PK id) {
        em  = getEntityManager();
        T t = em.find(instance, id);
        em.remove(t);
        closeEntityManager();
    }

    public List<T> findAll() {
        em  = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(instance);
        Root<T> rootEntry = cq.from(instance);
        CriteriaQuery<T> all = cq.select(rootEntry);
        List<T> t = em.createQuery(all).getResultList();
        closeEntityManager();
        return t;
    }

    public void delete(List<T> t) {
        em  = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> cq = cb.createCriteriaDelete(instance);
        Root<T> rootEntry = cq.from(instance);
        cq.where(rootEntry.in(t));
        em.createQuery(cq).executeUpdate();
        closeEntityManager();
    }
}
