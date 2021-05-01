package ro.andob.outofroom

//todo LOWEST add unit tests
abstract class Table
(
    val name : String
)
{
    val columns by lazy { findColumnsReflectively() }

    private fun findColumnsReflectively() : List<Column>
    {
        return this::class.java.declaredFields
            .filter { field -> field.type==Column::class.java }
            .map { field -> field.also { it.isAccessible=true } }
            .map { field -> field.get(this) as Column }
    }

    open val primaryKey : PrimaryKey? get() = null

    override fun toString() = name

    fun toCreateTableSQL() : String
    {
        val definitions=mutableListOf<String>()
        definitions.addAll(columns.map { column ->
            "`${column.name}` ${column.type} ${if (column.notNull) "not null" else ""}"
        })

        primaryKey?.let { primaryKey ->
            definitions.add("primary key "+
                primaryKey.columns.joinToString(separator = ", ",
                    transform = { column -> "`${column.name}`" }))
        }

        //todo MED create table foreign key
        //todo MED create index
        //todo MED create unique index
        return "create table if not exists `$name` (${definitions.joinToString(separator = ", ")})"
    }
}
