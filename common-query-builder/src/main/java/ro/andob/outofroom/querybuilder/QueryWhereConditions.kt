package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.Column
import java.util.*

class QueryWhereConditions : LinkedList<String>()
{
    fun addSearchConditions(search : String?, columns : Array<Column>)
    {
        val columnsAsStrings=Array(columns.size, init = { i -> columns[i].toString() })
        addSearchConditions(search, columnsAsStrings)
    }

    fun addSearchConditions(search : String?, columns : Array<String>)
    {
        if (search!=null&&search.isNotEmpty())
        {
            val likeArgument="'%${SQLEscape.escapeAndUnquoteString(search)}%'"

            if (columns.size==1)
            {
                add(" ${columns[0]} like $likeArgument ")
            }
            else
            {
                val subcondition=QueryWhereConditions()
                for (columnName in columns)
                    subcondition.add(" $columnName like $likeArgument ")
                add("(${subcondition.mergeWithOr()})")
            }
        }
    }

    fun mergeWithAnd() =
        if (!isEmpty())
            this.joinToString(separator = " and ")
        else " 1==1 "

    fun mergeWithOr() =
        if (!isEmpty())
            this.joinToString(separator = " or ")
        else " 1==1 "
}