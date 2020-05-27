package zju.group1.forum.dto;

import lombok.Data;

import java.util.List;

@Data
public class PictureMessage {
    private String message;
    private List<List<String>> replyList;
}
