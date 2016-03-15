package gamepub.db.dao.implementation;

import gamepub.db.dao.MarkDao;
import gamepub.db.entity.Game;
import gamepub.db.entity.Mark;
import gamepub.db.entity.User;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
public class MarkDaoImplementation extends BaseDaoImplementation<Mark,Integer> implements MarkDao {
    public MarkDaoImplementation(){
        super(Mark.class);
    }

    public Mark getMarkById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Mark> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        Mark result;
        try {
            result = (Mark)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public Mark getMarkByUserAndGameId(Integer userId, Integer gameId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Mark> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("user").<Integer>get("id"), userId),
                cb.equal(root.<Game>get("game").<Integer>get("id"), gameId));
        Mark result;
        try {
            result = (Mark)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public List<Mark> getMarksByUserId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Mark> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("user").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        return result;
    }

    public List<Mark> getMarksByGameId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Mark> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Game>get("game").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        return result;
    }

    public Double getAvgMarkByGameId(Integer gameId) {
        String jpa = "SELECT AVG(m.mark) from Mark m where m.game.id = :id";
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id",gameId);
        return this.executeScalarQuery(jpa,parameters);
    }
}
