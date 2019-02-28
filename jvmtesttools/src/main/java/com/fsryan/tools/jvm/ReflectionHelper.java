package com.fsryan.tools.jvm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class ReflectionHelper {

    @Nullable
    public static Method getMethodRecursivelyByNameSafe(@Nonnull Class<?> cls, @Nonnull String methodName) {
        return getMethodRecursivelyByName(cls, methodName, false);
    }

    @SuppressWarnings("ConstantConditions") // <-- RuntimeException thrown
    @Nonnull
    public static Method getMethodRecursivelyByNameExplosive(@Nonnull Class<?> cls, @Nonnull String methodName) {
        return getMethodRecursivelyByName(cls, methodName, true);
    }

    @Nullable
    public static Method getMethodRecursivelyByName(@Nonnull Class<?> cls, @Nonnull String methodName, boolean invokeExplosively) {
        for (Method m : cls.getDeclaredMethods()) {
            if (methodName.equals(m.getName())) {
                return m;
            }
        }
        if (cls == Object.class) {
            if (invokeExplosively) {
                throw new RuntimeException(String.format("Cannot find method '%s' of class '%s'", methodName, cls));
            }
            return null;
        }
        return getMethodRecursivelyByName(cls.getSuperclass(), methodName, invokeExplosively);
    }

    public static void setFieldExplosively(Class<?> cls, String fieldName, Object dest, Object val) {
        try {
            Field f = getFieldRecursivelyByNameExplosive(cls, fieldName);
            f.setAccessible(true);
            f.set(dest, val);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not set value for field '%s' of class '%s' to object '%s'", fieldName, cls, val), e);
        }
    }

    @Nullable
    public static Field getFieldRecursivelyByNameSafe(Class<?> cls, String fieldName) {
        return getFieldRecursivelyByName(cls, fieldName, false);
    }

    @SuppressWarnings("ConstantConditions") // <-- RuntimeException thrown
    @Nonnull
    public static Field getFieldRecursivelyByNameExplosive(Class<?> cls, String fieldName) {
        return getFieldRecursivelyByName(cls, fieldName, true);
    }

    @Nullable
    public static Field getFieldRecursivelyByName(@Nonnull Class<?> cls, @Nonnull String fieldName, boolean invokeExplosively) {
        Field f = null;
        Exception e = null;
        try {
            f = cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsfe) {
            e = nsfe;
        }

        if (f == null) {
            if (cls == Object.class) {
                if (invokeExplosively) {
                    throw new RuntimeException(String.format("Cannot find feild '%s' of class '%s'", fieldName, cls), e);
                }
                return null;
            }
            return getFieldRecursivelyByName(cls.getSuperclass(), fieldName, invokeExplosively);
        }

        return f;
    }
}
