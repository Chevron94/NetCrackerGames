package gamepub.db.dao.implementation;

import gamepub.db.dao.CityDao;
import gamepub.db.entity.City;
import gamepub.db.entity.Country;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roman on 05.12.15.
 */
public class CityDaoImplementation extends BaseDaoImplementation<City,Integer> implements CityDao {
    public CityDaoImplementation()
    {
        super(City.class);
    }

    public City getCityById(Integer id) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<City> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"),id));
        City result;
        try {
            result = (City)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;

    }

    public List<City> getCitiesById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<City> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Country>get("country").<Integer>get("id"), id));
        cq.orderBy(cb.asc(root.<String>get("name")));
        List<City> result = getEntityManager().createQuery(cq).getResultList();
        closeEntityManager();
        return result;
    }

    public List<City> getCitiesByNameAndCountryId(Integer id, String name) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<City> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Country>get("country").<Integer>get("id"), id), cb.like(root.<String>get("name"),name+"%"));
        cq.orderBy(cb.asc(root.<String>get("name")));
        List<City> result = getEntityManager().createQuery(cq).setMaxResults(20).getResultList();
        closeEntityManager();
        return result;
    }
    public City getCityByName(String name) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<City> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("name"), name));
        City result;
        try {
            result = (City)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }
}
