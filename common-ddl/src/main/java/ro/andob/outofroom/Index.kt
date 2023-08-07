package ro.andob.outofroom

class Index
(
    private val table : Table,
    private val columns : Array<Column>,
    private val unique : Boolean = false,
)
{
    constructor(table : Table, column : Column, unique : Boolean = false) :
        this(table = table, columns = arrayOf(column), unique = unique)

    fun toCreateIndexSQL() : String
    {
        val indexName = "index_${table}_${columns.joinToString(separator = "_")}"
        val columnsString = columns.joinToString(separator = ",") { "`$it`" }
        val uniqueString = if(unique) "unique" else ""
        return "create $uniqueString index if not exists `$indexName` on `$table`($columnsString)"
    }

    override fun toString() = toCreateIndexSQL()
}
