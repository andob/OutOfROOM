package ro.andob.outofroom.querybuilder.querybuilder

import com.yatatsu.fieldschema.FS
import ro.andob.outofroom.querybuilder.*
import ro.andob.outofroom.querybuilder.model.RestaurantFilter

class RestaurantJoinListQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter : RestaurantFilter, page : Page) : super(filter, page)

    override fun table() = FS.Restaurant.asTable()

    override fun where(conditions : QueryWhereConditions) = "1=1"

    override fun projection(clauses : QueryProjectionClauses): String
    {
        clauses.addAllFieldsFromTable(FS.Restaurant.asTable())

        clauses.addField(
            FS.City_name.asColumn(),
            fromTable = FS.City.asTable(),
            projectAs = FS.RestaurantJoin_cityName)

        clauses.addField(
            FS.Country_name.asColumn(),
            fromTable = FS.Country.asTable(),
            projectAs = FS.RestaurantJoin_countryName)

        return clauses.merge()
    }

    override fun join(clauses : QueryJoinClauses) : String?
    {
        clauses.addInnerJoin(
            table = FS.Restaurant.asTable(),
            column = FS.Restaurant_cityId.asColumn(),
            remoteTable = FS.City.asTable(),
            remoteColumn = FS.City_id.asColumn())

        clauses.addInnerJoin(
            table = FS.City.asTable(),
            column = FS.City_countryId.asColumn(),
            remoteTable = FS.Country.asTable(),
            remoteColumn = FS.Country_id.asColumn())

        return clauses.merge()
    }

    override fun orderBy() = "${FS.Restaurant_id} asc"
}
