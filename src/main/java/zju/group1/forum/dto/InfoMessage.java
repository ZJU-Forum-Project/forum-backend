package zju.group1.forum.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class InfoMessage {
    private boolean state;
    private String message;
    private String authorizeToken;

    private String real_name;
    private int real_name_hidden;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private int birth_hidden;

    private String email;
    private int eamil_hidden;

    private String phone;
    private int phone_hidden;

    private String gender;
    private int gender_hidden;

    private String hometown;
    private int hometown_hidden;

    private String organization;
    private int organization_hidden;

    private String signature;

    public void setInfo(UserInfo info) {
        setBirth(info.getBirth());
        setBirth_hidden(info.getBirth_hidden());
        setEmail(info.getEmail());
        setEamil_hidden(info.getEamil_hidden());
        setGender(info.getGender());
        setGender_hidden(info.getGender_hidden());
        setHometown(info.getHometown());
        setHometown_hidden(info.getHometown_hidden());
        setOrganization(info.getOrganization());
        setOrganization_hidden(info.getOrganization_hidden());
        setReal_name(info.getReal_name());
        setReal_name_hidden(info.getReal_name_hidden());
        setPhone(info.getPhone());
        setPhone_hidden(info.getPhone_hidden());
        setSignature(info.getSignature());
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorizeToken() {
        return authorizeToken;
    }

    public void setAuthorizeToken(String authorizeToken) {
        this.authorizeToken = authorizeToken;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public int getReal_name_hidden() {
        return real_name_hidden;
    }

    public void setReal_name_hidden(int real_name_hidden) {
        this.real_name_hidden = real_name_hidden;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public int getBirth_hidden() {
        return birth_hidden;
    }

    public void setBirth_hidden(int birth_hidden) {
        this.birth_hidden = birth_hidden;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEamil_hidden() {
        return eamil_hidden;
    }

    public void setEamil_hidden(int eamil_hidden) {
        this.eamil_hidden = eamil_hidden;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhone_hidden() {
        return phone_hidden;
    }

    public void setPhone_hidden(int phone_hidden) {
        this.phone_hidden = phone_hidden;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGender_hidden() {
        return gender_hidden;
    }

    public void setGender_hidden(int gender_hidden) {
        this.gender_hidden = gender_hidden;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public int getHometown_hidden() {
        return hometown_hidden;
    }

    public void setHometown_hidden(int hometown_hidden) {
        this.hometown_hidden = hometown_hidden;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public int getOrganization_hidden() {
        return organization_hidden;
    }

    public void setOrganization_hidden(int organization_hidden) {
        this.organization_hidden = organization_hidden;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
