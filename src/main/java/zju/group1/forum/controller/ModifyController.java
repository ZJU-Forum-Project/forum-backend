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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Api(tags = "修改密码")
@RestController
public class ModifyController {
    @Autowired
    private UserMapper userMapper;


    @ApiOperation("修改密码")
    @PostMapping(value = "/modify")
    @AuthToken
    public Message modify(@RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("token") String token,
                          HttpServletRequest request) throws IOException {

        Message message = new Message();

        HttpSession session = request.getSession();
        Object emailSession = session.getAttribute(email);
        if (emailSession == null || !emailSession.equals(token)) {
            message.setState(false);
            message.setMessage("输入的验证码有误或为空");
            return message;
        }

        userMapper.updatePassword(email, password);
        message.setState(true);
        message.setMessage("修改成功！");

        return message;
    }
}
