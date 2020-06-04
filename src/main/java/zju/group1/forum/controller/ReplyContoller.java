package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.Reply;
import zju.group1.forum.dto.ReplyMessage;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.ReplyMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import java.io.IOException;


@Api(tags = "查看、回复、修改和删除楼层")
@RestController
public class ReplyContoller {
    @Resource
    private ReplyMapper replyMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PostingsMapper postingsMapper;
    @Autowired
    private RedisProvider redisProvider;

    @ApiOperation("查看编号“floorId”的帖子")
    @PostMapping(value = "/GetFloor")
    @AuthToken
    public ReplyMessage checkReply(@RequestParam("floorId") int floorId) throws IOException {
        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setState(true);
        replyMessage.setMessage("查看该楼层成功！");
        Reply reply=replyMapper.getReplyByID(floorId);
        replyMessage.setReplyInfo(reply);
        return replyMessage;
    }

    @ApiOperation("回复帖子")
    @PostMapping(value = "/ReplyPosting")
    @AuthToken
    public Message replyPosting(@RequestParam("Authorization") String token,
                                @RequestParam("postId") int postId,
                                @RequestParam("author") String author,
                                @RequestParam("content") String content){
        Message message = new Message();
        if (token == null) {
            message.setState(false);
            message.setMessage("请重新登录");
            message.setAuthorizeToken(token);
            return message;
        }
        if (author == null) {
            message.setState(false);
            message.setMessage("用户不能为空");
            message.setAuthorizeToken(token);
            return message;
        }
        if (content == null) {
            message.setState(false);
            message.setMessage("内容不能为空");
            message.setAuthorizeToken(token);
            return message;
        }
        Reply newReply = new Reply();
        postingsMapper.updateReplyNumber(postId);//更新帖子的回复数
        int floorNumber=postingsMapper.searchReplyNById(postId);//查找帖子回复数作为楼层号
        newReply.setPostId(postId);
        newReply.setFloorNumber(floorNumber);
        newReply.setAuthor(author);
        newReply.setContent(content);
        newReply.setReplyId(0);//表示被回复的楼层楼层号，普通回帖赋值为0
        newReply.setReplyNumber(0);//表示被回复的楼层前端的楼层号，普通回帖赋值为0
        replyMapper.reply(newReply);
        message.setState(true);
        message.setMessage("回复帖子成功");
        return message;
    }

    @ApiOperation("回复楼层")
    @PostMapping(value = "/ReplyFloor")
    @AuthToken
    public Message replyFloor(@RequestParam("Authorization") String token,
                                @RequestParam("postId") int postId,
                                @RequestParam("author") String author,
                                @RequestParam("content") String content,
                                @RequestParam("floorId") int floorId//被回复的楼层号
    ){
        Message message = new Message();
        if (token == null) {
            message.setState(false);
            message.setMessage("请重新登录");
            message.setAuthorizeToken(token);
            return message;
        }
        if (author == null) {
            message.setState(false);
            message.setMessage("用户不能为空");
            message.setAuthorizeToken(token);
            return message;
        }
        if (content == null) {
            message.setState(false);
            message.setMessage("内容不能为空");
            message.setAuthorizeToken(token);
            return message;
        }

        Reply newReply = new Reply();
        postingsMapper.updateReplyNumber(postId);//更新帖子的回复数
        int floorNumber=postingsMapper.searchReplyNById(postId);//查找帖子回复数作为楼层号
        int replyNumber=replyMapper.getFloorNumberByID(floorId);//得到被回复的楼层的前端楼层号
        newReply.setPostId(postId);
        newReply.setFloorNumber(floorNumber);
        newReply.setAuthor(author);
        newReply.setContent(content);
        newReply.setReplyId(floorId);
        newReply.setReplyNumber(replyNumber);//注意存入前端楼层号
        replyMapper.reply(newReply);
        message.setState(true);
        message.setMessage("回复楼层成功");
        return message;
    }

    @ApiOperation("修改回复")
    @PostMapping(value = "/ModifyFloor")
    @AuthToken
    public Message modifyfloor(@RequestParam("Authorization") String token,
                               @RequestParam("floorId") int floorId,
                               @RequestParam("content") String content,
                               @RequestParam("replyId") int replyId){
        Message message = new Message();

        if (content == null) {
            message.setState(false);
            message.setMessage("内容不能为空");
            return message;
        }

        String email = redisProvider.getAuthorizedName(token);
        String name1 = userMapper.searchName(email);
        String name2 = replyMapper.getAuthorIdByID(floorId);
        boolean IsAuthor = name1.equals(name2);
        String admin = userMapper.isAdmin(email);
        boolean IsAdmin = admin.equals("1");

        if (IsAuthor || IsAdmin) {
            Reply newReply = new Reply();
            newReply.setId(floorId);
            newReply.setContent(content);
            newReply.setReplyId(replyId);//被回复的楼层ID，普通回帖为0
            replyMapper.modifyReply(newReply);
            message.setState(true);
            message.setMessage("修改回复成功");
            return message;
        } else {
            message.setState(false);
            message.setMessage("你没有该权限");
            message.setAuthorizeToken(token);
            return message;
        }
    }

    @ApiOperation("删除回复")
    @PostMapping(value = "/DeleteReply")
    @AuthToken
    public Message deleteReply(@RequestParam("Authorization") String token,
                               @RequestParam("floorId") int floorId){
        Message message = new Message();

        String email = redisProvider.getAuthorizedName(token);
        String name1 = userMapper.searchName(email);
        String name2 = replyMapper.getAuthorIdByID(floorId);
        boolean IsAuthor = name1.equals(name2);
        String admin = userMapper.isAdmin(email);
        boolean IsAdmin = admin.equals("1");

        if (IsAuthor || IsAdmin) {
            replyMapper.deleteReply(floorId);
            message.setState(true);
            message.setMessage("删除回复成功");
            return message;
        } else {
            message.setState(false);
            message.setMessage("你没有该权限");
            message.setAuthorizeToken(token);
            return message;
        }
    }
}