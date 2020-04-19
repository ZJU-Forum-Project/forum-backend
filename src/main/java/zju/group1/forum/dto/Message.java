package zju.group1.forum.dto;

import lombok.Data;

@Data
public class Message {
    private boolean state;
    private String message;
    private String authorizeToken;
}
