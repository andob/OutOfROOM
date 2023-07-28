package ro.andob.outofroom;

import org.jetbrains.annotations.Nullable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface QueryArgumentConverter
{
    @Nullable Object convert(@Nullable Object object);

    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.CLASS)
    @interface Generator
    {
        Class<?> insertDataExtensionMethodsParentClass() default Void.class;
    }
}
