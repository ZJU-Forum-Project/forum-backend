package zju.group1.forum.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel("用户实体-王钟毓")
@Data
public class User {
    @ApiModelProperty("用户邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户名称不能为空")
    private String name;

    @ApiModelProperty("用户密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("头像链接[注册时不需要该属性]")
    private String avatarUrl;

    @ApiModelProperty("邮箱验证码[需先调用发送邮件认证]")
    @NotBlank(message = "验证码不能为空")
    private String token;
}
