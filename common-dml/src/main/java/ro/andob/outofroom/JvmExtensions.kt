package ro.andob.outofroom

import java.util.*

@Suppress("NewApi")
fun <T> List<T>.firstAsOptional() =
    Optional.ofNullable(firstOrNull())
