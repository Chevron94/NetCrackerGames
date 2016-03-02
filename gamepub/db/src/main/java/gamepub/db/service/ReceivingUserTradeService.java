/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.service;

import gamepub.db.dao.implementation.ReceivingUserTradeDaoImplementation;
import gamepub.db.entity.ReceivingUserTrade;
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
public class ReceivingUserTradeService extends ReceivingUserTradeDaoImplementation{
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
    public ReceivingUserTrade getReceivingUserTradeById(Integer id) {
        return super.getReceivingUserTradeById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<ReceivingUserTrade> getReceivingUserTradesByTradeId(Integer id) {
        return super.getReceivingUserTradesByTradeId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<ReceivingUserTrade> getReceivingUserTradesByReceivingGameId(Integer id) {
        return super.getReceivingUserTradesByReceivingGameId(id);
    }
    

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ReceivingUserTrade create(ReceivingUserTrade trade) {
        return super.create(trade);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public ReceivingUserTrade find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ReceivingUserTrade update(ReceivingUserTrade friend) {
        return super.update(friend);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<ReceivingUserTrade> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<ReceivingUserTrade> t) {
        super.delete(t);
    }
}
