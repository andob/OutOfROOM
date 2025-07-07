package ro.andob.outofroom

import io.requery.android.database.sqlite.SQLiteDatabase

class LatestSQLiteDBWrapper
(
    private val databaseProvider : () -> SQLiteDatabase
) : IDatabase
{
    override fun rawQuery(sql : String, args : Array<String?>) : ICursor =
        LatestSQLiteDBCursorWrapper(databaseProvider().rawQuery(sql, args))

    override fun execSQL(sql : String, args : Array<String?>) =
        databaseProvider().execSQL(sql, args)

    override fun compileStatement(sql : String) : IStatement =
        LatestSQLiteDBStatementWrapper(databaseProvider().compileStatement(sql))
}
