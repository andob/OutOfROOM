package ro.andob.outofroom

interface InsertData
{
    fun hasKey(column : Column) : Boolean

    fun putString(column : Column, value : String?)
    fun putInt(column : Column, value : Int?)
    fun putLong(column : Column, value : Long?)
    fun putFloat(column : Column, value : Float?)
    fun putDouble(column : Column, value : Double?)
    fun putBoolean(column : Column, value : Boolean?)
}