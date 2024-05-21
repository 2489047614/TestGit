package com.iweb.blog.controller;

import com.iweb.blog.service.LoginService;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }

}
