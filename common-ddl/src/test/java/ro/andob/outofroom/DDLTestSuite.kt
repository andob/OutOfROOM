package ro.andob.outofroom

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses
(
    ToStringTests::class,
    CreateStatementsTests::class,
)
class DDLTestSuite
