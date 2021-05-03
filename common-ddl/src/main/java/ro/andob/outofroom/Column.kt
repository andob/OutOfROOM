package ro.andob.outofroom

data class Column
(
    val name : String,
    val type : SQLType,
    val notNull : Boolean = false,
)
{
    override fun toString() = name
}
