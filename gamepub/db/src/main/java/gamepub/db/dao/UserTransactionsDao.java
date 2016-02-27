package gamepub.db.dao;

import gamepub.db.entity.UserScreenshot;
import gamepub.db.entity.UserTransaction;

/**
 * Created by roman on 05.12.15.
 */
public interface UserTransactionsDao extends BaseDao<UserTransaction, Integer> {
    UserTransaction getTransactionById(Integer id);
    int getMaxId();
}
