package fu.mr.expressmylove.domain;

/**
 * Created by Fu on 2016/10/3 13:52.
 */

public class User {

    public User() {

    }

    private String uid;

    private String avatar;   //头像

    private String nickname;  //昵称

    private String sex;     //性别

    private String personalize;  //个性签名

    private int authentication;   //实名认证  0表示未认证，1表示认证

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPersonalize() {
        return personalize;
    }

    public void setPersonalize(String personalize) {
        this.personalize = personalize;
    }

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", personalize='" + personalize + '\'' +
                ", authentication=" + authentication +
                '}';
    }
}
