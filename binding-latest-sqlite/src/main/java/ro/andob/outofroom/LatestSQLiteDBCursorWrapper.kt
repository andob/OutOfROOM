package ro.andob.outofroom

import android.database.Cursor

class LatestSQLiteDBCursorWrapper
(
    private val delegate : Cursor
) : ICursor
{
    override fun getColumnIndexOrThrow(name : String) : Int = delegate.getColumnIndexOrThrow(name)

    private inline fun <T> nullOr(index : Int, some : () -> T?) : T? = if (delegate.isNull(index)) null else some()

    override fun getString(index : Int) : String? = nullOr(index) { delegate.getString(index) }
    override fun getInt(index : Int) : Int? = nullOr(index) { delegate.getInt(index) }
    override fun getLong(index : Int) : Long? = nullOr(index) { delegate.getLong(index) }
    override fun getFloat(index : Int) : Float? = nullOr(index) { delegate.getFloat(index) }
    override fun getDouble(index : Int) : Double? = nullOr(index) { delegate.getDouble(index) }
    override fun getBytes(index : Int) : ByteArray? = nullOr(index) { delegate.getBlob(index) }

    override fun moveToFirst() : Boolean = delegate.moveToFirst()

    override fun moveToNext() : Boolean = delegate.moveToNext()

    override fun close() = delegate.close()
}
