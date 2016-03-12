/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.dao.implementation;

import gamepub.db.dao.TradeDao;
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
public class TradeDaoImplementation extends BaseDaoImplementation<Trade, Integer> implements TradeDao{

    public TradeDaoImplementation() {
        super(Trade.class);
    }

    public Trade getTradeById(Integer id) {
         CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        try {
            return (Trade)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<Trade> getActiveTrades() {
         CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Boolean>get("status"), Boolean.TRUE));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }

    public List<Trade> getTradesByOfferingUserId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("offeringUser").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }
    public Trade getLastTradeByOfferingUserId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("offeringUser").<Integer>get("id"), id));        
        List<Trade> result = getEntityManager().createQuery(cq).getResultList();
        return result.get(result.size()-1);
        
    }
    public List<Trade> getTradesByReceivingUserId(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<User>get("receivingUser").<Integer>get("id"), id));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }

    public List<Trade> getTradesByStatus(String status) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Trade> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("status"),status));
        List result = getEntityManager().createQuery(cq).getResultList();
        return result;
    }
    

    
    
}
