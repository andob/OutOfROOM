package ro.andob.outofroom.sample.database.query_builder

import ro.andob.outofroom.querybuilder.QueryBuilder
import ro.andob.outofroom.querybuilder.QueryWhereConditions
import ro.andob.outofroom.sample.database.SampleDatabaseSchema
import ro.andob.outofroom.sample.model.NoteFilter

class NoteListQueryBuilder
(
    filter : NoteFilter,
    private val schema : SampleDatabaseSchema
) : QueryBuilder<NoteFilter>(filter)
{
    override fun table() = schema.noteTable

    override fun where(conditions : QueryWhereConditions) : Pair<String, Array<Any?>>
    {
        if (filter.search!=null)
        {
            conditions.addSearchConditions(
                filter.search, columns = arrayOf(
                    schema.noteTable.title,
                    schema.noteTable.message))
        }

        return conditions.mergeWithAnd() to arrayOf()
    }

    override fun isPaginationEnabled() = false
}
