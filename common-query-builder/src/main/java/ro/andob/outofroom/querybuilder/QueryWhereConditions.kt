package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.Column
import java.util.*

class QueryWhereConditions : LinkedList<String>()
{
    fun addSearchConditions(search : String?, onColumns : Array<Column>)
    {
        if (search!=null&&search.isNotEmpty())
        {
            val likeArgument="'%${SQLEscape.escapeAndUnquoteString(search)}%'"

            if (onColumns.size==1)
                add(" ${onColumns[0]} like $likeArgument ")
            else
            {
                val subcondition=QueryWhereConditions()
                for (columnName in onColumns)
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