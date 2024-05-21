package com.iweb.blog.controller;

import com.iweb.blog.service.SysUserService;
import com.iweb.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户信息
 * @author DestinyDerek
 * @date 2024/05/16
 */
@RestController//它将控制器中的方法的返回值直接序列化为 JSON 或其他格式的数据，而不是渲染为视图页面。
@RequestMapping("users")
public class UserController {
    @Autowired
    private SysUserService sysUserService;
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization")String token){
        return sysUserService.getUserInfoByToken(token);
    }
}
