package zju.group1.forum.dto;

import lombok.Data;

@Data
public class User {
    private String email;
    private String name;
    private String password;
    private String avatarUrl;
    private String token;
}
