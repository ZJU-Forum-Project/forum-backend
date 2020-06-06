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
    private Integer reputation;
    private String avatarUrl;

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

}
