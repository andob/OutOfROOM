package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.Column
import ro.andob.outofroom.SQLType
import ro.andob.outofroom.Table

fun String.removeUnnecessarySpaces() = this
    .trim()
    .replace("\n", " ")
    .replace("[ ]{2,}".toRegex(), " ")
    .replace(" ,", ",")
    .replace("( ", "(")
    .replace(" )", ")")

val String.table get() = asTable()
val String.column get() = asColumn()

private fun String.asTable() = object : Table(this) {}
private fun String.asColumn() = Column(name = this, type = SQLType.Blob)
