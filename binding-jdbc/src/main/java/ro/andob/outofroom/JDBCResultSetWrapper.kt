package ro.andob.outofroom

import java.io.Closeable
import java.sql.ResultSet
import java.sql.SQLException
import java.util.LinkedList
import java.util.Queue

class JDBCResultSetWrapper
(
    private val resultSet : ResultSet
) : ICursor, Closeable
{
    private val onClosedCallbacksQueue : Queue<() -> Unit> = LinkedList()

    override fun getColumnIndex(name : String) : Int? =
        try { resultSet.findColumn(name)-1 }
        catch (ignored : SQLException) { null }

    override fun getString(index : Int) : String? = resultSet.getString(index+1)
    override fun getInt(index : Int) : Int = resultSet.getInt(index+1)
    override fun getLong(index : Int) : Long = resultSet.getLong(index+1)
    override fun getFloat(index : Int) : Float = resultSet.getFloat(index+1)
    override fun getDouble(index : Int) : Double = resultSet.getDouble(index+1)
    override fun getBytes(index : Int) : ByteArray? = resultSet.getBytes(index+1)

    override fun moveToFirst() : Boolean = resultSet.first()
    override fun moveToNext() : Boolean = resultSet.next()

    fun onClosed(callback : () -> Unit)
    {
        onClosedCallbacksQueue.add(callback)
    }

    override fun close()
    {
        resultSet.close()
        while (onClosedCallbacksQueue.isNotEmpty())
            onClosedCallbacksQueue.remove().invoke()
    }
}
