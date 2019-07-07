package com.github.franckyi.cmpdl.api;

import java.lang.reflect.InvocationTargetException;

public interface IEnum {

    int toJson();

    @SuppressWarnings("unchecked")
    static <T extends Enum<T> & IEnum> T fromJson(Class<?> clazz, int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        T[] values = (T[]) clazz.getDeclaredMethod("values").invoke(null);
        for (T value : values) {
            if (value.toJson() == i) {
                return value;
            }
        }
        return null;
    }

}
