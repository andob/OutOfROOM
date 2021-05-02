package ro.andob.outofroom

abstract class Schema
{
    val tables by lazy { findTablesReflectively() }

    private fun findTablesReflectively() : List<Table>
    {
        return this::class.java.declaredFields
            .filter { field -> Table::class.java.isAssignableFrom(field.type) }
            .map { field -> field.also { it.isAccessible=true } }
            .map { field -> field.get(this) as Table }
    }
}
