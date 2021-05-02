package ro.andob.outofroom

class Index
(
    private val table : Table,
    private val column : Column,
    private val unique : Boolean = false,
)
{
    fun toCreateIndexSQL() = "create ${if(unique) "unique" else ""} index if not exists `index_${table}_$column` on `$table`(`$column`)"

    override fun toString() = toCreateIndexSQL()
}
