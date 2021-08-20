package ro.andob.outofroom

import org.junit.Assert.assertEquals
import org.junit.Test

class ToStringTests
{
    @Test
    fun testTableToString()
    {
        assertEquals("some", "some".asTable().toString())
        assertEquals("other", "other".asTable().toString())
    }

    @Test
    fun testColumnToString()
    {
        assertEquals("some", "some".asColumn().toString())
        assertEquals("other", "other".asColumn().toString())
    }

    @Test
    fun testPrimaryKeyToString()
    {
        assertEquals("primary key (`id`)",
            PrimaryKey("id".asColumn()).toString())

        assertEquals("primary key (`name`,`type`)",
            PrimaryKey("name".asColumn(), "type".asColumn()).toString())
    }

    @Test
    fun testForeignKeyToString()
    {
        assertEquals("foreign key (`cityId`) references `City`(`id`)",
            ForeignKey(sourceColumn = "cityId".asColumn(), destinationTable = "City".asTable(), destinationColumn = "id".asColumn()).toString())
    }

    @Test
    fun testIndexToString()
    {
        assertEquals("create  index if not exists `index_City_id` on `City`(`id`)",
            Index(table = "City".asTable(), "id".asColumn()).toString())

        assertEquals("create unique index if not exists `index_City_id` on `City`(`id`)",
            Index(table = "City".asTable(), "id".asColumn(), unique = true).toString())
    }
}
