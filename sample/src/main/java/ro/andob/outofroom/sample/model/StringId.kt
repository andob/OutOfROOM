package ro.andob.outofroom.sample.model

import java.io.Serializable
import java.util.*

class StringId<T>
(
    private val value : String
) : Serializable
{
    companion object
    {
        @JvmStatic
        fun <T> newRandomUUID() = StringId<T>(UUID.randomUUID().toString().replace("-", ""))
    }

    override fun toString() = value
}
