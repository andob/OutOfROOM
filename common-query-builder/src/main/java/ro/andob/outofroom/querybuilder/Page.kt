package ro.andob.outofroom.querybuilder

import kotlin.math.max

class Page(limit : Int? = null, offset : Int? = 0)
{
    val limit = limit?.let { limit -> max(0, limit) }
    val offset = offset?.let { offset -> max(0, offset) }
}
