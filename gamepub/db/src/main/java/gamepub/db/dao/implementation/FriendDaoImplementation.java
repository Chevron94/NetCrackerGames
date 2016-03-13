package gamepub.db.dao.implementation;

import gamepub.db.dao.FriendDao;
import gamepub.db.entity.Friend;
import gamepub.db.entity.User;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 05.12.15.
 */
public class FriendDaoImplementation extends BaseDaoImplementation<Friend, Integer> implements FriendDao {
    public FriendDaoImplementation(){
        super(Friend.class);
    }

    public Friend getFriendById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Friend> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        try {
            return (Friend)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public Friend getFriendBySubIdToId(Integer subId, Integer subToId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Friend> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("subscribedTo").<Integer>get("id"), subToId),
                cb.equal(root.<User>get("subscriber").<Integer>get("id"), subId));
        Friend result;
        try {
            result = (Friend)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }


    public List<Friend> getSubscribersByUserId(Integer id) {
        Boolean tr = true;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Friend> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.and(cb.equal(root.<User>get("subscribedTo").<Integer>get("id"), id),
                cb.isFalse(root.<Boolean>get("block"))));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        return result;
    }

    public List<Friend> getSubscribedToByUserId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Friend> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.and(cb.equal(root.<User>get("subscriber").<Integer>get("id"), id),
                cb.isFalse(root.<Boolean>get("block"))));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        return result;
    }
}
