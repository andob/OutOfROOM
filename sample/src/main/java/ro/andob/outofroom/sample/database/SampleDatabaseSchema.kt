package ro.andob.outofroom.sample.database

import ro.andob.outofroom.*

object SampleDatabaseSchema : Schema()
{
    val noteTable = NoteTable()
    class NoteTable : Table(name = "Note")
    {
        val id = Column(name = "id", type = SQLType.Text, notNull = true)
        val title = Column(name = "title", type = SQLType.Text)
        val message = Column(name = "message", type = SQLType.Text)

        override val primaryKey get() = PrimaryKey(id)
        override val foreignKeys get() = listOf<ForeignKey>()
    }

    override val indices get() = listOf(
        Index(table = noteTable, column = noteTable.title),
        Index(table = noteTable, column = noteTable.message))
}
