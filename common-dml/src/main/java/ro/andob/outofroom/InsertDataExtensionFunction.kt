package ro.andob.outofroom

interface InsertDataExtensionFunction<T>
{
    fun putObject(`this` : InsertData, column : Column, value : T?)
}
