package ro.andob.outofroom.dao

import ro.andob.outofroom.*
import ro.andob.outofroom.model.Note
import ro.andob.outofroom.model.StringId

class NoteDao
(
    private val entityManager : EntityManager,
    private val schema : TestDatabaseSchema,
)
{
    fun getAll() : List<Note>
    {
        return entityManager.query(
            sql = "select * from ${schema.noteTable}",
            adapter = ::queryResultToNote)
    }

    fun getById(noteId : StringId<Note>) : Note?
    {
        return entityManager.query(
            sql = "select * from ${schema.noteTable} where ${schema.noteTable.id}=?",
            arguments = arrayOf(noteId),
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
            arguments = arrayOf(note.id))
    }

    fun deleteAll()
    {
        entityManager.exec(sql = "delete from ${schema.noteTable}")
    }

    private fun queryResultToNote(queryResult : QueryResult) = Note(
        id = queryResult.getStringId(schema.noteTable.id),
        title = queryResult.getStringOrNull(schema.noteTable.title)?:"",
        message = queryResult.getStringOrNull(schema.noteTable.message)?:"",
        someInt = queryResult.getInt(schema.noteTable.someInt),
        someLong = queryResult.getLong(schema.noteTable.someLong),
        someFloat = queryResult.getFloat(schema.noteTable.someFloat),
        someDouble = queryResult.getDouble(schema.noteTable.someDouble),
        someBoolean = queryResult.getBoolean(schema.noteTable.someBoolean))

    private fun populateInsertData(insertData : InsertData, note : Note)
    {
        insertData.putStringId(schema.noteTable.id, note.id)
        insertData.putString(schema.noteTable.title, note.title)
        insertData.putString(schema.noteTable.message, note.message)
        insertData.putInt(schema.noteTable.someInt, note.someInt)
        insertData.putLong(schema.noteTable.someLong, note.someLong)
        insertData.putFloat(schema.noteTable.someFloat, note.someFloat)
        insertData.putDouble(schema.noteTable.someDouble, note.someDouble)
        insertData.putBoolean(schema.noteTable.someBoolean, note.someBoolean)
    }
}