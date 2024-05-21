package com.iweb.blog.service;

import com.iweb.blog.dao.pojo.SysUser;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.UserVo;

public interface SysUserService {

    /**
     * 根据id查询用户信息
     * @param id
     * @return {@link SysUser }
     */
    SysUser findUserById(Long id);

    /**
     * 根据账号密码查询用户信息
     * @param account
     * @param pwd
     * @return {@link SysUser }
     */
    SysUser findUser(String account, String pwd);

    /**
     * 根据token查询用户信息
     * @param token
     * @return {@link Result }
     */
    Result getUserInfoByToken(String token);

    /**
     * 根据账号查询用户信息
     * @param account
     * @return {@link SysUser }
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户信息
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 根据id查询用户信息
     * @param authorId
     * @return {@link UserVo }
     */
    UserVo findUserVoById(Long authorId);
}
