package gamepub.beans;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import gamepub.db.entity.City;
import gamepub.db.entity.User;
import gamepub.db.entity.UserRole;
import gamepub.db.entity.UserScreenshot;
import gamepub.db.service.CityService;
import gamepub.db.service.NewsService;
import gamepub.db.service.UserRoleService;
import gamepub.db.service.UserService;
import gamepub.encode.shaCode;
import org.primefaces.model.UploadedFile;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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

import org.primefaces.context.RequestContext;

/**
 * Created by Fitok on 13.01.2016.
 */
@ManagedBean
@RequestScoped
public class RegistrBean {
    private User user;
    private City city;
    private String name, password, email;
    private int cityId;

    @EJB
    UserService userService;
    @EJB
    CityService cityService;
    @EJB
    UserRoleService userRoleService;

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


    public List<City> getCities() {
        return cityService.findAll();
    }

    public void save() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        user = new User();
        UserRole ur = userRoleService.getUserRoleById(1);
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
            userService.create(user);

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
}