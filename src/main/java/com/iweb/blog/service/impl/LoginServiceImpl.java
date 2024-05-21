package com.iweb.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iweb.blog.dao.pojo.SysUser;
import com.iweb.blog.service.LoginService;
import com.iweb.blog.service.SysUserService;
import com.iweb.blog.utils.JWTUtils;
import com.iweb.blog.vo.ErrorCode;
import com.iweb.blog.vo.Result;
import com.iweb.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service//表示被标注的类是一个服务类，负责处理业务逻辑
@Transactional//添加事务管理(声明式事务)
public class LoginServiceImpl implements LoginService {
    private static final String slat = "mszlu!@###";
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 1.检查参数是否合法
     * 2.根据用户名和密码去user表中查询 是否存在
     * 3.如果不存在 登录失败
     * 4.如果存在 ，使用jwt 生成token 返回给前端
     * 5.token放入redis当中，redis token:user信息设置过期时间
     * (登录认证的时候 先认证token字符串是否合法去redis认证是否存在)
     * @param loginParam
     * @return {@link Result }
     */
    @Override
    public Result login(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String pwd = DigestUtils.md5Hex(password + slat);
        SysUser sysUser = sysUserService.findUser(account,pwd);
        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //登录成功，使用JWT生成token，返回token和redis中
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        //获取输入的 账号 密码 昵称
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        //判断账号 密码 昵称 是否为空
        if (StringUtils.isBlank(account)
                ||StringUtils.isBlank(password)
                ||StringUtils.isBlank(nickname)
            )
        {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //输入内容都不为null
        //从数据库中查找账号是否已经存在
        SysUser sysUser=this.sysUserService.findUserByAccount(account);
        if (sysUser!=null){
            //该账号已被创建 页面提示 账号已经存在信息
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //进行注册 将账号 密码 昵称 存入数据库
        sysUser=new SysUser();
        sysUser.setAccount(account);
        sysUser.setNickname(nickname);
        //密码需要进行加密处理
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        //创建日期 最后登录日期也需要存
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        //设置默认的虚拟人物信息
        sysUser.setAvatar("/static/img/logo.b3a40d.png");
        sysUser.setAdmin(1);//是否为管理员 1为true
        sysUser.setDeleted(0);//是否被删除 0为false
        sysUser.setSalt("");//盐值
        sysUser.setStatus("");//状态
        sysUser.setEmail("");//邮箱
        //将信息
        this.sysUserService.save(sysUser);
        //为了安全起见 注册成功后 返回一个token 用于后续的登录认证
        String token = JWTUtils.createToken(sysUser.getId());
        //将token存入redis
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        //注册成功 返回token
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        //token为空
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        //map为空 解析失败
        if (map==null){
            return null;
        }
        //解析成功
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        //将json转换为对象 解析回sysUser对象
        SysUser sysUser = JSON.parseObject(userJson,SysUser.class);
        return sysUser;
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("admin"+slat));
    }
}
