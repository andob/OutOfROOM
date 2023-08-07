package ro.andob.outofroom.querybuilder.querybuilder

import com.yatatsu.fieldschema.FS
import ro.andob.outofroom.querybuilder.QueryBuilder
import ro.andob.outofroom.querybuilder.QueryWhereConditions
import ro.andob.outofroom.querybuilder.asColumn
import ro.andob.outofroom.querybuilder.asTable
import ro.andob.outofroom.querybuilder.model.RestaurantFilter

class RestaurantListQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter : RestaurantFilter) : super(filter)

    override fun table() = FS.Restaurant.asTable()

    override fun where(conditions : QueryWhereConditions) : Pair<String, Array<Any?>>
    {
        val args = mutableListOf<Any?>()

        if (filter.search!=null)
            conditions.addSearchConditions(filter.search, columns = arrayOf(FS.Restaurant_name.asColumn()))

        if (filter.rating!=null)
        {
            conditions.add("${FS.Restaurant_rating} = ?")
            args.add(filter.rating)
        }

        if (filter.boundingBox!=null)
        {
            conditions.add("${FS.Restaurant_latitude}  <= ?")
            args.add(filter.boundingBox?.northWestLat)
            conditions.add("${FS.Restaurant_latitude}  >= ?")
            args.add(filter.boundingBox?.southEastLat)
            conditions.add("${FS.Restaurant_longitude} >= ?")
            args.add(filter.boundingBox?.northWestLng)
            conditions.add("${FS.Restaurant_longitude} <= ?")
            args.add(filter.boundingBox?.southEastLng)
        }

        return conditions.mergeWithAnd() to args.toTypedArray()
    }
}