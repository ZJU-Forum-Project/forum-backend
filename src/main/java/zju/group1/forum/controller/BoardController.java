package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.BoardMessage;
import zju.group1.forum.dto.Posting;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "板块信息")
@RestController
@RequestMapping("/board")
public class BoardController {
    @Resource
    private PostingsMapper postingsMapper;

    @ApiOperation("学习板块")
    @PostMapping(value = "/study")
    @AuthToken
    public BoardMessage studyBoard(){
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listStudy();
        message.setState(true);
        message.setPostings(postingList);
        message.setMessage("获取学习板块帖子成功");
        return message;
    }

    @ApiOperation("情感板块")
    @PostMapping(value = "/emotion")
    @AuthToken
    public BoardMessage emotionBoard(){
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listEmotion();
        message.setState(true);
        message.setPostings(postingList);
        message.setMessage("获取情感板块帖子成功");
        return message;
    }

    @ApiOperation("校园信息板块")
    @PostMapping(value = "/information")
    @AuthToken
    public BoardMessage informationBoard(){
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listInformation();
        message.setState(true);
        message.setPostings(postingList);
        message.setMessage("获取校园信息板块帖子成功");
        return message;
    }

    @ApiOperation("实习信息板块")
    @PostMapping(value = "/intern")
    @AuthToken
    public BoardMessage interBoard(){
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listIntern();
        message.setState(true);
        message.setPostings(postingList);
        message.setMessage("获取实习信息板块帖子成功");
        return message;
    }

}
