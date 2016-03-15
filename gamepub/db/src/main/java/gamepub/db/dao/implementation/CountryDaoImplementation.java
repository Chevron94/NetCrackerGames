package gamepub.db.dao.implementation;

import gamepub.db.dao.CountryDao;
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
public class CountryDaoImplementation extends BaseDaoImplementation<Country, Integer> implements CountryDao {

    public CountryDaoImplementation() {
        super(Country.class);
    }

    public Country getCountryById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Country> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        Country result;
        try {
            result = (Country)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }

    public Country getCountryByName(String name) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Country> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("name"), name));
        Country result;
        try {
            result = (Country)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }
}
