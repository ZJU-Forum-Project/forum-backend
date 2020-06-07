package zju.group1.forum.dto;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class OtherUserInfoMessage {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    private String gender;

    private String hometown;

    private String organization;

    private String signature;

    private Integer reputation;

    private String avatarUrl;
}
