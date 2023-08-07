package ro.andob.outofroom.querybuilder

import com.yatatsu.fieldschema.FS
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryJoinAndProjectionClausesTests
{
    @Test
    fun testInnerAndLeftOuterJoins()
    {
        val clauses = QueryJoinClauses()

        clauses.addInnerJoin(
            table = FS.Restaurant.asTable(),
            column = FS.Restaurant_cityId.asColumn(),
            remoteTable = FS.City.asTable(),
            remoteColumn = FS.City_id.asColumn())

        clauses.addLeftOuterJoin(
            table = FS.City.asTable(),
            column = FS.City_countryId.asColumn(),
            remoteTable = FS.Country.asTable(),
            remoteColumn = FS.Country_id.asColumn())

        val resultQuery = "select * from ${FS.Restaurant} ${clauses.merge()!!}".removeUnnecessarySpaces()
        val expectedQuery = "select * from ${FS.Restaurant} "+
                "inner join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "left outer join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id}"

        assertEquals(expectedQuery, resultQuery)
    }

    @Test
    fun testRightOuterAndCrossJoins()
    {
        val clauses = QueryJoinClauses()

        clauses.addRightOuterJoin(
            table = FS.Restaurant.asTable(),
            column = FS.Restaurant_cityId.asColumn(),
            remoteTable = FS.City.asTable(),
            remoteColumn = FS.City_id.asColumn())

        clauses.addCrossJoin(
            table = FS.City.asTable(),
            column = FS.City_countryId.asColumn(),
            remoteTable = FS.Country.asTable(),
            remoteColumn = FS.Country_id.asColumn())

        val resultQuery = "select * from ${FS.Restaurant} ${clauses.merge()!!}".removeUnnecessarySpaces()
        val expectedQuery = "select * from ${FS.Restaurant} "+
                "right outer join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "cross join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id}"

        assertEquals(expectedQuery, resultQuery)
    }

    @Test
    fun testFullOuterJoinsAndProjection()
    {
        val joinClauses = QueryJoinClauses()
        val projectionClauses = QueryProjectionClauses()

        joinClauses.addFullOuterJoin(
            table = FS.Restaurant.asTable(),
            column = FS.Restaurant_cityId.asColumn(),
            remoteTable = FS.City.asTable(),
            remoteColumn = FS.City_id.asColumn())

        joinClauses.addFullOuterJoin(
            table = FS.City.asTable(),
            column = FS.City_countryId.asColumn(),
            remoteTable = FS.Country.asTable(),
            remoteColumn = FS.Country_id.asColumn())

        projectionClauses.addAllFieldsFromTable(FS.Restaurant.asTable())

        projectionClauses.addField(
            FS.City_name.asColumn(),
            fromTable = FS.City.asTable(),
            projectAs = FS.RestaurantJoin_cityName)

        projectionClauses.addField(
            FS.Country_name.asColumn(),
            fromTable = FS.Country.asTable(),
            projectAs = FS.RestaurantJoin_countryName)

        val resultQuery = "select ${projectionClauses.merge()} from ${FS.Restaurant} ${joinClauses.merge()!!}".removeUnnecessarySpaces()
        val expectedQuery = "select ${FS.Restaurant}.*, "+
                    "${FS.City}.${FS.City_name} as ${FS.RestaurantJoin_cityName}, "+
                    "${FS.Country}.${FS.Country_name} as ${FS.RestaurantJoin_countryName} "+
                "from ${FS.Restaurant} "+
                "full outer join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "full outer join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id}"

        assertEquals(expectedQuery, resultQuery)
    }

    @Test
    fun testEmptyProjectionAndJoinClauses()
    {
        val joinClauses = QueryJoinClauses()
        val projectionClauses = QueryProjectionClauses()

        val resultQuery = "select ${projectionClauses.merge()} from ${FS.Restaurant} ${joinClauses.merge()?:""}".removeUnnecessarySpaces()
        val expectedQuery = "select * from ${FS.Restaurant}"

        assertEquals(expectedQuery, resultQuery)
    }
}
