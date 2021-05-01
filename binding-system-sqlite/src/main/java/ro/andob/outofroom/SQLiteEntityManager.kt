package ro.andob.outofroom

import android.database.sqlite.SQLiteDatabase

//todo LOW compatibilitate jdbc
//todo LOW compatibilitate requery sqlite
//todo HIGH sample the query builder
class SQLiteEntityManager
(
    val database : SQLiteDatabase
)
{
    //todo HIGH sample this
    inline fun <MODEL> query
    (
        sql : String,
        arguments : List<Any?> = listOf(),
        adapter : (SQLiteQueryResult) -> (MODEL),
    ) : List<MODEL>
    {
        val items=mutableListOf<MODEL>()

        val argumentsStringArray=arguments.map { it.toString() }.toTypedArray()
        database.rawQuery(sql, argumentsStringArray).use { cursor ->
            val queryResult=SQLiteQueryResult(cursor)
            while (cursor.moveToNext())
            {
                val item=adapter(queryResult)
                items.add(item)
            }
        }

        return items
    }

    //todo HIGH sample this
    fun exec
    (
        sql : String,
        arguments : List<Any?> = listOf(),
    )
    {
        val argumentsStringArray=arguments.map { it.toString() }.toTypedArray()
        database.execSQL(sql, argumentsStringArray)
    }

    //todo HIGH sample this
    inline fun insert
    (
        or : InsertOr,
        table : Table,
        columns : List<Column>,
        adapter : (SQLiteInsertData) -> (Unit),
    )
    {
        val columnNames=columns.joinToString(separator = ",", transform = { column -> "`${column.name}`" })
        val questionMarks=columns.joinToString(separator = ",", transform = { "?" })

        database.compileStatement("insert or $or $table($columnNames) values ($questionMarks)").use { statement ->
            adapter(SQLiteInsertData(statement, columns))
            statement.executeInsert()
        }
    }
}
