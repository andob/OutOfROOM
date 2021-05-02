package ro.andob.outofroom.sample.model

import ro.andob.outofroom.querybuilder.IQueryBuilderFilter
import java.io.Serializable

data class NoteFilter
(
    override val search : String? = null,
    override val offset : Int = 0,
    override val limit : Int = 100,
) : IQueryBuilderFilter, Serializable
