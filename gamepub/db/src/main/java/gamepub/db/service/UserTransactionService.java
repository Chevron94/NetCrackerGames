package gamepub.db.service;

import gamepub.db.dao.implementation.UserScreenshotDaoImplementation;
import gamepub.db.dao.implementation.UserTransactionsDaoImplementation;
import gamepub.db.entity.UserScreenshot;
import gamepub.db.entity.UserTransaction;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
@Stateless
public class UserTransactionService extends UserTransactionsDaoImplementation {
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
    public UserTransaction getTransactionById(Integer id) {
        return super.getTransactionById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UserTransaction create(UserTransaction userTransaction) {
        return super.create(userTransaction);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public UserTransaction find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UserTransaction update(UserTransaction userTransaction) {
        return super.update(userTransaction);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<UserTransaction> findAll() {
        return super.findAll();
    }


}
