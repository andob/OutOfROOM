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
        val filter = RestaurantFilter()

        val queryBuilder = RestaurantListQueryBuilder(filter)
        val resultQuery = queryBuilder.build().first.removeUnnecessarySpaces()
        val expectedQuery = "select * from ${FS.Restaurant} where 1=1"

        assertEquals(expectedQuery, resultQuery)
    }

    @Test
    fun testWithRatingFilter()
    {
        val filter = RestaurantFilter().copy(rating = 4)

        val queryBuilder = RestaurantListQueryBuilder(filter)
        val resultQuery = queryBuilder.build().first.removeUnnecessarySpaces()
        val resultArgs = queryBuilder.build().second.toList()
        val expectedQuery = "select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_rating} = ?"
        val expectedArgs = listOf(filter.rating)

        assertEquals(expectedQuery, resultQuery)
        assertEquals(expectedArgs, resultArgs)
    }

    @Test
    fun testWithBoundingBoxFilter()
    {
        val filter = RestaurantFilter().copy(boundingBox = BoundingBox(
            northWestLat = 1.0, northWestLng = 2.0,
            southEastLat = 3.0, southEastLng = 4.0))

        val queryBuilder = RestaurantListQueryBuilder(filter)
        val resultQuery = queryBuilder.build().first.removeUnnecessarySpaces()
        val resultArgs = queryBuilder.build().second.toList()
        val expectedQuery = "select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_latitude} <= ? and ${FS.Restaurant_latitude} >= ? "+
                "and ${FS.Restaurant_longitude} >= ? and ${FS.Restaurant_longitude} <= ?"
        val expectedArgs = listOf(filter.boundingBox?.northWestLat, filter.boundingBox?.southEastLat,
            filter.boundingBox?.northWestLng, filter.boundingBox?.southEastLng)

        assertEquals(expectedQuery, resultQuery)
        assertEquals(expectedArgs, resultArgs)
    }

    @Test
    fun testWithBoundingBoxAndRatingFilter()
    {
        val filter = RestaurantFilter().copy(
            rating = 4,
            boundingBox = BoundingBox(
                northWestLat = 1.0, northWestLng = 2.0,
                southEastLat = 3.0, southEastLng = 4.0))

        val queryBuilder = RestaurantListQueryBuilder(filter)
        val resultQuery = queryBuilder.build().first.removeUnnecessarySpaces()
        val resultArgs = queryBuilder.build().second.toList()
        val expectedQuery = "select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_rating} = ? "+
                "and ${FS.Restaurant_latitude} <= ? and ${FS.Restaurant_latitude} >= ? "+
                "and ${FS.Restaurant_longitude} >= ? and ${FS.Restaurant_longitude} <= ?"
        val expectedArgs = listOf(filter.rating, filter.boundingBox?.northWestLat,
            filter.boundingBox?.southEastLat, filter.boundingBox?.northWestLng,
            filter.boundingBox?.southEastLng)

        assertEquals(expectedQuery, resultQuery)
        assertEquals(expectedArgs, resultArgs)
    }
}
