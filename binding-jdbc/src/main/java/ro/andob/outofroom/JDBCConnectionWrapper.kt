package ro.andob.outofroom

import java.sql.Connection
import java.sql.PreparedStatement

class JDBCConnectionWrapper
(
    private val connectionProvider : () -> (Connection)
) : IDatabase
{
    override fun rawQuery(sql : String, args : Array<String>) : ICursor
    {
        return JDBCStatementWrapper(connectionProvider().prepareStatement(sql).withArgs(args)).executeQuery()
    }

    override fun execSQL(sql : String, args : Array<String>)
    {
        JDBCStatementWrapper(connectionProvider().prepareStatement(sql).withArgs(args)).use { statement ->
            statement.execute()
        }
    }

    override fun compileStatement(sql : String) : IStatement
    {
        return JDBCStatementWrapper(connectionProvider().prepareStatement(sql))
    }

    private fun PreparedStatement.withArgs(args : Array<String>) : PreparedStatement = also { statement ->
        for ((index, arg) in args.withIndex())
            statement.setString(index+1, arg)
    }
}
