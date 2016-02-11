package gamepub.db.dao.implementation;

import gamepub.db.dao.PrivateMessageDao;
import gamepub.db.entity.PrivateMessage;
import gamepub.db.entity.User;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 05.12.15.
 */
public class PrivateMessagesDaoImplementation extends BaseDaoImplementation<PrivateMessage, Integer> implements PrivateMessageDao {
    public PrivateMessagesDaoImplementation() {
        super(PrivateMessage.class);
    }

    public PrivateMessage getPrivateMessageById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<PrivateMessage> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        PrivateMessage result;
        try {
            result = (PrivateMessage) getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            result = null;
        } finally {
            closeEntityManager();
        }
        return result;
    }

    public PrivateMessage getPrivateMessageBySenderIdAndReceiverIdAndDate(Integer senderId, Integer receiverId, Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<PrivateMessage> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("sender").<Integer>get("id"), senderId),
                cb.equal(root.<User>get("receiver").<Integer>get("id"), receiverId),
                cb.equal(root.<Date>get("date"), date));
        PrivateMessage result;
        try {
            result = (PrivateMessage) getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            result = null;
        } finally {
            closeEntityManager();
        }
        return result;
    }

    public List<PrivateMessage> getSendedPrivateMessagesBySenderId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<PrivateMessage> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("sender").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        cq.orderBy(cb.desc(root.<Date>get("date")));
        closeEntityManager();
        return result;
    }

    public List<PrivateMessage> getReceivedPrivateMessagesByReceiverId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<PrivateMessage> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("receiver").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        cq.orderBy(cb.desc(root.<Date>get("date")));
        closeEntityManager();
        return result;
    }

    public List<PrivateMessage> getPrivateMessagesBySenderIdAndReceiverId(Integer senderId, Integer receiverId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<PrivateMessage> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("sender").<Integer>get("id"), senderId),
                cb.equal(root.<User>get("receiver").<Integer>get("id"), receiverId));
        cq.orderBy(cb.desc(root.<Date>get("date")));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        return result;
    }

    public List<PrivateMessage> getAllPrivateMessagesBySenderIdAndReceiverId(Integer senderId, Integer receiverId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<PrivateMessage> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.or(cb.and(cb.equal(root.<User>get("sender").<Integer>get("id"), senderId),
                cb.equal(root.<User>get("receiver").<Integer>get("id"), receiverId)),
                cb.and(cb.equal(root.<User>get("sender").<Integer>get("id"), receiverId),
                        cb.equal(root.<User>get("receiver").<Integer>get("id"), senderId))));
        cq.orderBy(cb.desc(root.<Date>get("date")));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        return result;
    }
}
