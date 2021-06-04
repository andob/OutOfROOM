package ro.andob.outofroom.querybuilder.querybuilder

import com.yatatsu.fieldschema.FS
import ro.andob.outofroom.querybuilder.QueryBuilder
import ro.andob.outofroom.querybuilder.QueryWhereConditions
import ro.andob.outofroom.querybuilder.model.RestaurantFilter

class RestaurantListQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter: RestaurantFilter) : super(filter)

    override fun table() = FS.Restaurant.table

    override fun where(conditions : QueryWhereConditions) : String
    {
        if (filter.search!=null)
            conditions.addSearchConditions(filter.search, onColumns = arrayOf(FS.Restaurant_name.column))

        if (filter.rating!=null)
            conditions.add("${FS.Restaurant_rating} = ${filter.rating}")

        if (filter.boundingBox!=null)
        {
            conditions.add("${FS.Restaurant_latitude}  <= ${filter.boundingBox?.northWestLat}")
            conditions.add("${FS.Restaurant_latitude}  >= ${filter.boundingBox?.southEastLat}")
            conditions.add("${FS.Restaurant_longitude} >= ${filter.boundingBox?.northWestLng}")
            conditions.add("${FS.Restaurant_longitude} <= ${filter.boundingBox?.southEastLng}")
        }

        return conditions.mergeWithAnd()
    }
}