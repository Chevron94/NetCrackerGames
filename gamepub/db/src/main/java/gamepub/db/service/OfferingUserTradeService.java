/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.service;

import gamepub.db.dao.implementation.OfferingUserTradeDaoImplementation;
import gamepub.db.entity.OfferingUserTrade;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author fitok
*/
@Stateless
public class OfferingUserTradeService extends OfferingUserTradeDaoImplementation{
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
    public OfferingUserTrade getOfferingUserTradeById(Integer id) {
        return super.getOfferingUserTradeById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<OfferingUserTrade> getOfferingUserTradesByTradeId(Integer id) {
        return super.getOfferingUserTradesByTradeId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<OfferingUserTrade> getOfferingUserTradesByOfferingGameId(Integer id) {
        return super.getOfferingUserTradesByOfferingGameId(id);
    }
    

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OfferingUserTrade create(OfferingUserTrade trade) {
        return super.create(trade);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public OfferingUserTrade find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OfferingUserTrade update(OfferingUserTrade friend) {
        return super.update(friend);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<OfferingUserTrade> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<OfferingUserTrade> t) {
        super.delete(t);
    }
}
