package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import zju.group1.forum.dto.PersonalMessage;
import zju.group1.forum.dto.ReplyMessage;
import zju.group1.forum.mapper.RepliesMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.Reply;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Api(tags = "查看回复")
@RestController
public class PeresonalReplyController {
    @Resource
    private RepliesMapper repliesMapper;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisProvider redisProvider;

    @ApiOperation("查看个人收到回复")
    @PostMapping(value = "/CheckReply")
    @AuthToken
    public PersonalMessage CheckReply(@RequestParam("Authorization") String token) throws IOException {
         PersonalMessage message = new PersonalMessage();
        if (token == null) {
            message.setState(false);
            message.setMessage("请重新登录");
            message.setAuthorizeToken(token);
            return message;
        }
        String email = redisProvider.getAuthorizedName(token);
        if (email == null) {
            message.setState(false);
            message.setMessage("无当前用户，请重新注册/登录");
            message.setAuthorizeToken(token);
            return message;
        }
        String name = userMapper.searchName(email);
        List<Reply> ReplyList = ReplyMapper.CheckReply(name);
        message.setReplies(ReplyList);
        message.setMessage("获取个人所有回复信息成功");
        return message;
    }

    @ApiOperation("修改未读状态")
    @PostMapping(value = "/seenReply")
    @AuthToken
    public ReplyMessage SeenReply(@PathVariable("id") String id) {
        ReplyMessage message = new Message();

        /*Reply reply = ReplyMapper.getReplyByID(id);
       reply.setState(true); 这两行也许不需要?*/
        ReplyMapper.SeenReply(id);
        message.setreplyState(true);
        message.setstate(true);
        message.setMessage("修改状态成功");
        return message;
    }
    @ApiOperation("获得未阅读回复数量")
    @PostMapping(value = "/getUnreadReplyNumber")
    @AuthToken
    public PersonalMessage getUnreadReplyNumber(@RequestParam("Authorization") String token) throws IOException {
         PersonalMessage message = new PersonalMessage();
        if (token == null) {
            message.setState(false);
            message.setMessage("请重新登录");
            message.setAuthorizeToken(token);
            return message;
        }
        String email = redisProvider.getAuthorizedName(token);
        if (email == null) {
            message.setState(false);
            message.setMessage("无当前用户，请重新注册/登录");
            message.setAuthorizeToken(token);
            return message;
        }
        String name = userMapper.searchName(email);
       int num1 = ReplyMapper.getUnreadReplyNumber(name);
        message.setNum(num1);
        message.setMessage("获取未阅读回复数量成功");
        return message;
    }
}
