package com.jiashn.oneofficesso.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公用返回对象
 * @author jiangjs
 * @date 2022-05-18 6:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> JsonResult<T> success(T data){
        return JsonResult.<T>builder()
                .code(2000)
                .msg("成功")
                .data(data).build();
    }
    public static <T> JsonResult<T> success(String msg){
        return JsonResult.<T>builder()
                .code(2000)
                .msg(msg)
                .data(null).build();
    }

    public static <T> JsonResult<T> success(String msg,T data){
        return JsonResult.<T>builder()
                .code(2000)
                .msg(msg)
                .data(data).build();
    }

    public static <T> JsonResult<T> success(){
        return JsonResult.<T>builder()
                .code(2000)
                .msg("成功")
                .data(null).build();
    }

    public static <T> JsonResult<T> fail(String msg){
        return JsonResult.<T>builder()
                .code(5000)
                .msg(msg)
                .data(null).build();
    }

    public static <T> JsonResult<T> fail(int code,String msg){
        return JsonResult.<T>builder()
                .code(code)
                .msg(msg)
                .data(null).build();
    }
}
