/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.service;

import gamepub.db.dao.implementation.TradeDaoImplementation;
import gamepub.db.entity.Trade;
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
public class TradeService extends TradeDaoImplementation{
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
    public Trade getTradeById(Integer id) {
        return super.getTradeById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Trade> getActiveTrades() {
        return super.getActiveTrades();
    }
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Trade> getTradesByOfferingUserId(Integer id) {
        return super.getTradesByOfferingUserId(id);
    }
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Trade getLastTradeByOfferingUserId(Integer id) {
        return super.getLastTradeByOfferingUserId(id);
    }
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Trade> getTradesByReceivingUserId(Integer id) {
        return super.getTradesByReceivingUserId(id);
    }
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Trade create(Trade trade) {
        return super.create(trade);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Trade find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Trade update(Trade trade) {
        return super.update(trade);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Trade> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<Trade> t) {
        super.delete(t);
    }

}
