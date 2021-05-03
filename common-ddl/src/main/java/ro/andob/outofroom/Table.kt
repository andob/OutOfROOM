package ro.andob.outofroom

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

    abstract val primaryKey : PrimaryKey?
    abstract val foreignKeys : List<ForeignKey>

    override fun toString() = name

    fun toCreateTableSQL() : String
    {
        if (columns.isEmpty())
            throw RuntimeException("Please define columns for table \"$name\"!")

        val definitions=mutableListOf<String>()
        definitions.addAll(columns.map { column ->

            val tokens=mutableListOf<String>()
            tokens.add("`${column.name}`")
            tokens.add("${column.type}")
            if (column.notNull)
                tokens.add("not null")

            (primaryKey as? PrimaryKey.AutoIncrement)?.let { autoIncrementPrimaryKey ->
                if (autoIncrementPrimaryKey.column==column)
                    tokens.add("primary key autoincrement")
            }

            return@map tokens.joinToString(separator = " ")
        })

        if (primaryKey!=null&&primaryKey !is PrimaryKey.AutoIncrement)
            definitions.add(primaryKey.toString())

        for (foreignKey in foreignKeys)
            definitions.add(foreignKey.toString())

        return "create table if not exists `$name` (${definitions.joinToString(separator = ", ")})"
    }
}
