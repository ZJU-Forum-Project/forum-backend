package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.BoardMessage;
import zju.group1.forum.dto.Posting;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "搜索帖子和人")
@RestController
public class SearchController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private PostingsMapper postingsMapper;

    @ApiOperation("搜索帖子和人")
    @PostMapping(value = "/search")
    @AuthToken
    public BoardMessage search(@RequestParam("content") String content){
        BoardMessage message = new BoardMessage();
        List<Posting> postingList = postingsMapper.selectByTitle(content);
        //设置头像
        for(Posting posting: postingList){
            posting.setAvatarUrl(userMapper.getAvatarUrlByName(posting.getAuthor()));
        }

        message.setPostings(postingList);
        message.setState(true);

        return message;
    }
}
