package ro.andob.outofroom

import java.sql.Connection

class EntityManagerBuilder
(
    private val connection : Connection
)
{
    private var queryArgumentConverter : QueryArgumentConverter = QueryArgumentConverter.identity()
    fun queryArgumentConverter(value : QueryArgumentConverter) = also { queryArgumentConverter=value }

    fun build() = EntityManager(
        database = JDBCConnectionWrapper { connection },
        queryArgumentConverter = queryArgumentConverter)
}