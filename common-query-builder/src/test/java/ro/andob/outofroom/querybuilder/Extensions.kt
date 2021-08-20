package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.*

fun String.removeUnnecessarySpaces() = this
    .trim()
    .replace("\n", " ")
    .replace("[ ]{2,}".toRegex(), " ")
    .replace(" ,", ",")
    .replace("( ", "(")
    .replace(" )", ")")


fun String.asTable() = object : Table(this)
{
    override val primaryKey : PrimaryKey? = null
    override val foreignKeys : List<ForeignKey> = listOf()
}

fun String.asColumn() = Column(name = this, type = SQLType.Blob)
