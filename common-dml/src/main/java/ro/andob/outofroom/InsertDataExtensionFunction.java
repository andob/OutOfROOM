package ro.andob.outofroom;

public interface InsertDataExtensionFunction<T>
{
    void putObject(InsertData thiz, Column column, T value);
}
