package zju.group1.forum.dto;

import lombok.Data;

@Data
public class BoardPosting {
    private int id;
    private String author;
    private String title;
    private int type;
    private String content;
    private String time;
    private int replyN;
    private int visitN;
    private String avatarUrl;

    public BoardPosting(int id,
                        String author,
                        String title,
                        int type,
                        String content,
                        String time,
                        int replyN,
                        int visitN){
        this.id=id;
        this.author=author;
        this.title=title;
        this.type=type;
        this.content=content;
        this.time=time;
        this.replyN=replyN;
        this.visitN=visitN;
    }
}
