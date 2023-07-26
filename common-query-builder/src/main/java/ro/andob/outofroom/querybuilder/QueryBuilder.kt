package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.QueryArgumentConverter
import ro.andob.outofroom.Table

@Suppress("UNCHECKED_CAST")
abstract class QueryBuilder<FILTER>
(
    val filter : FILTER,
    private val page : Page = Page(),
)
{
    fun build() : Pair<String, Array<Any?>>
    {
        val sql = StringBuilder("select ${projection(QueryProjectionClauses())}")
        sql.append(" from ${table()} ")

        join(QueryJoinClauses())?.let { join ->
            if (join.isNotEmpty())
                sql.append(join)
        }

        val (where, args) = where(QueryWhereConditions())
        if (where.isNotEmpty())
            sql.append(" where $where ")

        orderBy()?.let { order ->
            if (order.isNotEmpty())
                sql.append(" order by $order ")
        }

        if (isPaginationEnabled())
        {
            if (page.limit!=null)
                sql.append(" limit ${page.limit} ")
            if (page.offset!=null)
                sql.append(" offset ${page.offset} ")
        }

        return sql.toString() to args
    }

    abstract fun table() : Table
    open fun projection(clauses : QueryProjectionClauses) : String = "*"
    open fun join(clauses : QueryJoinClauses) : String? = null
    abstract fun where(conditions : QueryWhereConditions) : Pair<String, Array<Any?>>
    open fun orderBy() : String? = null
    open fun isPaginationEnabled() : Boolean = QueryBuilderDefaults.isPaginationEnabled
    open fun getQueryArgumentConverter() = QueryArgumentConverter { a -> a }

    val String.sqlEscaped get() = SQLEscape.escapeString(this)
    val IntArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val LongArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val DoubleArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val FloatArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val Boolean.sqlEscaped get() = SQLEscape.escapeBoolean(this)
    val Array<*>.sqlEscaped get() = SQLEscape.escapeArray(this, getQueryArgumentConverter())
    val Collection<*>.sqlEscaped get() = SQLEscape.escapeCollection(this, getQueryArgumentConverter())
}
