package gamepub.beans.robokassa;

import gamepub.beans.SessionBean;
import gamepub.db.entity.Friend;
import gamepub.db.entity.Trade;
import gamepub.db.entity.User;
import gamepub.db.entity.UserTransaction;
import gamepub.db.service.FriendService;
import gamepub.db.service.TradeService;
import gamepub.db.service.UserService;
import gamepub.db.service.UserTransactionService;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.Map;

/**
 * Created by �������� on 24.02.2016.
 */
@ManagedBean
@RequestScoped
@Stateful
public class ResultBean {
    @EJB
    UserService userService;
    @EJB
    TradeService tradeService;
    @EJB
    UserTransactionService userTransactionService;
    @EJB
    FriendService friendService;
    private String mrhPass2 = "NetCracker1";

    //@ManagedProperty(value="#{param.OutSum}")
    String outSum;

    //@ManagedProperty(value="#{param.InvId}")
    String invId;

    //@ManagedProperty(value="#{param.SignatureValue}")
    String signatureValue;

    @PostConstruct
    public void initMyBean() {

        Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        outSum = requestParams.get("OutSum");
        invId = requestParams.get("InvId");
        UserTransaction userTransaction = userTransactionService.getTransactionById(Integer.parseInt(invId));
        if(!userTransaction.getStatus()) {
            signatureValue = requestParams.get("SignatureValue");

            String md5Hex = DigestUtils.md5Hex(
                    outSum + ":" + invId + ":" + mrhPass2
            ).toUpperCase();
            System.out.println(outSum + " " + invId + " " + signatureValue + " " + md5Hex);
            if (signatureValue.equals(md5Hex)) {
                userTransaction.setStatus(true);
                userTransactionService.update(userTransaction);
                endTransaction(userTransaction);
            }
        }
    }

    private void endTransaction(UserTransaction userTransaction) {
        User user = userTransaction.getUser();
        if (userTransaction.getDescription().equals("fine")) {
            user.setFine(user.getFine() - userTransaction.getOutSumm());
        } else if (userTransaction.getDescription().equals("gold")) {
            user.setTradesLeft(user.getTradesLeft()+10);
            user.setGold(true);
            userService.update(user);
        }
        
        if(userTransaction.getDescription().contains("trade")){
           Integer tradeId = Integer.parseInt(userTransaction.getDescription().substring(5));                      
           Trade tr = tradeService.getTradeById(tradeId);
           if(tr.getOfferingUser().getId()==userTransaction.getUser().getId()){
           tr.setOfferingUserPay(Boolean.TRUE);
           }
           if(tr.getReceivingUser().getId()==userTransaction.getUser().getId()){
           tr.setReceivingUserPay(Boolean.TRUE);
           }
        }
        if(userTransaction.getDescription().contains("unblock")){
            Integer userId = Integer.parseInt(userTransaction.getDescription().substring(7));
            Friend friend = friendService.getFriendBySubIdToId(userId,user.getId());
            friend.setBlock(false);
            friendService.update(friend);
        }
        if(userTransaction.getDescription().equals("unban")){
            user.setBanned(false);
            userService.update(user);
        }
        if(userTransaction.getDescription().equals("more")){
            user.setTradesLeft(user.getTradesLeft()+5);
            userService.update(user);
        }
        

    }

    public String getResult() {

        return "result";
    }
}
