package gamepub.db.dao;

import gamepub.db.entity.Country;

/**
 * Created by roman on 05.12.15.
 */
public interface CountryDao extends BaseDao<Country,Integer> {
    public Country getCountryById(Integer id);
    Country getCountryByName(String name);
}
