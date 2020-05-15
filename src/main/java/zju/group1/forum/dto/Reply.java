package zju.group1.forum.dto;

import lombok.Data;

@Data
public class Reply {
    private int id;
    private int postId;//关联帖子数据库
    private String postName;
    private String author;//关联用户数据库外键
    private String content;
    private String replyTime;
    private int replyId;//被回复的楼层ID，普通回帖为0
    private int floorNumber;//前端显示的楼层号
    private int replyNumber;//前端显示的楼层号
    private boolean replyState;
}
