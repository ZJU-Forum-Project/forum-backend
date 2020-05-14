package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.Posting;
import zju.group1.forum.dto.Reply;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.ReplyMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Date;

@Api(tags = "发送、修改和删除帖")
@RestController
public class PostContoller {
    @Resource
    private PostingsMapper postingsMapper;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisProvider redisProvider;
    @Resource
    private ReplyMapper replyMapper;

    @ApiOperation("发送帖子")
    @PostMapping(value = "/post")
    @AuthToken
    public Message emotionBoard(@RequestParam("Authorization") String token,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @RequestParam("type") int type){
        Message message = new Message();
        if (token == null) {
            message.setState(false);
            message.setMessage("请重新登录");
            message.setAuthorizeToken(token);
            return message;
        }
        if (title == null) {
            message.setState(false);
            message.setMessage("标题不能为空");
            message.setAuthorizeToken(token);
            return message;
        }
        if (content == null) {
            message.setState(false);
            message.setMessage("内容不能为空");
            message.setAuthorizeToken(token);
            return message;
        }

        Posting newPosting = new Posting();
        String email = redisProvider.getAuthorizedName(token);
        String name = userMapper.searchName(email);
        newPosting.setAuthor(name);
        newPosting.setTitle(title);
        newPosting.setContent(content);
        newPosting.setType(type);
        postingsMapper.Post(newPosting);

        int postId=postingsMapper.getLastPostId();
        Reply newReply = new Reply();
        postingsMapper.updateReplyNumber(postId);//更新帖子的回复数
        int floorNumber=postingsMapper.searchReplyNById(postId);//查找帖子回复数作为楼层号
        newReply.setPostId(postId);
        newReply.setFloorNumber(floorNumber);
        newReply.setAuthor(name);
        newReply.setContent(content);
        newReply.setReplyId(0);//表示被回复的楼层楼层号，普通回帖赋值为0
        newReply.setReplyNumber(floorNumber);//表示被回复的楼层前端的楼层号，普通回帖赋值为0
        replyMapper.reply(newReply);
        message.setState(true);
        message.setMessage("发送帖子成功");
        return message;
    }
    @ApiOperation("修改帖子")
    @PostMapping(value = "/modifyposting")
    @AuthToken
    public Message modifyPosting(@RequestParam("postingID") int postingID,
                                 @RequestParam("title") String title,
                                 @RequestParam("content") String content){
        Message message = new Message();
        if (title == null) {
            message.setState(false);
            message.setMessage("标题不能为空");
            return message;
        }
        if (content == null) {
            message.setState(false);
            message.setMessage("内容不能为空");
            return message;
        }

        Posting newPosting = new Posting();
        newPosting.setId(postingID);
        newPosting.setTitle(title);
        newPosting.setContent(content);
        postingsMapper.modifyPosting(newPosting);
        message.setState(true);
        message.setMessage("修改帖子成功");
        return message;
    }
    @ApiOperation("删除帖子")
    @PostMapping(value = "/deleteposting")
    @AuthToken
    public Message deletePosting(@RequestParam("postingID") int postingID){
        Message message = new Message();

        postingsMapper.deletePosting(postingID);
        message.setState(true);
        message.setMessage("删除帖子成功");
        return message;
    }
}
