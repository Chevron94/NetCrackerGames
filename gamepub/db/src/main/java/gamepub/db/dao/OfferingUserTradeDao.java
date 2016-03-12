/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.dao;

import gamepub.db.entity.OfferingUserTrade;
import java.util.List;

/**
 *
 * @author fitok
 */
public interface OfferingUserTradeDao extends BaseDao<OfferingUserTrade, Integer>{
    public OfferingUserTrade getOfferingUserTradeById(Integer id);
    public List<OfferingUserTrade> getOfferingUserTradesByTradeId(Integer id);
    public List<OfferingUserTrade> getOfferingUserTradesByOfferingGameId(Integer id);
    
    
}
