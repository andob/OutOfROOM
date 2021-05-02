package ro.andob.outofroom.sample.model

import java.io.Serializable

data class Note
(
    val id : StringId<Note>,
    val title : String,
    val message : String,
) : Serializable
