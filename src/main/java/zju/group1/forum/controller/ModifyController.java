package zju.group1.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zju.group1.forum.dto.Message;
import zju.group1.forum.mapper.UserMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class ModifyController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/modify")
    public Message modify(@RequestParam("password") String password,
                          @RequestParam("email") String email,
                          @RequestParam("token") String token,
                          HttpServletRequest request) throws IOException {

        Message message = new Message();

        HttpSession session = request.getSession();
        if (!session.getAttribute(email).equals(token)) {
            message.setState(false);
            message.setMessage("您输入的验证码有误");
            return message;
        }

        userMapper.updatePassword(email, password);
        message.setState(true);
        message.setMessage("修改成功！");

        return message;
    }
}
