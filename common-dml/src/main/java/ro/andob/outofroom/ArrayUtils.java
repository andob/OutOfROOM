package ro.andob.outofroom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ArrayUtils
{
    public static @NotNull String[] convertObjectArrayToStringArray(@Nullable Object[] objects)
    {
        if (objects==null)
            return new String[] {};

        String[] strings=new String[objects.length];
        for (int i=0; i<objects.length; i++)
            strings[i]=objToString(objects[i]);
        return strings;
    }

    private static String objToString(Object object)
    {
        if (object==null)
            return "";

        if (object.getClass().isArray())
            return arrayToString((Object[])object);

        if (object instanceof Iterable)
            return iterableToString((Iterable)object);

        return object.toString();
    }

    private static String arrayToString(Object[] array)
    {
        if (array==null)
            return "";
        if (array.length==0)
            return "";

        StringBuilder builder=new StringBuilder();
        builder.append('(');
        for (int i=0; i<array.length; i++)
        {
            builder.append('\'');
            builder.append(objToString(array[i]));
            builder.append('\'');
            if (i!=array.length-1)
                builder.append(',');
        }

        builder.append(')');
        return builder.toString();
    }

    private static String iterableToString(Iterable iterable)
    {
        if (iterable==null)
            return "";

        List list=new ArrayList();
        for (Object item : iterable)
            list.add(item);

        if (list.isEmpty())
            return "";

        StringBuilder builder=new StringBuilder();
        builder.append('(');
        for (int i=0; i<list.size(); i++)
        {
            builder.append('\'');
            builder.append(objToString(list.get(i)));
            builder.append('\'');
            if (i!=list.size()-1)
                builder.append(',');
        }

        builder.append(')');
        return builder.toString();
    }
}
