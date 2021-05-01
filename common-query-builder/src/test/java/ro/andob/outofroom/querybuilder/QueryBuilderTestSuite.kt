package ro.andob.outofroom.querybuilder

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses
(
    QueryJoinAndProjectionClausesTests::class,
    QueryWhereConditionsTests::class,
    RestaurantJoinListQueryBuilderTests::class,
    RestaurantListQueryBuilderTests::class,
    SQLEscapeTests::class
)
class QueryBuilderTestSuite
