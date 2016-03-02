/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.dao.implementation;

import gamepub.db.dao.OfferingUserTradeDao;
import gamepub.db.entity.Game;
import gamepub.db.entity.OfferingUserTrade;
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
public class OfferingUserTradeDaoImplementation extends BaseDaoImplementation<OfferingUserTrade,Integer> implements OfferingUserTradeDao{
    public OfferingUserTradeDaoImplementation() {
        super(OfferingUserTrade.class);
    }

    public OfferingUserTrade getOfferingUserTradeById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<OfferingUserTrade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        try {
            return (OfferingUserTrade)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<OfferingUserTrade> getOfferingUserTradesByTradeId(Integer id) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Trade>get("offeringTrade").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }

    public List<OfferingUserTrade> getOfferingUserTradesByOfferingGameId(Integer id) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Game>get("offeringGame").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }

    
    
    
}
