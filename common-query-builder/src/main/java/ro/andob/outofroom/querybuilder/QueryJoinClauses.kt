package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.Column
import ro.andob.outofroom.Table
import java.util.*

class QueryJoinClauses : LinkedList<String>()
{
    fun addInnerJoin      (remoteTable : Table, remoteColumn : Column, table : Table, column : Column) = join("inner",       remoteTable, remoteColumn, table, column)
    fun addCrossJoin      (remoteTable : Table, remoteColumn : Column, table : Table, column : Column) = join("cross",       remoteTable, remoteColumn, table, column)
    fun addLeftOuterJoin  (remoteTable : Table, remoteColumn : Column, table : Table, column : Column) = join("left outer",  remoteTable, remoteColumn, table, column)
    fun addRightOuterJoin (remoteTable : Table, remoteColumn : Column, table : Table, column : Column) = join("right outer", remoteTable, remoteColumn, table, column)
    fun addFullOuterJoin  (remoteTable : Table, remoteColumn : Column, table : Table, column : Column) = join("full outer",  remoteTable, remoteColumn, table, column)

    private fun join(type : String, remoteTable : Table, remoteColumn : Column, table : Table, column : Column)
    {
        add(" $type join $remoteTable on $table.$column = $remoteTable.$remoteColumn")
    }

    fun addInnerJoin      (builder : JoinClauseBuilder) = join("inner",       builder)
    fun addCrossJoin      (builder : JoinClauseBuilder) = join("cross",       builder)
    fun addLeftOuterJoin  (builder : JoinClauseBuilder) = join("left outer",  builder)
    fun addRightOuterJoin (builder : JoinClauseBuilder) = join("right outer", builder)
    fun addFullOuterJoin  (builder : JoinClauseBuilder) = join("full outer",  builder)

    private fun join(type : String, builder : JoinClauseBuilder)
    {
        join(type = type, remoteTable = builder.remoteTable, remoteColumn = builder.remoteColumn,
            table = builder.table, column = builder.column)
    }

    fun merge() : String?
    {
        if (isNotEmpty())
            return this.joinToString(separator = " ")
        return null
    }
}

class JoinClauseBuilder
{
    internal lateinit var remoteTable : Table
    fun remoteTable(remoteTable : Table) = also { this.remoteTable = remoteTable }

    internal lateinit var remoteColumn : Column
    fun remoteColumn(remoteColumn : Column) = also { this.remoteColumn = remoteColumn }

    internal lateinit var table : Table
    fun table(table : Table) = also { this.table = table }

    internal lateinit var column : Column
    fun column(column : Column) = also { this.column = column }
}
