package ro.andob.outofroom

import java.sql.Connection
import java.sql.PreparedStatement

class JDBCConnectionWrapper
(
    private val connectionProvider : () -> (Connection)
) : IDatabase
{
    override fun rawQuery(sql : String, args : Array<String?>) : ICursor
    {
        val connection = connectionProvider()
        val statement = JDBCStatementWrapper(connection.prepareStatement(sql).withArgs(args))
        statement.onClosed { connection.close() }
        val cursor = statement.executeQuery()
        (cursor as JDBCResultSetWrapper).onClosed { statement.close() }
        return cursor
    }

    override fun execSQL(sql : String, args : Array<String?>)
    {
        val connection = connectionProvider()
        val statement = JDBCStatementWrapper(connection.prepareStatement(sql).withArgs(args))
        statement.onClosed { connection.close() }
        statement.use { statement.execute() }
    }

    override fun compileStatement(sql : String) : IStatement
    {
        val connection = connectionProvider()
        val statement = JDBCStatementWrapper(connection.prepareStatement(sql))
        statement.onClosed { connection.close() }
        return statement
    }

    private fun PreparedStatement.withArgs(args : Array<String?>) : PreparedStatement = also { statement ->
        args.forEachIndexed { index, arg -> statement.setString(index+1, arg) }
    }
}
