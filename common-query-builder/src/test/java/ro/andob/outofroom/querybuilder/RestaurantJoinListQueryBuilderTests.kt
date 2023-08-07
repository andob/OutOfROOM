package ro.andob.outofroom.querybuilder

import com.yatatsu.fieldschema.FS
import org.junit.Assert.assertEquals
import org.junit.Test
import ro.andob.outofroom.querybuilder.model.RestaurantFilter
import ro.andob.outofroom.querybuilder.querybuilder.RestaurantJoinListQueryBuilder

class RestaurantJoinListQueryBuilderTests
{
    @Test
    fun testJoinsAndProjections()
    {
        val filter = RestaurantFilter()
        val page = Page(limit = 100, offset = 1)

        val queryBuilder = RestaurantJoinListQueryBuilder(filter, page)
        val resultQuery = queryBuilder.build().first.removeUnnecessarySpaces()
        val expectedQuery = "select ${FS.Restaurant}.*, "+
                    "${FS.City}.${FS.City_name} as ${FS.RestaurantJoin_cityName}, "+
                    "${FS.Country}.${FS.Country_name} as ${FS.RestaurantJoin_countryName} "+
                "from ${FS.Restaurant} "+
                "inner join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "inner join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id} "+
                "where 1=1 "+
                "order by ${FS.Restaurant_id} asc "+
                (if (QueryBuilderDefaults.isPaginationEnabled)
                    "limit ${page.limit} offset ${page.offset}"
                else "")

        assertEquals(expectedQuery.trim(), resultQuery)
    }

    @Test
    fun testJoinsAndProjectionsWithModifiedDefaultPaginationBehavior()
    {
        QueryBuilderDefaults.isPaginationEnabled = true
        testJoinsAndProjections()
        QueryBuilderDefaults.isPaginationEnabled = false
    }
}
