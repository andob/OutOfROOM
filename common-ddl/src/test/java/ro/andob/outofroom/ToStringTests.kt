package ro.andob.outofroom

import org.junit.Assert.assertEquals
import org.junit.Test

class ToStringTests
{
    @Test
    fun testTableToString()
    {
        assertEquals("some", "some".table.toString())
        assertEquals("other", "other".table.toString())
    }

    @Test
    fun testColumnToString()
    {
        assertEquals("some", "some".column.toString())
        assertEquals("other", "other".column.toString())
    }

    @Test
    fun testPrimaryKeyToString()
    {
        assertEquals("primary key (`id`)",
            PrimaryKey("id".column).toString())

        assertEquals("primary key (`name`,`type`)",
            PrimaryKey("name".column, "type".column).toString())
    }

    @Test
    fun testForeignKeyToString()
    {
        assertEquals("foreign key (`cityId`) references `City`(`id`)",
            ForeignKey(sourceColumn = "cityId".column, destinationTable = "City".table, destinationColumn = "id".column).toString())
    }

    @Test
    fun testIndexToString()
    {
        assertEquals("create  index if not exists `index_City_id` on `City`(`id`)",
            Index(table = "City".table, "id".column).toString())

        assertEquals("create unique index if not exists `index_City_id` on `City`(`id`)",
            Index(table = "City".table, "id".column, unique = true).toString())
    }
}
