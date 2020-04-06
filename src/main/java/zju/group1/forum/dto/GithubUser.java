package zju.group1.forum.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String login;
    private Long id;
    private String bio;
    private String avatar_url;
    private String token;
    private String gmtCreate;
    private String gmtModified;
}
