package com.ashago.mainapp.util;

import java.util.function.Consumer;

public class CommonUtil {
    public  static <T> void executeIfNotNull(Consumer<T> execute, T notNull) {
        if (notNull != null) {
            execute.accept(notNull);
        }
    }
}
