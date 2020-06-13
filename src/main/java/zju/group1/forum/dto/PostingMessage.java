package zju.group1.forum.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostingMessage {
    private boolean state;
    private String message;
    private String authorizeToken;
    private String author;
    private String title;
    private String content;
    private String time;
    private int replyN;
    private int visitN;
    private List<ReplyMessage> replyList;

    public void setPostingInfo(Posting posting){
        setAuthor(posting.getAuthor());
        setTitle(posting.getTitle());
        setContent(posting.getContent());
        setTime(posting.getTime());
        setReplyN(posting.getReplyN());
        setVisitN(posting.getVisitN());
    }

}
