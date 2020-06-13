package zju.group1.forum.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.BoardMessage;
import zju.group1.forum.dto.Posting;
import zju.group1.forum.interceptor.AuthToken;
import zju.group1.forum.mapper.PostingsMapper;
import zju.group1.forum.mapper.UserMapper;
import zju.group1.forum.provider.RedisProvider;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Api(tags = "查看个人帖子")
@RestController
public class PersonalPostingController {
    @Resource
    private PostingsMapper postingsMapper;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisProvider redisProvider;

    @ApiOperation("查看个人帖子")
    @PostMapping(value = "/personalposting")
    @AuthToken
    public BoardMessage personalPosting(@RequestParam("Authorization") String token) throws IOException {
        BoardMessage message = new BoardMessage();
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
        List<Posting> postingList = postingsMapper.listPersonalPostings(name);
        for(Posting posting: postingList){
            posting.setAvatarUrl(userMapper.getAvatarUrlByName(posting.getAuthor()));
        }
        message.setPostings(postingList);
        message.setMessage("获取个人所有发帖成功");
        return message;
    }
}
