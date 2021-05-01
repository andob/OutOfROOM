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

    override fun bindNull(index : Int) = statement.setNull(index, Types.NULL)

    //todo a se interpreta rezultatul - pt insert or throw
    override fun executeInsert() : Long = statement.executeUpdate().toLong()

    internal fun executeQuery() : ICursor = JDBCResultSetWrapper(statement.executeQuery())

    internal fun execute() : Boolean = statement.execute()

    override fun close() = statement.close()
}
