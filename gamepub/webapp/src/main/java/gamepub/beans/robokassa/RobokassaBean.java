package gamepub.beans.robokassa;

import gamepub.beans.SessionBean;
import gamepub.db.entity.UserTransaction;
import gamepub.db.service.UserService;
import gamepub.db.service.UserTransactionService;
import org.apache.commons.codec.digest.DigestUtils;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.rating.Rating;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Date;

/**
 * Created by Анатолий on 24.02.2016.
 */
@ManagedBean
@SessionScoped
@Stateful
public class RobokassaBean {
    @EJB
    UserService userService;
    @EJB
    UserTransactionService userTransactionService;

    private String mrhLogin ="Gamepub";
    private String mrhPass1 = "netcracker1";
    private int invId ;
    private String invDesc = "Payment";
    private String outSumm;
    private boolean isOutPay = false;

    public Boolean getIsOutPay(){
        return isOutPay;
    }
    public String getOutSumm(){
        return outSumm;
    }

    /*public void setOutSumm(String outSumm){
        this.outSumm = outSumm;
    }*/

    public String getInvId(){
        invId = userTransactionService.getMaxId();
        return Integer.toString(invId);
    }

    public String getInvDesc(){
        return invDesc;
    }

    public String getMrhLogin(){
        return mrhLogin;
    }
    public String getSignature(){

        String md5Hex = DigestUtils.md5Hex(
                mrhLogin+":"+outSumm+":"+invId+":" + mrhPass1
        );
        return md5Hex;
    }

    public String submit(){
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot uiViewRoot = context.getViewRoot();
        InputText inputText = (InputText) uiViewRoot.findComponent("outSummform:inputSumm");
        outSumm = (String)inputText.getValue();
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setDate(new Date());
        userTransaction.setOutSumm(Integer.parseInt(outSumm));
        userTransaction.setStatus(false);
        userTransaction.setUser(userService.getUserById(SessionBean.getUserId()));
        userTransactionService.create(userTransaction);
        isOutPay = true;
        return "robokassa";
    }
}
