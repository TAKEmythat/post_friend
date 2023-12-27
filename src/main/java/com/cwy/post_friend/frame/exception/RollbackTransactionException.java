package com.cwy.post_friend.frame.exception;

/**
 * @Classname RollbackTransactionException
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-27 15:38
 * @Since 1.0.0
 */
public class RollbackTransactionException extends Exception {
    public RollbackTransactionException() {
    }

    public RollbackTransactionException(String message) {
        super(message);
    }
}
