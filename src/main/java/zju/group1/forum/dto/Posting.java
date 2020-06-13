package zju.group1.forum.dto;

import lombok.Data;

@Data
public class Posting {
    private int id;
    private String author;
    private String title;
    private int type;
    private String content;
    private String time;
    private int replyN;
    private int visitN;
    private String avatarUrl;
}
