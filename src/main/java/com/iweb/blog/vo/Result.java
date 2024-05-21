package com.iweb.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private boolean success;

    private int code;

    private String msg;

    private  Object data;


    /**
     * 静态方法，返回成功结果
     * @param data
     * @return {@link Result }
     */
    public static Result success(Object data) {
        return new Result(true, 200, "success", data);
    }

    /**
     * 静态方法，返回失败结果
     * @param code
     * @param msg
     * @return {@link Result }
     */
    public static Result fail(int code, String msg) {
        return new Result(false, code, msg, null);
    }
}
