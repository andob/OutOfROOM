package ro.andob.outofroom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArrayUtils
{
    public static @NotNull String[] convertObjectArrayToStringArray(@Nullable Object[] objects)
    {
        if (objects==null)
            return new String[] {};

        String[] strings=new String[objects.length];
        for (int i=0; i<objects.length; i++)
        {
            if (objects[i]!=null)
                strings[i]=objects[i].toString();
            else strings[i]="";
        }

        return strings;
    }

    public static @NotNull String createQuestionMarks(int times)
    {
        if (times<=1)
            return "?";

        StringBuilder builder=new StringBuilder();
        for (int i=0; i<times; i++)
        {
            builder.append('?');
            if (i!=times-1)
                builder.append(',');
        }

        return builder.toString();
    }
}
