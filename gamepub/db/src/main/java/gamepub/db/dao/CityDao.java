package gamepub.db.dao;

import gamepub.db.entity.City;

import java.util.List;

/**
 * Created by roman on 05.12.15.
 */
public interface CityDao extends BaseDao<City,Integer>{
    public City getCityById(Integer id);
    public List<City> getCitiesById(Integer id);
    public  List<City> getCitiesByNameAndCountryId(Integer id, String name);
    City getCityByName(String name);
}
