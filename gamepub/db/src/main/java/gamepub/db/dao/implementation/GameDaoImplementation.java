package gamepub.db.dao.implementation;

import com.mchange.v2.uid.UidUtils;
import gamepub.db.dao.GameDao;
import gamepub.db.entity.Game;
import gamepub.db.entity.Platform;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by roman on 06.12.15.
 */
public class GameDaoImplementation extends BaseDaoImplementation<Game, Integer> implements GameDao {
    public GameDaoImplementation() {
        super(Game.class);
    }

    public Game getGameById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Game> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        Game result;
        try {
            result = (Game)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public Game getGameByUid(String uid) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Game> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("uid"), uid));
        Game result;
        try {
            result = (Game)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public List<Game> getGamesByName(String name, boolean all, Integer start, Integer count) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Game> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("name"), name));
        cq.orderBy(cb.asc(root.<String>get("name")));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        if (all)
            return result;
        else {
            List<Game> resList = new ArrayList<Game>();
            for (int i = 0; (i<count) && (result.size()>start+i); i++){
                resList.add((Game)result.get(start+count));
            }
            return resList;
        }
    }

    public List<Game> getGamesWhichHaveNews() {
        String jpa = "Select DISTINCT n.game from News n order by n.game.name";
        return this.executeQuery(jpa);
    }

    public List<Game> getGamesByCustomParams(List<HashMap.Entry<String, Object>> parameterList, boolean all, Integer start, Integer count) {
        String jpa = "Select DISTINCT g.game FROM GameGenre g, GamePlatform gp WHERE gp.game=g.game";
        if (parameterList.size() == 0) {
            List<Game> result = this.executeQuery(jpa);
            if (all)
                return result;
            else {
                List<Game> resList = new ArrayList<Game>();
                for (int i = 0; (i<count) && (result.size()>start+i); i++){
                    resList.add(result.get(start+count));
                }
                return resList;
            }
        }
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        for (HashMap.Entry<String, Object> param : parameterList) {
            if (param.getKey().equals("name")) {
                jpa += " AND g.game.name LIKE :name";
                parameters.put(param.getKey(), param.getValue() + "%");
            } else {
                if (param.getKey().equals("platform")) {
                    List<Platform> platforms = (List<Platform>)param.getValue();
                    if (platforms.size()==0){
                        jpa += " AND gp.platform= :platform";
                        parameters.put(param.getKey(), platforms.get(0));
                    }else{
                        jpa += " AND ( gp.platform= :platform";
                        parameters.put("platform",platforms.get(0));
                        for(int i=1; i<platforms.size(); i++){
                            jpa+= " OR gp.platform= :platform"+i;
                            parameters.put("platform"+i,platforms.get(i));
                        }
                        jpa+=" )";
                    }

                } else {
                    if (param.getKey().equals("genre")) {
                        jpa += " AND g.genre= :genre";
                    } else jpa += " AND gp.releaseDate<= :dateGame";
                    parameters.put(param.getKey(), param.getValue());
                }

            }
        }
        List<Game> result = this.executeQuery(jpa, parameters);
        if (all)
            return result;
        else {
            List<Game> resList = new ArrayList<Game>();
            for (int i = 0; (i<count) && (result.size()>start+i); i++){
                resList.add(result.get(start+count));
            }
            return resList;
        }
    }

    public List<Game> getGamesOrderByMarks(int maxValue){
        String jpa = "Select gp.game from GamePlatform gp Order by gp.metacritic desc, gp.game.name";
        List<Game> queryResult = this.executeQuery(jpa);
        List<Game> result = new ArrayList<Game>();
        for(int i = 0; i<Math.min(maxValue,queryResult.size()); i++){
            result.add(queryResult.get(i));
        }
        return result;
    }

    @Override
    public Game create(Game game) {
        game.setUid(UUID.randomUUID().toString());
        return super.create(game);
    }
}
