package ro.andob.outofroom

class QueryResult
(
    private val cursor : ICursor
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

    fun toInt() = cursor.getInt(0)
    fun toLong() = cursor.getLong(0)
    fun toFloat() = cursor.getFloat(0)
    fun toDouble() = cursor.getDouble(0)
    fun toBoolean() = cursor.getLong(0)!=0L

    override fun toString() : String
    {
        return try { cursor.getString(1)?:"" }
        catch (ex : Throwable) { "" }
    }
}
