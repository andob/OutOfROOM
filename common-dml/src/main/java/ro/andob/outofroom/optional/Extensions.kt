package ro.andob.outofroom.optional

import java.util.*

@Suppress("NewApi")
fun <T> List<T>.firstAsOptional() =
    Optional.ofNullable(firstOrNull())

fun Boolean.toInt()  = if(this) 1  else 0
fun Boolean.toLong() = if(this) 1L else 0L
