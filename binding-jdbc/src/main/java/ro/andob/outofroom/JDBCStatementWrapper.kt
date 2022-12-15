package ro.andob.outofroom

import java.sql.PreparedStatement
import java.sql.Types

class JDBCStatementWrapper
(
    private val statement : PreparedStatement
) : IStatement
{
    override fun bindString(index : Int, value : String) = statement.setString(index, value)
    override fun bindLong(index : Int, value : Long) = statement.setLong(index, value)
    override fun bindDouble(index : Int, value : Double) = statement.setDouble(index, value)
    override fun bindBytes(index : Int, value : ByteArray) = statement.setBytes(index, value)

    override fun bindNull(index : Int) = statement.setNull(index, Types.NULL)

    override fun executeInsert() { statement.execute() }

    internal fun executeQuery() : ICursor = JDBCResultSetWrapper(statement.executeQuery())

    internal fun execute() { statement.execute() }

    override fun close() = statement.close()
}
