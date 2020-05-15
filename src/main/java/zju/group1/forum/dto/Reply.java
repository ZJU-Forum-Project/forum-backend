package zju.group1.forum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class Reply {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getReplyNumber() {
        return replyNumber;
    }

    public void setReplyNumber(int replyNumber) {
        this.replyNumber = replyNumber;
    }

    public boolean getReplyState() {
        return replyState;
    }

    public void setReplyState(boolean replyState) {
        this.replyState = replyState;
    }

    private int postId;//关联帖子数据库
    private String author;//关联用户数据库外键
    private String content;
    private String replyTime;
    private int replyId;//被回复的楼层ID，普通回帖为0
    private int floorNumber;//前端显示的楼层号
    private int replyNumber;//前端显示的楼层号
    private boolean replyState;
}
