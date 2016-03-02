/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.dao.implementation;

import gamepub.db.dao.ReceivingUserTradeDao;
import gamepub.db.entity.Game;
import gamepub.db.entity.ReceivingUserTrade;
import gamepub.db.entity.Trade;
import gamepub.db.entity.User;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author fitok
 */
public class ReceivingUserTradeDaoImplementation extends BaseDaoImplementation<ReceivingUserTrade,Integer> implements ReceivingUserTradeDao{
    
    public ReceivingUserTradeDaoImplementation() {
        super(ReceivingUserTrade.class);
    }
     public ReceivingUserTrade getReceivingUserTradeById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<ReceivingUserTrade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        try {
            return (ReceivingUserTrade)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<ReceivingUserTrade> getReceivingUserTradesByTradeId(Integer id) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Trade>get("receivingTrade").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }

    public List<ReceivingUserTrade> getReceivingUserTradesByReceivingGameId(Integer id) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Game>get("receivingGame").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }
  
}
