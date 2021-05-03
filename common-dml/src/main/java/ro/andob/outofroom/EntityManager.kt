package ro.andob.outofroom

class EntityManager
(
    val database : IDatabase
)
{
    //todo test this
    inline fun <MODEL> query
    (
        sql : String,
        arguments : List<Any?> = listOf(),
        adapter : (QueryResult) -> (MODEL),
    ) : List<MODEL>
    {
        val items=mutableListOf<MODEL>()

        val argumentsStringArray=arguments.map { it.toString() }.toTypedArray()
        database.rawQuery(sql, argumentsStringArray).use { cursor ->
            val queryResult=QueryResult(cursor)
            while (cursor.moveToNext())
            {
                val item=adapter(queryResult)
                items.add(item)
            }
        }

        return items
    }

    //todo test this
    fun exec
    (
        sql : String,
        arguments : List<Any?> = listOf(),
    )
    {
        val argumentsStringArray=arguments.map { it.toString() }.toTypedArray()
        database.execSQL(sql, argumentsStringArray)
    }

    //todo test this
    inline fun insert
    (
        or : InsertOr,
        table : Table,
        columns : List<Column>,
        adapter : (InsertData) -> (Unit),
    )
    {
        val columnNames=columns.joinToString(separator = ",", transform = { column -> "`${column.name}`" })
        val questionMarks=columns.joinToString(separator = ",", transform = { "?" })

        database.compileStatement("insert or $or into $table($columnNames) values ($questionMarks)").use { statement ->
            adapter(InsertData(statement, columns))
            statement.executeInsert()
        }
    }
}
