package ro.andob.outofroom.querybuilder.querybuilder

import com.yatatsu.fieldschema.FS
import ro.andob.outofroom.querybuilder.*
import ro.andob.outofroom.querybuilder.model.RestaurantFilter

class RestaurantJoinListQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter : RestaurantFilter) : super(filter)

    override fun table() = FS.Restaurant.table

    override fun where(conditions : QueryWhereConditions) = "1=1"

    override fun projection(clauses : QueryProjectionClauses): String
    {
        clauses.addAllFieldsFromTable(FS.Restaurant.table)

        clauses.addField(
            FS.City_name.column,
            fromTable = FS.City.table,
            projectAs = FS.RestaurantJoin_cityName)

        clauses.addField(
            FS.Country_name.column,
            fromTable = FS.Country.table,
            projectAs = FS.RestaurantJoin_countryName)

        return clauses.merge()
    }

    override fun join(clauses : QueryJoinClauses) : String?
    {
        clauses.addInnerJoin(
            table = FS.Restaurant.table,
            column = FS.Restaurant_cityId.column,
            remoteTable = FS.City.table,
            remoteColumn = FS.City_id.column)

        clauses.addInnerJoin(
            table = FS.City.table,
            column = FS.City_countryId.column,
            remoteTable = FS.Country.table,
            remoteColumn = FS.Country_id.column)

        return clauses.merge()
    }

    override fun orderBy() = "${FS.Restaurant_id} asc"
}
