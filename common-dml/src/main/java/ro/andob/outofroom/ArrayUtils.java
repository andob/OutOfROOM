package ro.andob.outofroom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayUtils
{
    public static @NotNull String[] convertObjectArrayToStringArray(@Nullable Object[] objects)
    {
        if (objects==null)
            return new String[] {};

        String[] strings=new String[objects.length];
        for (int i=0; i<objects.length; i++)
            strings[i]=convertObjectToString(objects[i]);
        return strings;
    }

    private static String convertObjectToString(Object object)
    {
        if (object==null)
            return "";

        if (object.getClass().isArray())
        {
            StringBuilder builder=new StringBuilder();
            Object[] array=(Object[])object;

            for (int i=0; i<array.length; i++)
            {
                builder.append(convertObjectToString(array[i]));
                if (i!=array.length-1)
                    builder.append(",");
            }

            return builder.toString();
        }

        if (object instanceof Iterable)
        {
            StringBuilder builder=new StringBuilder();
            List<Object> list=new ArrayList<>();
            for (Object item : (Iterable)object)
                list.add(item);

            for (int i=0; i<list.size(); i++)
            {
                builder.append(convertObjectToString(list.get(i)));
                if (i!=list.size()-1)
                    builder.append(",");
            }

            return builder.toString();
        }

        return object.toString();
    }
}
