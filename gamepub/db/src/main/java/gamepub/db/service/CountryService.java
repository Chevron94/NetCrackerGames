package gamepub.db.service;

import gamepub.db.dao.implementation.CountryDaoImplementation;
import gamepub.db.entity.Country;

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
public class CountryService extends CountryDaoImplementation {
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
    public Country getCountryById(Integer id) {
        return super.getCountryById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Country getCountryByName(String name) {
        return super.getCountryByName(name);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Country create(Country country) {
        return super.create(country);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Country find(Integer id) {
        return super.find(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Country update(Country country) {
        return super.update(country);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Country> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(List<Country> t) {
        super.delete(t);
    }
}
