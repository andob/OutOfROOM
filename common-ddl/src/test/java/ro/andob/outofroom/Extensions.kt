package ro.andob.outofroom

fun String.asTable() = object : Table(this)
{
    override val primaryKey : PrimaryKey? = null
    override val foreignKeys : List<ForeignKey> = listOf()
}

fun String.asColumn() = Column(name = this, type = SQLType.Blob)
