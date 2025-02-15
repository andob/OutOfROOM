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
    override fun equals(other : Any?) = (other as? StringId<*>)?.value == value
    override fun hashCode() = value.hashCode()
}

fun <T> String.toStringId() = StringId<T>(this)
