package ro.andob.outofroom

import io.requery.android.database.sqlite.SQLiteDatabase

class LatestSQLiteDBWrapper
(
    private val database : SQLiteDatabase
) : IDatabase
{
    override fun rawQuery(sql : String, args : Array<String>) : ICursor =
        LatestSQLiteDBCursorWrapper(database.rawQuery(sql, args))

    override fun execSQL(sql : String, args : Array<String>) = database.execSQL(sql, args)

    override fun compileStatement(sql : String) : IStatement =
        LatestSQLiteDBStatementWrapper(database.compileStatement(sql))
}
