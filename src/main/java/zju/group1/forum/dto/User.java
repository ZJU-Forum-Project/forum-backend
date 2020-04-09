package zju.group1.forum.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    @Email(message = "邮箱格式错误")
    private String email;
    @NotBlank(message = "账号不能为空")
    private String name;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String avatarUrl;
    @NotBlank(message = "验证码不能为空")
    private String token;
}
