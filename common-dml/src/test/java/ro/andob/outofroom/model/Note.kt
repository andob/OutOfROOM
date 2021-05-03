package ro.andob.outofroom.model

import java.io.Serializable

data class Note
(
    val id : StringId<Note>,
    val title : String,
    val message : String,
    val someInt : Int,
    val someLong : Long,
    val someFloat : Float,
    val someDouble : Double,
    val someBoolean : Boolean,
) : Serializable
