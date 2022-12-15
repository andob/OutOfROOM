package ro.andob.outofroom

import ro.andob.outofroom.optional.toInt

class QueryResult
(
    private val cursor : ICursor
)
{
    fun getString(column : Column) : String? =
        cursor.getString(cursor.getColumnIndexOrThrow(column.name))

    fun getInt(column : Column) : Int? =
        cursor.getInt(cursor.getColumnIndexOrThrow(column.name))

    fun getLong(column : Column) : Long? =
        cursor.getLong(cursor.getColumnIndexOrThrow(column.name))

    fun getFloat(column : Column) : Float? =
        cursor.getFloat(cursor.getColumnIndexOrThrow(column.name))

    fun getDouble(column : Column) : Double? =
        cursor.getDouble(cursor.getColumnIndexOrThrow(column.name))

    fun getBoolean(column : Column) : Boolean =
        cursor.getInt(cursor.getColumnIndexOrThrow(column.name))==true.toInt()

    fun getBytes(column : Column) : ByteArray? =
        cursor.getBytes(cursor.getColumnIndexOrThrow(column.name))

    fun toInt() = cursor.getInt(0)?:0
    fun toLong() = cursor.getLong(0)?:0L
    fun toFloat() = cursor.getFloat(0)?:0f
    fun toDouble() = cursor.getDouble(0)?:0.0
    fun toBoolean() = cursor.getLong(0)!=0L
}
