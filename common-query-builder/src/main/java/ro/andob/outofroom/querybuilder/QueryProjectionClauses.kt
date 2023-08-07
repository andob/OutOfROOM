package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.Column
import ro.andob.outofroom.Table
import java.util.*

class QueryProjectionClauses : LinkedList<String>()
{
    fun addAllFieldsFromTable(table : Table) : QueryProjectionClauses
    {
        add("$table.*")
        return this
    }

    fun addField(fieldName : Column, fromTable : Table, projectAs : String) : QueryProjectionClauses
    {
        add("$fromTable.$fieldName as $projectAs")
        return this
    }

    fun addField(fieldName : Column, fromTable : Table, projectAs : Column) : QueryProjectionClauses
    {
        add("$fromTable.$fieldName as $projectAs")
        return this
    }

    fun addField(builder : ProjectionClauseBuilder)
    {
        addField(fieldName = builder.fieldName, fromTable = builder.fromTable, projectAs = builder.projectAs)
    }

    fun merge() : String
    {
        if (isNotEmpty())
            return this.joinToString(separator = " , ")
        return "*"
    }
}

class ProjectionClauseBuilder
{
    internal lateinit var fieldName : Column
    fun fieldName(fieldName : Column) = also { this.fieldName = fieldName }

    internal lateinit var fromTable : Table
    fun fromTable(fromTable : Table) = also { this.fromTable = fromTable }

    internal lateinit var projectAs : String
    fun projectAs(projectAs : String) = also { this.projectAs = projectAs }
    fun projectAs(projectAs : Column) = also { this.projectAs = projectAs.toString() }
}
