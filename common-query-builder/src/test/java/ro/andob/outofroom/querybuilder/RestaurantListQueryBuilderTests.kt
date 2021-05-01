package ro.andob.outofroom.querybuilder

import com.yatatsu.fieldschema.FS
import org.junit.Test
import org.junit.Assert.*
import ro.andob.outofroom.querybuilder.model.BoundingBox
import ro.andob.outofroom.querybuilder.model.RestaurantFilter
import ro.andob.outofroom.querybuilder.querybuilder.RestaurantListQueryBuilder

class RestaurantListQueryBuilderTests
{
    @Test
    fun testWithoutFilter()
    {
        val filter=RestaurantFilter()

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} where 1==1"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWithRatingFilter()
    {
        val filter=RestaurantFilter().copy(rating = 4)

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_rating} = ${filter.rating}"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWithBoundingBoxFilter()
    {
        val filter=RestaurantFilter().copy(boundingBox = BoundingBox(
            northWestLat = 1.0, northWestLng = 2.0,
            southEastLat = 3.0, southEastLng = 4.0))

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_latitude} <= ${filter.boundingBox!!.northWestLat} and ${FS.Restaurant_latitude} >= ${filter.boundingBox!!.southEastLat} "+
                "and ${FS.Restaurant_longitude} >= ${filter.boundingBox!!.northWestLng} and ${FS.Restaurant_longitude} <= ${filter.boundingBox!!.southEastLng}"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWithBoundingBoxAndRatingFilter()
    {
        val filter=RestaurantFilter().copy(
            rating = 4,
            boundingBox = BoundingBox(
                northWestLat = 1.0, northWestLng = 2.0,
                southEastLat = 3.0, southEastLng = 4.0))

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_rating} = ${filter.rating} "+
                "and ${FS.Restaurant_latitude} <= ${filter.boundingBox!!.northWestLat} and ${FS.Restaurant_latitude} >= ${filter.boundingBox!!.southEastLat} "+
                "and ${FS.Restaurant_longitude} >= ${filter.boundingBox!!.northWestLng} and ${FS.Restaurant_longitude} <= ${filter.boundingBox!!.southEastLng}"

        assertEquals(resultQuery, expectedQuery)
    }
}
