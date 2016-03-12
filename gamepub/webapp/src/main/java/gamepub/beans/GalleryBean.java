/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.beans;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import gamepub.cloud.cloudUpload;
import gamepub.db.entity.User;
import gamepub.db.entity.UserScreenshot;
import gamepub.db.service.UserScreenshotService;
import gamepub.db.service.UserService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author fitok
 */
@ManagedBean
@RequestScoped
public class GalleryBean {
   
   @EJB
    UserService userService; 
   @EJB
   UserScreenshotService userScreenshotService;
   
   @ManagedProperty(value = "#{param.userId}")
    private String userId;

private List<UserScreenshot> userScreens;   
private List<String> images;
private UploadedFile file;
private Map uploadResult;
private boolean noScreensForUser;

public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
     
    public void upload(FileUploadEvent event) throws IOException {
       if(event.getFile()!= null) {  
      cloudUpload upload = new cloudUpload(event.getFile());                          
                 UserScreenshot uScreen = new UserScreenshot();
                    uScreen.setLink((String)upload.getUploadResult().get("url"));      
                    Date curDate = new Date();            
                    uScreen.setDate(curDate);   
                    uScreen.setUser(userService.getUserById(SessionBean.getUserId()));                     
                userScreenshotService.create(uScreen);
            FacesMessage message = new FacesMessage("Succesful", getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    } 
 
  
       
    
    
    /**
     * @return the images
     */
    public List<String> getImages() {
        images = new ArrayList<String>();  
        
        if(checkForMyGallery()){
        userScreens = userScreenshotService.getScreenshotsByUserId(SessionBean.getUserId());}
        if(!checkForMyGallery()){
            User user = userService.getUserByUid(userId);
        userScreens = userScreenshotService.getScreenshotsByUserId(user.getId());}
    if (userScreens.isEmpty()){
        images.add("/Template/404g.png");
    }else{                              
            for(UserScreenshot u:userScreens){
            images.add(u.getLink()); 
            }
    }
             
        return images;
    }
    
    public void deleteImage(){
        Map<String,String[]> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap(); 
        String[] selectedImage = params.get("selectedImage");        
        if(checkForMyGallery()){
        userScreens = userScreenshotService.getScreenshotsByUserId(SessionBean.getUserId());}
        if(!checkForMyGallery()){
           User user = userService.getUserByUid(userId);
        userScreens = userScreenshotService.getScreenshotsByUserId(user.getId());}        
            for (UserScreenshot us:userScreens){
                if(us.getLink().equals(selectedImage[0])) userScreenshotService.delete(us.getId());}
    }
    
    /**
     * @return the noScreensForUser
     */
    public boolean isNoScreensForUser() {
        return noScreensForUser;
    }

    /**
     * @param noScreensForUser the noScreensForUser to set
     */
    public void setNoScreensForUser(boolean noScreensForUser) {
        this.noScreensForUser = noScreensForUser;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Boolean checkForMyGallery(){        
        return userId.equals("my");
    }
    public String getGalleryOwner(){
        if (userId.equals("my")) {return "My gallery";}
        else{ User user = userService.getUserByUid(userId);
         return user.getLogin()+"'s gallery";                
    }
           
   }
    
}