package com.iweb.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iweb.blog.dao.mapper.SysUserMapper;
import com.iweb.blog.dao.pojo.SysUser;
import com.iweb.blog.service.SysUserService;
import com.iweb.blog.utils.JWTUtils;
import com.iweb.blog.vo.ErrorCode;
import com.iweb.blog.vo.LoginUserVo;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser ==null){
            sysUser =new SysUser();
            sysUser.setNickname("Dermao");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String pwd) {
        //根据账号和密码查询
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // select
        //      id,account,avatar,nickname from sys_user
        // where
        //      account = ? and password = ?
        //limit 1
        sysUserLambdaQueryWrapper.eq(SysUser::getAccount,account);
        sysUserLambdaQueryWrapper.eq(SysUser::getPassword,pwd);
        sysUserLambdaQueryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname);
        sysUserLambdaQueryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(sysUserLambdaQueryWrapper);
        return sysUser;
    }

    @Override
    public Result getUserInfoByToken(String token) {
        //1.token台法性校验
        //是否为空，解析是否成功 redis是否存在
        //2.
        //如果校验失败 返回错误
        //如果成功，返回对应的结果 LoginUserVo
        Map<String, Object> map = JWTUtils.checkToken(token);
        //没有结果
        if (map==null){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }
        //
        String userJson=redisTemplate.opsForValue().get("TOKEN_"+token);
        if (StringUtils.isBlank(userJson)){
            return Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
        }
        //
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setNickname(sysUser.getNickname());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        //单表查询 直接使用 MyBatis-Plus提供的查询构建器LambdaQueryWrapper
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //select * from sys_user where account = ? limit 1;
        //account =?
        queryWrapper.eq(SysUser::getAccount,account);
        //limit 1;
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //注意 默认生成的id 是分布式id 采用了雪花算法
        this.sysUserMapper.insert(sysUser);

    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("码神之路");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        userVo.setId(sysUser.getId());
        return userVo;
    }
}
