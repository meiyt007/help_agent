package com.zfsoft.platform.utils.util;

import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: kkfan
 * @create: 2022-03-28 10:39:34
 * @description:
 */
@Slf4j
public class ExceptHandle {


    public static <R> Supplier<R> handlingSupplierWrapper(
            ThrowingSupplier<R, Exception> throwingConsumer) {
        return handlingSupplierWrapper(throwingConsumer, Exception.class);
    }

    public static <R, E extends Exception> Supplier<R> handlingSupplierWrapper(ThrowingSupplier<R, E> throwingSupplier, Class<E> exceptionClass) {
        return () -> {
            try {
                return throwingSupplier.get();
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    log.error(exCast.getMessage(), exCast);
                    throw new ResultInfoException(exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    log.error(ccEx.getMessage(), ccEx);
                    throw new ResultInfoException(ccEx.getMessage());
                }
            }
        };
    }

    public static <T> Consumer<T> handlingConsumerWrapper(
            ThrowingConsumer<T, Exception> throwingConsumer) {
        return handlingConsumerWrapper(throwingConsumer, Exception.class);
    }

    public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
            ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    log.error(exCast.getMessage(), exCast);
                    throw new ResultInfoException(exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    log.error(ccEx.getMessage(), ccEx);
                    throw new ResultInfoException(ccEx.getMessage());
                }
            }
        };
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface ThrowingSupplier<R, E extends Exception> {
        R get() throws E;
    }
}
