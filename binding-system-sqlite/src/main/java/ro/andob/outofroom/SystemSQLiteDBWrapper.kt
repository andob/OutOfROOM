package ro.andob.outofroom

import android.database.sqlite.SQLiteDatabase

class SystemSQLiteDBWrapper
(
    private val databaseProvider : () -> SQLiteDatabase
) : IDatabase
{
    override fun rawQuery(sql : String, args : Array<String?>) : ICursor =
        SystemSQLiteDBCursorWrapper(databaseProvider().rawQuery(sql, args))

    override fun execSQL(sql : String, args : Array<String?>) =
        databaseProvider().execSQL(sql, args)

    override fun compileStatement(sql : String) : IStatement =
        SystemSQLiteDBStatementWrapper(databaseProvider().compileStatement(sql))
}
