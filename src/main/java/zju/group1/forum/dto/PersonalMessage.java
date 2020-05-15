package zju.group1.forum.dto;

import lombok.Data;

import java.util.List;
@Data
public class PersonalMessage {
    private boolean state;
    private String message;
    private String authorizeToken;
    private int num; //未阅读回复数量，前端传参
    private List<Reply> Replies;

    public void setNum(int num) {
        this.num = num;
    }

}
