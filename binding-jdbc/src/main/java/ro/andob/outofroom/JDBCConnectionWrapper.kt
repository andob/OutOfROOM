package ro.andob.outofroom

import java.sql.Connection

class JDBCConnectionWrapper
(
    private val connectionProvider : () -> (Connection)
) : IDatabase
{
    override fun rawQuery(sql : String, args : Array<String>) : ICursor
    {
        JDBCStatementWrapper(connectionProvider().prepareStatement(sql, args)).use { statement ->
            return statement.executeQuery()
        }
    }

    override fun execSQL(sql : String, args : Array<String>)
    {
        JDBCStatementWrapper(connectionProvider().prepareStatement(sql, args)).use { statement ->
            statement.execute()
        }
    }

    override fun compileStatement(sql : String) : IStatement
    {
        return JDBCStatementWrapper(connectionProvider().prepareStatement(sql))
    }
}
