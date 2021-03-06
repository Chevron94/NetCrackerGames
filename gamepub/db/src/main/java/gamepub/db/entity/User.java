package gamepub.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
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
    @Column(name = "GOOGLE_INFO", nullable = true)
    String googleInfo;
    @Column(name = "FB_INFO", nullable = true)
    String fbInfo;
    @Column(name = "TOKEN", nullable = true)
    String token;
    @Column(name = "ACTIVE")
    Boolean active;
    @Column(name = "BANNED")
    Boolean banned;

    @Column(name = "FINE", columnDefinition = "int default 0")
    Integer fine;

    @Column(name = "REPUTATION",columnDefinition = "int default 0")
    Integer reputation;
    @Column(name = "TRADES_LEFT",columnDefinition = "int default 3")
    Integer tradesLeft;

    @Column(name = "GOLD",columnDefinition = "boolean default false")
    Boolean gold;

    @Column(name = "API_TOKEN", nullable = true)
    String apiToken;
    @Column(name = "USED_REQUESTS")
    Integer usedRequest;
    @Column(name = "TOKEN_EXPIRE_DATE")
    Date expireDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ROLE_ID", nullable = false)
    UserRole userRole;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY_ID", nullable = false)
    City city;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    List<PrivateMessage> sentPrivateMessages;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    List<PrivateMessage> receivedPrivateMessages;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscribedTo")
    List<Friend> subscribers;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subscriber")
    List<Friend> subscribeTo;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<UserScreenshot> userScreenshots;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<Mark> marks;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<UserGame> userGames;
    @JsonIgnore
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
    
    public String getGoogleInfo() {
        return googleInfo;
    }

    public void setGoogleInfo(String googleInfo) {
        this.googleInfo = googleInfo;
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

    public List<PrivateMessage> getSentPrivateMessages() {
        return sentPrivateMessages;
    }

    public void setSentPrivateMessages(List<PrivateMessage> sentPrivateMessages) {
        this.sentPrivateMessages = sentPrivateMessages;
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


    public int getFine(){
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public Integer getUsedRequest() {
        return usedRequest;
    }

    public void setUsedRequest(Integer usedRequest) {
        this.usedRequest = usedRequest;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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
                ", googleInfo='" + googleInfo + '\'' +
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

    /**
     * @return the reputation
     */
    public Integer getReputation() {
        return reputation;
    }

    /**
     * @param reputation the reputation to set
     */
    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    /**
     * @return the gold
     */
    public Boolean getGold() {
        return gold;
    }

    /**
     * @param gold the gold to set
     */
    public void setGold(Boolean gold) {
        this.gold = gold;
    }

    /**
     * @return the tradesLeft
     */
    public Integer getTradesLeft() {
        return tradesLeft;
    }

    /**
     * @param tradesLeft the tradesLeft to set
     */
    public void setTradesLeft(Integer tradesLeft) {
        this.tradesLeft = tradesLeft;
    }

   


    

    

    

   
}
