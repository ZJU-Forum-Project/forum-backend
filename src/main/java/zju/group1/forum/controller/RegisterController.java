package zju.group1.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zju.group1.forum.dto.Message;
import zju.group1.forum.dto.User;
import zju.group1.forum.mapper.UserMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class RegisterController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/register")
    public Message register(@Valid User user,
                            BindingResult bindingResult,
                            HttpServletRequest request) throws IOException {
        Message message = new Message();
        System.out.println(user);
        if (bindingResult.hasErrors()) {

        }
//        HttpSession session = request.getSession();
//        if (session.getAttribute(email) != token) {
//            message.setState(false);
//            message.setMessage("您输入的验证码有误");
//            return message;
//        }
        message.setState(false);
        message.setMessage("您输入的验证码有误");
        return message;

    }

}
