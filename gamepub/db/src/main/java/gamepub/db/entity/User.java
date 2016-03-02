package gamepub.db.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by roman on 30.11.15.
 */
@Entity
@Table(name = "GAMEPUB_USER")
public class User {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name="UID",nullable = true)
    String uid;

    @Column(name = "LOGIN", nullable = false)
    String login;
    @Column(name = "PASSWORD", nullable = false)
    String password;
    @Column(name = "EMAIL", nullable = false)
    String email;
    @Column(name = "AVATAR_URL", nullable = false)
    String avatarUrl;
    @Column(name = "VK_INFO", nullable = true)
    String vkInfo;
    @Column(name = "STEAM_INFO", nullable = true)
    String steamInfo;
    @Column(name = "FB_INFO", nullable = true)
    String fbInfo;
    @Column(name = "TOKEN", nullable = true)
    String token;
    @Column(name = "ACTIVE")
    Boolean active;
    @Column(name = "BANNED")
    Boolean banned;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ROLE_ID", nullable = false)
    UserRole userRole;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY_ID", nullable = false)
    City city;
    
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    List<PrivateMessage> sendedPrivateMessages;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    List<PrivateMessage> receivedPrivateMessages;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscribedTo")
    List<Friend> subscribers;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscriber")
    List<Friend> subscribeTo;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<UserScreenshot> userScreenshots;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<Mark> marks;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<UserGame> userGames;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<Comment> comments;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offeringUser")
    List<Trade> offeringUserTrades;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receivingUser")
    List<Trade> receivingUserTrades;
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getVkInfo() {
        return vkInfo;
    }

    public void setVkInfo(String vkInfo) {
        this.vkInfo = vkInfo;
    }

    public String getSteamInfo() {
        return steamInfo;
    }

    public void setSteamInfo(String steamInfo) {
        this.steamInfo = steamInfo;
    }

    public String getFbInfo() {
        return fbInfo;
    }

    public void setFbInfo(String fbInfo) {
        this.fbInfo = fbInfo;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<PrivateMessage> getSendedPrivateMessages() {
        return sendedPrivateMessages;
    }

    public void setSendedPrivateMessages(List<PrivateMessage> sendedPrivateMessages) {
        this.sendedPrivateMessages = sendedPrivateMessages;
    }

    public List<PrivateMessage> getReceivedPrivateMessages() {
        return receivedPrivateMessages;
    }

    public void setReceivedPrivateMessages(List<PrivateMessage> receivedPrivateMessages) {
        this.receivedPrivateMessages = receivedPrivateMessages;
    }

    public List<Friend> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Friend> subscribers) {
        this.subscribers = subscribers;
    }

    public List<Friend> getSubscribeTo() {
        return subscribeTo;
    }

    public void setSubscribeTo(List<Friend> subscribeTo) {
        this.subscribeTo = subscribeTo;
    }

    public List<UserScreenshot> getUserScreenshots() {
        return userScreenshots;
    }

    public void setUserScreenshots(List<UserScreenshot> userScreenshots) {
        this.userScreenshots = userScreenshots;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public List<UserGame> getUserGames() {
        return userGames;
    }

    public void setUserGames(List<UserGame> userGames) {
        this.userGames = userGames;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return id == user.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", vkInfo='" + vkInfo + '\'' +
                ", steamInfo='" + steamInfo + '\'' +
                ", fbInfo='" + fbInfo + '\'' +
                ", userRole=" + userRole +
                ", city=" + city +
                '}';
    }

    /**
     * @return the offeringUserTrades
     */
    public List<Trade> getOfferingUserTrades() {
        return offeringUserTrades;
    }

    /**
     * @param offeringUserTrades the offeringUserTrades to set
     */
    public void setOfferingUserTrades(List<Trade> offeringUserTrades) {
        this.offeringUserTrades = offeringUserTrades;
    }

    /**
     * @return the receivingUserTrades
     */
    public List<Trade> getReceivingUserTrades() {
        return receivingUserTrades;
    }

    /**
     * @param receivingUserTrades the receivingUserTrades to set
     */
    public void setReceivingUserTrades(List<Trade> receivingUserTrades) {
        this.receivingUserTrades = receivingUserTrades;
    }


    

    

    

   
}
