package gamepub.db.dao;

import gamepub.db.entity.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 05.12.15.
 */
public interface UserDao extends BaseDao<User, Integer> {
    public User getUserById(Integer id);
    public User getUserByUid(String uid);
    public User getUserByLogin(String login);
    public User getUserByLoginAndPassword(String login, String password);
    public User getUserByEmail(String email);
    public User getUserByToken(String token);
    public User getUserByVkInfo(String vkInfo);
    public User getUserBySteamInfo(String steamInfo);
    public User getUserByFbInfo(String fbInfo);
    public User getUserByApiToken(String token);
    public void refreshRequestsCount();
    
    public List<User> getUsersByUserRoleId(Integer id);
    public List<User> getUsersByCityId(Integer id);
    public List<User> getUsersByCountryId(Integer id);
    public List<User> getUsersByCustomParams(List<HashMap.Entry<String, Object>> parameterList);
}
