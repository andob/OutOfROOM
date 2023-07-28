package ro.andob.outofroom

import android.database.sqlite.SQLiteStatement
import java.io.Closeable

class SystemSQLiteDBStatementWrapper
(
    private val delegate : SQLiteStatement
) : IStatement, Closeable
{
    override fun bindString(index : Int, value : String) = delegate.bindString(index, value)
    override fun bindLong(index : Int, value : Long) = delegate.bindLong(index, value)
    override fun bindDouble(index : Int, value : Double) = delegate.bindDouble(index, value)
    override fun bindBytes(index : Int, value : ByteArray) = delegate.bindBlob(index, value)

    override fun bindNull(index : Int) = delegate.bindNull(index)

    override fun executeInsert() { delegate.executeInsert() }

    override fun close() = delegate.close()
}
