package gamepub.beans;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import gamepub.db.entity.*;
import gamepub.db.service.*;
import gamepub.encode.shaCode;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

/**
 * Created by Fitok on 13.01.2016.
 */
@ManagedBean
@RequestScoped
public class RegistrBean {
    private User user;
    private City city;
    private String name, password, email, countryName, cityName;
    private int cityId;

    @EJB
    UserService userService;
    @EJB
    CityService cityService;
    @EJB
    UserRoleService userRoleService;
    @EJB
    CountryService countryService;

    public String getName() {
        return name;
    }

    public void setName(String uname) {
        name = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String upass) {
        password = upass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String uemail) {
        email = uemail;
    }

    public int getCity() {
        return cityId;
    }

    public void setCity(int ucityId) {
        cityId = ucityId;
    }


    public List<City> getCities(String name) {
        Country country = countryService.getCountryByName("Russia");
        return cityService.getCitiesByNameAndCountryId(country.getId(), name);
    }

    public void save() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        user = new User();
        UserRole ur = userRoleService.getUserRoleById(1);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        cityId = (Integer)facesContext.getExternalContext().getSessionMap().get("registrCity");
        city = cityService.getCityById(cityId);


        if (userService.getUserByLogin(name) == null) {
            user.setAvatarUrl("http://dializa.md/wp-content/uploads/2015/06/no-avatar-ff.png");

            user.setPassword(shaCode.code(shaCode.code(name) + password));
            user.setEmail(email);
            user.setLogin(name);
            user.setCity(city);
            user.setUserRole(ur);
            user.setActive(true);
            user.setBanned(false);
            user.setFine(0);
            user.setGold(false);
            user.setReputation(0);
            userService.create(user);
            loginIn();
            FacesMessage regMes = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Success",
                    "Welcome " + name + "! Login now.");
            RequestContext.getCurrentInstance().showMessageInDialog(regMes);
        } else {
            FacesMessage failMes = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Error",
                    "User " + name + " already exists!Try another name.");
            RequestContext.getCurrentInstance().showMessageInDialog(failMes);
        }

    }

    public void loginIn() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String hashPass = shaCode.code(shaCode.code(name) + password);
        HttpSession ses = SessionBean.getSession();

        user = userService.getUserByLoginAndPassword(name, hashPass);
        ses.setAttribute("userid", user.getId());
        ses.setAttribute("username", getName());

    }

    public String getCountryName() {
        return countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String name) {
        cityName = name;
    }

    public void onItemSelect(String str) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getSessionMap().put("registrCity", cityService.getCityByName(cityName).getId());

    }

}