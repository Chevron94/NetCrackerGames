package gamepub.db.dao.implementation;

import gamepub.db.dao.UserTransactionsDao;
import gamepub.db.entity.UserTransaction;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserTransactionsDaoImplementation extends BaseDaoImplementation<UserTransaction,Integer> implements UserTransactionsDao {

    public UserTransactionsDaoImplementation(){
        super(UserTransaction.class);
    }

    public UserTransaction getTransactionById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<UserTransaction> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        UserTransaction result;
        try {
            result = (UserTransaction)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public int getMaxId(){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<UserTransaction> root = cq.from(instance);
        cq.select(cb.max(root.<Integer>get("id")));
        Integer result;
        try {
            result = (Integer)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

}
