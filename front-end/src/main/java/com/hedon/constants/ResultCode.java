package com.hedon.constants;

import lombok.Getter;

/**
 * 返回给前端的提示码
 * @author Hedon Wang
 * @create 2020-07-12 01:07
 */

@Getter
public enum ResultCode {

    /**
     * 默认成功值
     */
    SUCCESS("00000","操作成功！"),

    /**
     * 通用错误值
     */
    ERROR("E0000","未知错误"),
    EMPTY_OBJECT("E0001","对象不能为空"),
    EMPTY_USER_ID("E0002","用户ID不能为空"),
    TIME_OUT("E0003","当前请求过多，响应超时，请稍后重试"),
    FALL_BACK("E0004","服务被降级了"),

    /**
     * 数据库操作部分错误码： DB = DataBase
     * @author Jiahan Wang
     */
    INSERT_ERROR("DB001", "新增错误"),
    UPDATE_ERROR("DB002", "更新错误"),
    DELETE_ERROR("DB003", "删除错误"),
    PARAMETER_ERROR("DB004", "参数错误"),
    INVALID_PARAMETER("DB005", "不合法的参数"),
    MISS_PARAMETER("DB006", "缺少参数"),
    REPEAT_RECORD("DB007","重复记录"),

    /**
     * 用户模块错误码  U = USER
     */
    USER_NOT_EXISTS("U0001","用户不存在"),
    USER_EXISTS("U0002","用户存在"),
    USER_PWD_WRONG("U0003","用户密码错误"),
    NO_LOGIN("U0004","未登录"),
    BALANCE_NOT_ENOUGH("U0005","余额不足"),

    /**
     * 应用部分
     */
     APPLICATION_NOT_EXISTS("A0001","应用不存在"),
     APPLICATION_ID_EMPTY("A0002","应用ID不能为空"),

    /**
     * 交易记录部分
     */
    EMPTY_ORDER_RECORD_ID("ER001","交易记录ID不能为空");


    private String code;     //错误码
    private String message;  //对应的信息

    ResultCode(String code, String msg) {
        this.code = code;
        this.message = msg;
    }


}
