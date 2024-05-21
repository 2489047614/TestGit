package com.iweb.blog.service;

import com.iweb.blog.dao.pojo.SysUser;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.params.LoginParam;

public interface LoginService {
    /**
     * 登录
     * @param loginParam
     * @return {@link Result}
     */
    Result login(LoginParam loginParam);

    /**
     * 登出
     * @param token
     * @return {@link Result }
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParam
     * @return {@link Result }
     */
    Result register(LoginParam loginParam);

    /**
     * 校验token
     * @param token
     * @return {@link SysUser }
     */
    SysUser checkToken(String token);
}
