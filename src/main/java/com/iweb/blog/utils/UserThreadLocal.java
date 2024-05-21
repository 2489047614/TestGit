package com.iweb.blog.utils;

import com.iweb.blog.dao.pojo.SysUser;
//线程内存
public class UserThreadLocal {
    // 创建ThreadLocal对象

    private UserThreadLocal() {
    }

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    // 设置值
    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    // 获取值
    public static SysUser get() {
        return LOCAL.get();
    }

    // 移除值
    public static void remove() {
        LOCAL.remove();
    }
}

