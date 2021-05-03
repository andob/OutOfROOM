package ro.andob.outofroom

object TestDatabaseSchema : Schema()
{
    val noteTable = NoteTable()
    class NoteTable : Table(name = "Note")
    {
        val id = Column(name = "id", type = SQLType.Text, notNull = true)
        val title = Column(name = "title", type = SQLType.Text)
        val message = Column(name = "message", type = SQLType.Text)
        val someInt = Column(name = "someInt", type = SQLType.Integer)
        val someLong = Column(name = "someLong", type = SQLType.Integer)
        val someFloat = Column(name = "someFloat", type = SQLType.Real)
        val someDouble = Column(name = "someDouble", type = SQLType.Real)
        val someBoolean = Column(name = "someBoolean", type = SQLType.Integer)

        override val primaryKey get() = PrimaryKey(id)
        override val foreignKeys get() = listOf<ForeignKey>()
    }

    override val indices get() = listOf(
        Index(table = noteTable, column = noteTable.title),
        Index(table = noteTable, column = noteTable.message))
}
