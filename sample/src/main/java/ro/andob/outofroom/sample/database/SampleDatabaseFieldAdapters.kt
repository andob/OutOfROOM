package ro.andob.outofroom.sample.database

import ro.andob.outofroom.Column
import ro.andob.outofroom.InsertData
import ro.andob.outofroom.QueryResult
import ro.andob.outofroom.sample.model.StringId

fun <T> QueryResult.getStringId(column : Column) : StringId<T> = StringId(getString(column))
fun <T> InsertData.putStringId(column : Column, id : StringId<T>?) = putString(column, id?.toString())
