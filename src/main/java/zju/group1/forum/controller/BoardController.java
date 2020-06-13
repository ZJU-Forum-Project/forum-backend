package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.BoardMessage;
import zju.group1.forum.dto.BoardPosting;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.Posting;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.BoardMapper;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "板块信息")
@RestController
@RequestMapping("/board")
public class BoardController {
    @Resource
    private PostingsMapper postingsMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private BoardMapper boardMapper;

    @Autowired
    RedisProvider redisProvider;

    @ApiOperation("情感板块")
    @PostMapping(value = "/emotion")
    @AuthToken
    public BoardMessage emotionBoard() {
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listEmotion();
        List<BoardPosting> boardPostingList = transform(postingList);
        message.setState(true);
        String intro = boardMapper.getIntro(1);
        message.setIntro(intro);
        message.setPostings(boardPostingList);
        message.setMessage("获取情感板块帖子成功");
        return message;
    }


    @ApiOperation("校园生活板块")
    @PostMapping(value = "/information")
    @AuthToken
    public BoardMessage informationBoard() {
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listInformation();
        List<BoardPosting> boardPostingList = transform(postingList);
        message.setState(true);
        String intro = boardMapper.getIntro(2);
        message.setIntro(intro);
        message.setPostings(boardPostingList);
        message.setMessage("获取校园信息板块帖子成功");
        return message;
    }

    @ApiOperation("实习信息板块")
    @PostMapping(value = "/intern")
    @AuthToken
    public BoardMessage interBoard() {
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listIntern();
        List<BoardPosting> boardPostingList = transform(postingList);
        message.setState(true);
        String intro = boardMapper.getIntro(3);
        message.setIntro(intro);
        message.setPostings(boardPostingList);
        message.setMessage("获取实习信息板块帖子成功");
        return message;
    }

    @ApiOperation("学习板块")
    @PostMapping(value = "/study")
    @AuthToken
    public BoardMessage studyBoard() {
        BoardMessage message = new BoardMessage();

        List<Posting> postingList = postingsMapper.listStudy();
        List<BoardPosting> boardPostingList = transform(postingList);
        message.setState(true);
        String intro = boardMapper.getIntro(4);
        message.setIntro(intro);
        message.setPostings(boardPostingList);
        message.setMessage("获取学习板块帖子成功");
        return message;
    }

    @ApiOperation("修改版面简介")
    @PostMapping(value = "/boardmodify")
    @AuthToken
    public Message boardModify(@RequestParam("Authorization") String token,
                               @RequestParam("boardId") Integer boardId,
                               @RequestParam("introduction") String introduction) {
        Message message = new Message();

        String email = redisProvider.getAuthorizedName(token);

        String admin = userMapper.isAdmin(email);
        if (admin.equals("1")) {
            boardMapper.updateIntro(boardId, introduction);
            message.setState(true);
            message.setMessage("修改成功");
            message.setAuthorizeToken(token);
            return message;
        } else {
            message.setState(false);
            message.setMessage("你没有该权限");
            message.setAuthorizeToken(token);
            return message;
        }
    }

    private List<BoardPosting> transform(List<Posting> postingList) {
        int n = postingList.size();
        List<BoardPosting> boardPostingList = new ArrayList<>();
        BoardPosting boardPosting;
        for(int i=0;i<n;i++){
            Posting posting = postingList.get(i);
            String avatarUrl = userMapper.getAvatarUrlByName(posting.getAuthor());
            boardPosting = new BoardPosting(
                    posting.getId(),
                    posting.getAuthor(),
                    posting.getTitle(),
                    posting.getType(),
                    posting.getContent(),
                    posting.getTime(),
                    posting.getReplyN(),
                    posting.getVisitN()
            );
            boardPosting.setAvatarUrl(avatarUrl);
            boardPostingList.add(boardPosting);
        }
        return boardPostingList;
    }
}
