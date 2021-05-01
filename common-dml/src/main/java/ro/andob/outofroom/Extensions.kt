package ro.andob.outofroom

import java.util.*

fun <T> List<T>.firstAsOptional() =
    Optional.ofNullable(firstOrNull())
