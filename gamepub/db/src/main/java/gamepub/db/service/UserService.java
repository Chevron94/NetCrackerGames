package gamepub.db.service;

import gamepub.db.dao.implementation.UserDaoImplementation;
import gamepub.db.entity.User;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 06.12.15.
 */
@Stateless
public class UserService extends UserDaoImplementation {
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
    public User getUserById(Integer id) {
        return super.getUserById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByLogin(String login) {
        return super.getUserByLogin(login);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByLoginAndPassword(String login, String password) {
        return super.getUserByLoginAndPassword(login, password);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByEmail(String email) {
        return super.getUserByEmail(email);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByVkInfo(String vkInfo) {
        return super.getUserByVkInfo(vkInfo);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserBySteamInfo(String steamInfo) {
        return super.getUserBySteamInfo(steamInfo);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByGoogleInfo(String googleInfo) {
        return super.getUserByGoogleInfo(googleInfo);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByFbInfo(String fbInfo) {
        return super.getUserByFbInfo(fbInfo);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> getUsersByUserRoleId(Integer id) {
        return super.getUsersByUserRoleId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> getUsersByCityId(Integer id) {
        return super.getUsersByCityId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> getUsersByCountryId(Integer id) {
        return super.getUsersByCountryId(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByUid(String uid) {
        return super.getUserByUid(uid);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByToken(String token) {
        return super.getUserByToken(token);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User getUserByApiToken(String token) {
        return super.getUserByApiToken(token);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> getUsersByCustomParams(List<HashMap.Entry<String, Object>> parameterList) {
        return super.getUsersByCustomParams(parameterList);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User create(User user) {
        return super.create(user);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User update(User user) {
        return super.update(user);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<User> t) {
        super.delete(t);
    }
}
