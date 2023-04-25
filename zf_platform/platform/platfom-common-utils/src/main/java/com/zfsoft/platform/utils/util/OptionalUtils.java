package com.zfsoft.platform.utils.util;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author: kkfan
 * @create: 2021-09-17 17:22:32
 * @description:
 */
public class OptionalUtils {

    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
