package com.iweb.blog.controller;

import com.iweb.blog.dao.pojo.SysUser;
import com.iweb.blog.utils.UserThreadLocal;
import com.iweb.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* 测试类
* */
@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
