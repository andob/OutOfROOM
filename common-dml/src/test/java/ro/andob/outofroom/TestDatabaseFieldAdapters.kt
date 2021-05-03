package ro.andob.outofroom

import ro.andob.outofroom.model.StringId

fun <T> QueryResult.getStringId(column : Column) : StringId<T> = StringId(getString(column))
fun <T> InsertData.putStringId(column : Column, id : StringId<T>?) = putString(column, id?.toString())
