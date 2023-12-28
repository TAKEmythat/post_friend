package com.cwy.post_friend.frame.enum_;

/**
 * @Classname InsertMethod
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-28 9:06
 * @Since 1.0.0
 */

public enum InsertMethod {
    // 日志
    JOURNAL,
    // 事务
    TRANSACTION,
    // 前置通知
    AOP_START,
    // 后置通知
    AOP_END,
    // 错误捕获通知
    AOP_CATCH
}
