package zju.group1.forum.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import zju.group1.forum.dto.Message;


//先把接口列出来，后续再实现


@Api(tags = "禁言相关操作")
@RestController
public class BanningController {
    @ApiOperation("执行禁言")
    public Message banUser(@RequestParam("Authorization") String token,
                           @RequestParam("author") String author,
                           @RequestParam("banningDays") int banningDays
    )
    {
        Message message = new Message();


        return message;
    }


    @ApiOperation("取消禁言")
    public Message freeUser(@RequestParam("Authorization") String token,
                           @RequestParam("author") String author
    )
    {
        Message message = new Message();


        return message;
    }


    @ApiOperation("查询是否为禁言状态")
    public Message checkIfBanned(@RequestParam("Authorization") String token,
                                @RequestParam("author") String author
    )
    {
        Message message = new Message();


        return message;
    }



}