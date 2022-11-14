package ro.andob.outofroom.querybuilder.model

data class RestaurantFilter
(
    val rating : Int? = null,
    val boundingBox : BoundingBox? = null,
    val search : String? = null,
)
