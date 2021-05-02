package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.*

fun String.removeUnnecessarySpaces() = this
    .trim()
    .replace("\n", " ")
    .replace("[ ]{2,}".toRegex(), " ")
    .replace(" ,", ",")
    .replace("( ", "(")
    .replace(" )", ")")


val String.table get() = asTable()
private fun String.asTable() = object : Table(this)
{
    override val primaryKey : PrimaryKey? = null
    override val foreignKeys : List<ForeignKey> = listOf()
}

val String.column get() = asColumn()
private fun String.asColumn() = Column(name = this, type = SQLType.Blob)
