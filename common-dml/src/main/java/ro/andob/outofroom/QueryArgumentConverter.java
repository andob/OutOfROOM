package ro.andob.outofroom;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface QueryArgumentConverter
{
    Object convert(Object object);

    static QueryArgumentConverter identity() { return a -> a; }

    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.CLASS)
    @interface Generator
    {
        Class<?> insertDataExtensionMethodsParentClass() default Void.class;
    }
}
