package ro.andob.outofroom.querybuilder

import com.yatatsu.fieldschema.FS
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryJoinAndProjectionClausesTests
{
    @Test
    fun testInnerAndLeftOuterJoins()
    {
        val clauses=QueryJoinClauses()

        clauses.addInnerJoin(
            table = FS.Restaurant.table,
            column = FS.Restaurant_cityId.column,
            remoteTable = FS.City.table,
            remoteColumn = FS.City_id.column)

        clauses.addLeftOuterJoin(
            table = FS.City.table,
            column = FS.City_countryId.column,
            remoteTable = FS.Country.table,
            remoteColumn = FS.Country_id.column)

        val resultQuery="select * from ${FS.Restaurant} ${clauses.merge()!!}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "inner join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "left outer join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id}"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testRightOuterAndCrossJoins()
    {
        val clauses=QueryJoinClauses()

        clauses.addRightOuterJoin(
            table = FS.Restaurant.table,
            column = FS.Restaurant_cityId.column,
            remoteTable = FS.City.table,
            remoteColumn = FS.City_id.column)

        clauses.addCrossJoin(
            table = FS.City.table,
            column = FS.City_countryId.column,
            remoteTable = FS.Country.table,
            remoteColumn = FS.Country_id.column)

        val resultQuery="select * from ${FS.Restaurant} ${clauses.merge()!!}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "right outer join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "cross join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id}"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testFullOuterJoinsAndProjection()
    {
        val joinClauses=QueryJoinClauses()
        val projectionClauses=QueryProjectionClauses()

        joinClauses.addFullOuterJoin(
            table = FS.Restaurant.table,
            column = FS.Restaurant_cityId.column,
            remoteTable = FS.City.table,
            remoteColumn = FS.City_id.column)

        joinClauses.addFullOuterJoin(
            table = FS.City.table,
            column = FS.City_countryId.column,
            remoteTable = FS.Country.table,
            remoteColumn = FS.Country_id.column)

        projectionClauses.addAllFieldsFromTable(FS.Restaurant.table)

        projectionClauses.addField(
            FS.City_name.column,
            fromTable = FS.City.table,
            projectAs = FS.RestaurantJoin_cityName)

        projectionClauses.addField(
            FS.Country_name.column,
            fromTable = FS.Country.table,
            projectAs = FS.RestaurantJoin_countryName)

        val resultQuery="select ${projectionClauses.merge()} from ${FS.Restaurant} ${joinClauses.merge()!!}".removeUnnecessarySpaces()
        val expectedQuery="select ${FS.Restaurant}.*, "+
                    "${FS.City}.${FS.City_name} as ${FS.RestaurantJoin_cityName}, "+
                    "${FS.Country}.${FS.Country_name} as ${FS.RestaurantJoin_countryName} "+
                "from ${FS.Restaurant} "+
                "full outer join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "full outer join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id}"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testEmptyProjectionAndJoinClauses()
    {
        val joinClauses=QueryJoinClauses()
        val projectionClauses=QueryProjectionClauses()

        val resultQuery="select ${projectionClauses.merge()} from ${FS.Restaurant} ${joinClauses.merge()?:""}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant}"

        assertEquals(resultQuery, expectedQuery)
    }
}
