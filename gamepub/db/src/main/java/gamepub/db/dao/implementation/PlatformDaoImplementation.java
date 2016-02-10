package gamepub.db.dao.implementation;

import gamepub.db.dao.PlatformDao;
import gamepub.db.entity.Platform;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
public class PlatformDaoImplementation extends BaseDaoImplementation<Platform,Integer> implements PlatformDao {
    public PlatformDaoImplementation(){
        super(Platform.class);
    }

    public Platform getPlatformByName(String name) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Platform> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("name"), name));
        Platform result;
        try {
            result = (Platform)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public Platform getPlatformById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Platform> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        Platform result;
        try {
            result = (Platform)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }
}
