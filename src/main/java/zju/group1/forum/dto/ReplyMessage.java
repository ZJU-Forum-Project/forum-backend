package zju.group1.forum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Data
public class ReplyMessage {
    private boolean state;
    private String message;
    private String authorizeToken;

    private String author;
    private boolean replyState;
    private String time;
    private String content;
    private int floorUI;//表示前端对应的楼层
    private int floorId;//表示数据库中的楼层ID,用于调用其他函数时传给后端
    private int replyUI;//表示被回复的楼层前端楼层号，普通回帖则为0
    private int replyId;//表示被回复楼层的ID，普通回帖则为0

    public void setReplyInfo(Reply reply){
        setAuthor(reply.getAuthor());
        setReplyState(reply.getReplyState());
        setTime(reply.getReplyTime());
        setContent(reply.getContent());
        setFloorId(reply.getId());
        setFloorUI(reply.getFloorNumber());
        setReplyId(reply.getReplyId());
        setReplyUI(reply.getReplyNumber());
        return;
    }
}