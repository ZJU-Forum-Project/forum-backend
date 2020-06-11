package zju.group1.forum.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchMessage {
    Boolean state;
    List<SearchUser> userList;
    List<Posting> postingList;
}
