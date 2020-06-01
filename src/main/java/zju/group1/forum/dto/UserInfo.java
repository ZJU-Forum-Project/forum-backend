package zju.group1.forum.dto;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class UserInfo {
    //varchar(50)
    private String real_name;
    private int real_name_hidden;
    //做成下拉，最后说明传回的类型
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private int birth_hidden;
    //varchar(50) PK
    private String email;
    private int eamil_hidden;
    //varchar(50)
    private String phone;
    private int phone_hidden;
    //varchar(50)
    private String gender;
    private int gender_hidden;
    //varchar(50)
    private String hometown;
    private int hometown_hidden;
    //varchar(50)
    private String organization;
    private int organization_hidden;
    //varchar(200)
    private String signature;
    private Integer reputation;
}
