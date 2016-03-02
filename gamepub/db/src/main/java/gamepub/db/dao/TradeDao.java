package gamepub.db.dao;

import gamepub.db.entity.Trade;

import java.util.List;

public interface TradeDao extends BaseDao<Trade,Integer> {
    public Trade getTradeById(Integer id);
    public List<Trade> getActiveTrades();  
    public List<Trade> getTradesByOfferingUserId(Integer id);
    public Trade getLastTradeByOfferingUserId(Integer id);
    public List<Trade> getTradesByReceivingUserId(Integer id);                
}