package ro.andob.outofroom

import android.database.sqlite.SQLiteDatabase

class SystemSQLiteDBWrapper
(
    private val database : SQLiteDatabase
) : IDatabase
{
    override fun rawQuery(sql : String, args : Array<String>) : ICursor =
        SystemSQLiteDBCursorWrapper(database.rawQuery(sql, args))

    override fun execSQL(sql : String, args : Array<String>) = database.execSQL(sql, args)

    override fun compileStatement(sql : String) : IStatement =
        SystemSQLiteDBStatementWrapper(database.compileStatement(sql))
}
