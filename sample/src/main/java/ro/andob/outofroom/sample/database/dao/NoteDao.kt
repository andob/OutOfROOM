package ro.andob.outofroom.sample.database.dao

import ro.andob.outofroom.EntityManager
import ro.andob.outofroom.InsertData
import ro.andob.outofroom.InsertOr
import ro.andob.outofroom.QueryResult
import ro.andob.outofroom.sample.database.SampleDatabaseSchema
import ro.andob.outofroom.sample.database.getStringId
import ro.andob.outofroom.sample.database.putStringId
import ro.andob.outofroom.sample.database.query_builder.NoteListQueryBuilder
import ro.andob.outofroom.sample.model.Note
import ro.andob.outofroom.sample.model.NoteFilter
import ro.andob.outofroom.sample.model.StringId

class NoteDao
(
    private val entityManager : EntityManager,
    private val schema : SampleDatabaseSchema,
)
{
    fun getAll(noteFilter : NoteFilter) : List<Note>
    {
        return entityManager.query(
            sql = NoteListQueryBuilder(noteFilter, schema).build(),
            adapter = ::queryResultToNote)
    }

    fun getById(noteId : StringId<Note>) : Note?
    {
        return entityManager.query(
            sql = "select * from ${schema.noteTable} where ${schema.noteTable.id}=?",
            arguments = listOf(noteId),
            adapter = ::queryResultToNote
        ).firstOrNull()
    }

    fun insert(note : Note, or : InsertOr = InsertOr.Fail)
    {
        return entityManager.insert(or = or,
            table = schema.noteTable,
            columns = schema.noteTable.columns,
            adapter = { insertData -> populateInsertData(insertData, note) })
    }

    fun update(note : Note) = insert(note, or = InsertOr.Replace)

    fun delete(note : Note)
    {
        entityManager.exec(
            sql = "delete from ${schema.noteTable} where ${schema.noteTable.id}=?",
            arguments = listOf(note.id))
    }

    private fun queryResultToNote(queryResult : QueryResult) = Note(
        id = queryResult.getStringId(schema.noteTable.id),
        title = queryResult.getStringOrNull(schema.noteTable.title)?:"",
        message = queryResult.getStringOrNull(schema.noteTable.message)?:"")

    private fun populateInsertData(insertData : InsertData, note : Note)
    {
        insertData.putStringId(schema.noteTable.id, note.id)
        insertData.putString(schema.noteTable.title, note.title)
        insertData.putString(schema.noteTable.message, note.message)
    }
}
