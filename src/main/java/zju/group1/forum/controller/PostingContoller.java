package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.*;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.ReplyMapper;
import zju.group1.forum.mapper.UserInfoMapper;
import zju.group1.forum.mapper.UserMapper;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "查看帖子")
@RestController
public class PostingContoller {
    @Resource
    private PostingsMapper postingsMapper;
    @Resource
    private ReplyMapper replyMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @ApiOperation("查看编号“postingID”的帖子")
    @PostMapping(value = "/postings/{postingID}")
    @AuthToken
    public PostingMessage checkPostings(@PathVariable("postingID") int postingID) {
        PostingMessage postingMessage = new PostingMessage();


        Posting posting = postingsMapper.getPostingByID(postingID);
        postingMessage.setState(true);
        postingMessage.setMessage("查看该贴成功！");
        postingMessage.setPostingInfo(posting);
        postingsMapper.updateVisitNumber(postingID);

        List<Reply> replyList=replyMapper.getReplyByPostID(postingID);
        List<ReplyMessage> replyMessageList=new ArrayList<>();
        for(int i=0;i<replyList.size();i++){
            ReplyMessage mid=new ReplyMessage();
            int floorId=replyList.get(i).getId();
            Reply reply=replyMapper.getReplyByID(floorId);
            mid.setReplyInfo(reply);
            //查询reputation和头像链接
            //设置头像链接
            mid.setAvatarUrl(userMapper.getAvatarUrlByName(reply.getAuthor()));
            //设置reputation
            mid.setReputation(userInfoMapper.getReputationByEmail(userMapper.getEmailByName(reply.getAuthor())));
            replyMessageList.add(mid);
        }
        postingMessage.setReplyList(replyMessageList);

        return postingMessage;
    }

    @ApiOperation("查看最新十条帖子")
    @PostMapping(value = "/checkLatestPostings")
    @AuthToken
    public BoardMessage checkLatestPostings() {
        BoardMessage boardMessage = new BoardMessage();
        List<Posting> postingList = postingsMapper.listLastest();
        for(Posting posting: postingList){
            posting.setAvatarUrl(userMapper.getAvatarUrlByName(posting.getAuthor()));
        }
        boardMessage.setState(true);
        boardMessage.setPostings(postingList);
        boardMessage.setMessage("获取最新发帖成功");
        return boardMessage;
    }
}
