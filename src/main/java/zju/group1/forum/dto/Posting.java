package zju.group1.forum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

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

}
