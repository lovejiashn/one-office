package com.jiashn.oneofficesso.domain.req;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jiangjs
 * @date 2022-05-18 7:19
 */
@Data
@Accessors(chain = true)
public class UserLoginReq {

    @NotBlank(message = "请填写用户名")
    private String username;
    @NotBlank(message = "请填写密码")
    private String password;
}
