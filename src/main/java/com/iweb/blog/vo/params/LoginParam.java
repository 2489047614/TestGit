package com.iweb.blog.vo.params;


import lombok.Data;

/**
 *获取登录参数
 * @author DestinyDerek
 * @date 2024/05/15
 */
@Data
public class LoginParam {

    private String account;

    private String password;

    private String nickname;
}
