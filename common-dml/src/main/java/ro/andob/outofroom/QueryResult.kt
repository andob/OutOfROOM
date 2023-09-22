package ro.andob.outofroom

class QueryResult
(
    private val cursor : ICursor
)
{
    fun getString(column : Column) : String? =
        cursor.getColumnIndex(column.name)?.let(cursor::getString)

    fun getInt(column : Column) : Int? =
        cursor.getColumnIndex(column.name)?.let(cursor::getInt)

    fun getLong(column : Column) : Long? =
        cursor.getColumnIndex(column.name)?.let(cursor::getLong)

    fun getFloat(column : Column) : Float? =
        cursor.getColumnIndex(column.name)?.let(cursor::getFloat)

    fun getDouble(column : Column) : Double? =
        cursor.getColumnIndex(column.name)?.let(cursor::getDouble)

    fun getBoolean(column : Column) : Boolean =
        cursor.getColumnIndex(column.name)?.let(cursor::getInt)==true.toInt()

    fun getBytes(column : Column) : ByteArray? =
        cursor.getColumnIndex(column.name)?.let(cursor::getBytes)

    fun toInt() = cursor.getInt(0)?:0
    fun toLong() = cursor.getLong(0)?:0L
    fun toFloat() = cursor.getFloat(0)?:0f
    fun toDouble() = cursor.getDouble(0)?:0.0
    fun toBoolean() = cursor.getLong(0)!=0L
}
