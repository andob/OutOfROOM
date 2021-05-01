package ro.andob.outofroom.querybuilder.model

import ro.andob.outofroom.querybuilder.IQueryBuilderFilter

data class RestaurantFilter
(
    val rating : Int? = null,
    val boundingBox : BoundingBox? = null,
    override val search : String? = null,
    override val offset : Int = 0,
    override val limit : Int = 100,
) : IQueryBuilderFilter
