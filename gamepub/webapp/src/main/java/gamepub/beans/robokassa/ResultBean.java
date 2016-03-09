package gamepub.beans.robokassa;

import gamepub.beans.SessionBean;
import gamepub.db.entity.User;
import gamepub.db.entity.UserTransaction;
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
 * Created by Анатолий on 24.02.2016.
 */
@ManagedBean
@RequestScoped
@Stateful
public class ResultBean {
    @EJB
    UserService userService;

    @EJB
    UserTransactionService userTransactionService;
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
        } else if (userTransaction.getDescription().equals("gold"))
            user.setGold(true);


    }

    public String getResult() {

        return "result";
    }
}
