package com.e_commerce.project;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField((fieldName));

            if (!f.canAccess(target)) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);

            if (wasPrivate) {
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
