package ro.andob.outofroom

import android.database.Cursor

class SystemSQLiteDBCursorWrapper
(
    private val delegate : Cursor
) : ICursor
{
    override fun getColumnIndexOrThrow(name : String) : Int = delegate.getColumnIndexOrThrow(name)

    override fun getString(index : Int) : String? = delegate.getString(index)
    override fun getInt(index : Int) : Int = delegate.getInt(index)
    override fun getLong(index : Int) : Long = delegate.getLong(index)
    override fun getFloat(index : Int) : Float = delegate.getFloat(index)
    override fun getDouble(index : Int) : Double = delegate.getDouble(index)

    override fun moveToFirst() : Boolean = delegate.moveToFirst()

    override fun moveToNext() : Boolean = delegate.moveToNext()

    override fun close() = delegate.close()
}
