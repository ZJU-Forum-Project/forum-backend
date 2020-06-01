package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;

import zju.group1.forum.dto.*;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.ReplyMapper;

import javax.annotation.Resource;
import java.io.IOException;

@Api(tags = "查看帖子")
@RestController
public class PostingContoller {
    @Resource
    private PostingsMapper postingsMapper;
    @Resource
    private ReplyMapper replyMapper;

    @ApiOperation("查看编号“postingID”的帖子")
    @PostMapping(value = "/postings/{postingID}")
    @AuthToken
    public PostingMessage checkPostings(@PathVariable("postingID") int postingID) throws IOException {
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
            replyMessageList.add(mid);

            //查询reputation

        }
        postingMessage.setReplyList(replyMessageList);

        return postingMessage;
    }

    @ApiOperation("查看最新十条帖子")
    @PostMapping(value = "/latestpostings")
    @AuthToken
    public BoardMessage checkLatestPostings() {
        BoardMessage boardMessage = new BoardMessage();

        return boardMessage;
    }
    
}
