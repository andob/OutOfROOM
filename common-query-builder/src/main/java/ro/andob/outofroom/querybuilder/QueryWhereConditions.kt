package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.Column
import java.util.*

class QueryWhereConditions : LinkedList<String>()
{
    fun addSearchConditions(search : String?, columns : Array<Column>)
    {
        addSearchConditions(search?.let(::setOf)?:setOf(), columns)
    }

    fun addSearchConditions(searchTerms : Set<String>, columns : Array<Column>)
    {
        if (searchTerms.size == 1 && columns.size == 1)
        {
            val likeArgument = "'%${SQLEscape.escapeAndUnquoteString(searchTerms.first())}%'"
            add(" ${columns[0]} like $likeArgument ")
        }
        else
        {
            val subcondition = QueryWhereConditions()
            for (searchTerm in searchTerms)
            {
                val likeArgument = "'%${SQLEscape.escapeAndUnquoteString(searchTerm)}%'"
                for (column in columns)
                    subcondition.add(" $column like $likeArgument ")
            }

            add("(${subcondition.mergeWithOr()})")
        }
    }

    fun mergeWithAnd() =
        if (!isEmpty())
            this.joinToString(separator = " and ")
        else " 1=1 "

    fun mergeWithOr() =
        if (!isEmpty())
            this.joinToString(separator = " or ")
        else " 1=1 "
}