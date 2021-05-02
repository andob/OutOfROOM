package ro.andob.outofroom

open class PrimaryKey
(
    private vararg val columns : Column
)
{
    class AutoIncrement(val column : Column) : PrimaryKey(column)

    override fun toString() = "primary key (${columns.joinToString(separator = ",") { column -> "`${column.name}`" }})"
}
