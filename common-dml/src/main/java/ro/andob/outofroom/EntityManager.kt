package ro.andob.outofroom

class EntityManager
(
    val database : IDatabase,
    val queryArgumentConverter : QueryArgumentConverter,
)
{
    inline fun <MODEL> query
    (
        sql : String,
        arguments : Array<Any?> = arrayOf(),
        adapter : (QueryResult) -> (MODEL),
    ) : List<MODEL>
    {
        val items = mutableListOf<MODEL>()

        val argumentsStringArray = ArrayUtils.convertObjectArrayToStringArray(queryArgumentConverter, arguments)

        database.rawQuery(sql, argumentsStringArray).use { cursor ->
            val queryResult = QueryResult(cursor)
            while (cursor.moveToNext())
            {
                val item = adapter(queryResult)
                items.add(item)
            }
        }

        return items
    }

    fun exec
    (
        sql : String,
        arguments : Array<Any?> = arrayOf(),
    )
    {
        val argumentsStringArray = ArrayUtils.convertObjectArrayToStringArray(queryArgumentConverter, arguments)

        database.execSQL(sql, argumentsStringArray)
    }

    inline fun insert
    (
        or : InsertOr,
        table : Table,
        columns : List<Column>,
        adapter : (InsertData) -> (Unit),
    )
    {
        val columnNames = columns.joinToString(separator = ",", transform = { column -> "`${column.name}`" })
        val questionMarks = columns.joinToString(separator = ",", transform = { "?" })

        database.compileStatement("insert or $or into $table($columnNames) values ($questionMarks)").use { statement ->
            adapter(InsertDataImpl(statement, columns))
            statement.executeInsert()
        }
    }
}
