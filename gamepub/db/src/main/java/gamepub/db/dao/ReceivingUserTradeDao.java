/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.db.dao;

import gamepub.db.entity.ReceivingUserTrade;
import java.util.List;

/**
 *
 * @author fitok
 */
public interface ReceivingUserTradeDao extends BaseDao<ReceivingUserTrade,Integer>{
    public ReceivingUserTrade getReceivingUserTradeById(Integer id);
    public List<ReceivingUserTrade> getReceivingUserTradesByTradeId(Integer id);
    public List<ReceivingUserTrade> getReceivingUserTradesByReceivingGameId(Integer id);
}
