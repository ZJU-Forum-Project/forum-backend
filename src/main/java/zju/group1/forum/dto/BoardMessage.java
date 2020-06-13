package zju.group1.forum.dto;

import lombok.Data;

import java.util.List;
@Data
public class BoardMessage {
    private boolean state;
    private String message;
    private String authorizeToken;
    private String intro;
    private List<Posting> postings;
}
