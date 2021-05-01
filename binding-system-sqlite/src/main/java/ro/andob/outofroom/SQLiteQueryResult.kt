package ro.andob.outofroom

import android.database.Cursor

class SQLiteQueryResult
(
    private val cursor : Cursor
)
{
    fun getString(column : Column) : String =
        cursor.getString(cursor.getColumnIndexOrThrow(column.name))!!

    fun getStringOrNull(column : Column) : String? =
        cursor.getString(cursor.getColumnIndexOrThrow(column.name))

    fun getInt(column : Column) : Int =
        cursor.getInt(cursor.getColumnIndexOrThrow(column.name))

    fun getLong(column : Column) : Long =
        cursor.getLong(cursor.getColumnIndexOrThrow(column.name))

    fun getFloat(column : Column) : Float =
        cursor.getFloat(cursor.getColumnIndexOrThrow(column.name))

    fun getDouble(column : Column) : Double =
        cursor.getDouble(cursor.getColumnIndexOrThrow(column.name))

    fun getBoolean(column : Column) : Boolean =
        cursor.getInt(cursor.getColumnIndexOrThrow(column.name))!=0

    fun toInt() = cursor.getInt(1)
    fun toLong() = cursor.getLong(1)
    fun toFloat() = cursor.getFloat(1)
    fun toDouble() = cursor.getDouble(1)
    fun toBoolean() = cursor.getLong(1)!=0L

    override fun toString() : String
    {
        return try { cursor.getString(1) }
        catch (ex : Throwable) { "" }
    }
}
