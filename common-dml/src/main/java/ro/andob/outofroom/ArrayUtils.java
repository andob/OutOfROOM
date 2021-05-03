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
            else strings[i]=null;
        }

        return strings;
    }
}
