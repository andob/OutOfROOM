package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.QueryArgumentConverter
import ro.andob.outofroom.Table

@Suppress("UNCHECKED_CAST")
abstract class QueryBuilder
<FILTER : IQueryBuilderFilter>
(val filter : FILTER)
{
    fun build() : String
    {
        var sql="select ${projection(QueryProjectionClauses())}"
        sql+=" from ${table()} "

        join(QueryJoinClauses())?.let { join ->
            if (join.isNotEmpty())
                sql+=join
        }

        where(QueryWhereConditions())?.let { where ->
            if (where.isNotEmpty())
                sql+=" where $where "
        }

        orderBy()?.let { order ->
            if (order.isNotEmpty())
                sql+=" order by $order "
        }

        if (isPaginationEnabled())
        {
            sql+=" limit ${filter.limit} "
            sql+=" offset ${filter.offset} "
        }

        return sql
    }

    abstract fun table() : Table?
    open fun projection(clauses : QueryProjectionClauses) : String = "*"
    open fun join(clauses : QueryJoinClauses) : String? = null
    abstract fun where(conditions : QueryWhereConditions) : String?
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
