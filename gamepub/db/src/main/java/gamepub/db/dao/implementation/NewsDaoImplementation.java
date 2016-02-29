package gamepub.db.dao.implementation;

import gamepub.db.dao.NewsDao;
import gamepub.db.entity.Game;
import gamepub.db.entity.News;
import org.apache.commons.codec.digest.DigestUtils;
import org.omg.CORBA.DATA_CONVERSION;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by roman on 06.12.15.
 */
public class NewsDaoImplementation extends BaseDaoImplementation<News,Integer> implements NewsDao {
    public NewsDaoImplementation(){
        super(News.class);
    }

    public News getNewsById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<News> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        News result;
        try {
            result = (News)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public News getNewsByUid(String uid) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<News> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("uid"), uid));
        News result;
        try {
            result = (News)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public List<News> getNewsByName(String name, boolean all, Integer start, Integer count) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<News> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.like(root.<String>get("name"), "%" + name + "%"));
        cq.orderBy(cb.desc(root.<Date>get("date")));
        List result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        if (all)
            return result;
        else {
            List<News> resList = new ArrayList<News>();
            for(int i = 0; (i<count) && (result.size()>start+1); i++){
                resList.add((News)result.get(start+i));
            }
            return resList;
        }
    }

    public List<News> getNewsByGameId(Integer id, boolean all, Integer start, Integer count) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<News> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Game>get("game").<Integer>get("id"), id));
        cq.orderBy(cb.desc(root.<Date>get("date")));
        List result;
        if(all){
            result = getEntityManager().createQuery(cq).getResultList();
        }else{
            result = getEntityManager().createQuery(cq).setFirstResult(start).setMaxResults(count).getResultList();
        }
        closeEntityManager();
        return result;
    }

    public List<News> getNewsOrderByDate(boolean all, Integer start, Integer count) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<News> root = cq.from(instance);
        cq.select(root);
        cq.orderBy(cb.desc(root.<Date>get("date")));
        List result;
        if(all){
            result = getEntityManager().createQuery(cq).getResultList();
        }else{
            result = getEntityManager().createQuery(cq).setFirstResult(start).setMaxResults(count).getResultList();
        }
        closeEntityManager();
        return result;
    }

    public List<News> getNewsByDate(Date date, boolean all, Integer start, Integer count) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<News> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Date>get("date"), date));
        cq.orderBy(cb.desc(root.<Date>get("date")));
        List result;
        if(all){
            result = getEntityManager().createQuery(cq).getResultList();
        }else{
            result = getEntityManager().createQuery(cq).setFirstResult(start).setMaxResults(count).getResultList();
        }
        closeEntityManager();
        return result;
    }

    public List<News> getNewsByCustomParams(List<HashMap.Entry<String, Object>> parameterList, boolean all, Integer start, Integer count){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<News> root = cq.from(instance);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if(parameterList!=null && parameterList.size()>0) {
            for (HashMap.Entry<String, Object> entry : parameterList) {
                if (entry.getKey().equals("name")) {
                    predicates.add(cb.like(root.<String>get("name"), entry.getValue() + "%"));
                } else if (entry.getKey().equals("game")) {
                    predicates.add(cb.equal(root.<Game>get("game"), entry.getValue()));
                } else predicates.add(cb.lessThanOrEqualTo(root.<Date>get("date"), (Date) entry.getValue()));
            }
            Predicate[] p = new Predicate[predicates.size()];
            int i = 0;
            for(Predicate predicate:predicates){
                p[i] = predicate;
                i++;
            }
            cq.where(p);
        }
        cq.orderBy(cb.desc(root.<Date>get("date")));
        List result;
        if(all){
            result = getEntityManager().createQuery(cq).getResultList();
        }else{
            result = getEntityManager().createQuery(cq).setFirstResult(start).setMaxResults(count).getResultList();
        }
        closeEntityManager();
        return result;
    }
    @Override
    public News create(News news) {
        news.setUid(UUID.randomUUID().toString());
        return super.create(news);
    }
}
