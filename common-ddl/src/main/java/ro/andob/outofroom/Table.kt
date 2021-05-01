package ro.andob.outofroom

//todo add unit tests on everything
//todo check on projects - what else needs to be done?
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

        //todo create table foreign key
        //todo create index
        //todo create unique index
        return "create table if not exists `$name` (${definitions.joinToString(separator = ", ")})"
    }
}
