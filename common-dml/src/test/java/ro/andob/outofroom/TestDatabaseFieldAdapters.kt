package ro.andob.outofroom

import ro.andob.outofroom.model.StringId
import ro.andob.outofroom.model.toStringId

fun <T> QueryResult.getStringId(column : Column) : StringId<T>? = getString(column)?.toStringId()
fun <T> InsertData.putStringId(column : Column, id : StringId<T>?) = putString(column, id?.toString())
