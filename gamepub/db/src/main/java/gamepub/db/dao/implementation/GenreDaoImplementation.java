package gamepub.db.dao.implementation;

import gamepub.db.dao.GenreDao;
import gamepub.db.entity.Genre;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;

/**
 * Created by roman on 06.12.15.
 */
public class GenreDaoImplementation extends BaseDaoImplementation<Genre,Integer> implements GenreDao {
    public GenreDaoImplementation(){
        super(Genre.class);
    }

    public Genre getGenreByName(String name) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Genre> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<String>get("name"), name));
        Genre result;
        try {
            result = (Genre)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {

        }
        closeEntityManager();
        return result;
    }

    public Genre getGenreById(Integer id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Genre> root = cq.from(instance);
        cq.select(root);
        cq.where(cb.equal(root.<Integer>get("id"), id));
        Genre result;
        try {
            result = (Genre)getEntityManager().createQuery(cq).getSingleResult();
        }catch (NoResultException e){
            result = null;
        }finally {
            closeEntityManager();
        }
        return result;
    }
}
