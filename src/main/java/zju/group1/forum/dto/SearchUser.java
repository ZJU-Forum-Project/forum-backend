package zju.group1.forum.dto;

import lombok.Data;

@Data
public class SearchUser {
    String name;
    String avatarUrl;

    public SearchUser(String name,String avatarUrl){
        this.name=name;
        this.avatarUrl=avatarUrl;
    }
}
