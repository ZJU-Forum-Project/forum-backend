package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.Message;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;


@Api(tags = "禁言相关操作")
@RestController
public class BanningController {

    @Autowired
    private RedisProvider redisProvider;
    @Resource
    private UserMapper userMapper;

    @ApiOperation("执行禁言")
    @PostMapping(value = "/banUser")
    @AuthToken
    public Message banUser(@RequestParam("Authorization") String token,
                           @RequestParam("author") String author,
                           @RequestParam("banningDays") int banningDays
    )
    {
        Message message = new Message();

        /*case1：未登录*/
        if (token == null) {
            message.setState(false);
            message.setMessage("请重新登录");
            message.setAuthorizeToken(token);
            return message;
        }


        /*case2:若用户已被禁言*/
        if(redisProvider.checkIfBanned(author)){
            message.setState(false);
            message.setMessage("该用户已被禁言");
            message.setAuthorizeToken(token);
            return message;
        }

        /*case3:正常禁言流程*/
        redisProvider.setBannedUser(author, banningDays);

        message.setState(true);
        message.setMessage("禁言成功");
        message.setAuthorizeToken(token);

        return message;
    }


    @ApiOperation("取消禁言")
    @PostMapping(value = "/freeUser")
    @AuthToken
    public Message freeUser(@RequestParam("Authorization") String token,
                           @RequestParam("author") String author
    )
    {
        Message message = new Message();

        /*case1：未登录*/
        if (token == null) {


            message.setState(false);
            message.setMessage("请重新登录");
            message.setAuthorizeToken(token);
            return message;
        }


        /*case2:若用户未被禁言*/
        if(redisProvider.checkIfBanned(author) == false){
            message.setState(false);
            message.setMessage("该用户未被禁言");
            message.setAuthorizeToken(token);
            return message;
        }

        /*case3:正常解禁流程*/
        redisProvider.freeBannedUser(author);

        message.setState(true);
        message.setMessage("解禁成功");
        message.setAuthorizeToken(token);

        return message;
    }


    @ApiOperation("查询指定author是否为禁言状态")
    @PostMapping(value = "/checkIfBannedByAuthor")
    @AuthToken
    public Message checkIfBannedByAuthor(@RequestParam("Authorization") String token,
                                        @RequestParam("author") String author
    )
    {
        Message message = new Message();


        if(redisProvider.checkIfBanned(author)){

            message.setState(true);
            message.setMessage("用户为禁言状态");
            message.setAuthorizeToken(token);
        }

        else{
            message.setState(false);
            message.setMessage("用户不在禁言状态");
            message.setAuthorizeToken(token);
        }


        return message;
    }

    @ApiOperation("查询指定token是否为禁言状态")
    @PostMapping(value = "/checkIfBannedByAuthorization")
    @AuthToken
    public Message checkIfBannedByAuthorization(@RequestParam("Authorization") String token)
    {
        Message message = new Message();

        String email = redisProvider.getAuthorizedName(token);

        String author = userMapper.searchName(email);

        if(redisProvider.checkIfBanned(author)){

            message.setState(true);
            message.setMessage("用户为禁言状态");
            message.setAuthorizeToken(token);
        }

        else{
            message.setState(false);
            message.setMessage("用户不在禁言状态");
            message.setAuthorizeToken(token);
        }


        return message;
    }
}