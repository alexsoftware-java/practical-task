package io.rateboard.reservationapi.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Async messaging call return type is void, exceptions will not be propagated to the calling thread.
 * So, we need to add extra configurations to handle Async exceptions.
 */
@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        log.warn("Exception occurred during async call!", throwable);
    }
}