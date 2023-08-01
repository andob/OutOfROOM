package ro.andob.outofroom

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class CreateStatementsTests
{
    @Test
    fun testCreateIndex()
    {
        assertEquals(
            "create  index if not exists `index_City_id` on `City`(`id`)",
            Index(table = "City".asTable(), "id".asColumn()).toString()
        )

        assertEquals(
            "create unique index if not exists `index_City_id` on `City`(`id`)",
            Index(table = "City".asTable(), "id".asColumn(), unique = true).toString()
        )
    }

    @Test
    fun testCreateTableWithColumnTypes()
    {
        object : Table(name = "Some")
        {
            val a = Column(name = "a", type = SQLType.Integer)
            val b = Column(name = "b", type = SQLType.Real)
            val c = Column(name = "c", type = SQLType.Text)
            val d = Column(name = "d", type = SQLType.Blob)
            val e = Column(name = "e", type = SQLType.Integer, notNull = true)
            val f = Column(name = "f", type = SQLType.Real, notNull = true)
            val g = Column(name = "g", type = SQLType.Text, notNull = true)
            val h = Column(name = "h", type = SQLType.Blob, notNull = true)

            override val primaryKey : PrimaryKey? get() = null
            override val foreignKeys : List<ForeignKey> get() = listOf()

        }.toCreateTableSQL().let { createTable ->
            assertEquals("create table if not exists `Some` (`a` integer, `b` real, `c` text, "+
                "`d` blob, `e` integer not null, `f` real not null, `g` text not null, `h` blob not null)", createTable)
        }
    }

    @Test
    fun testCreateTableWithPrimaryKeyAutoincrement()
    {
        object : Table(name = "City")
        {
            val id = Column(name = "id", type = SQLType.Integer, notNull = true)
            val name = Column(name = "name", type = SQLType.Text)

            override val primaryKey : PrimaryKey get() = PrimaryKey.AutoIncrement(id)
            override val foreignKeys : List<ForeignKey> get() = listOf()

        }.toCreateTableSQL().let { createTable ->
            assertEquals("create table if not exists `City` (`id` integer not null primary key autoincrement, `name` text)", createTable)
        }
    }

    @Test
    fun testCreateTableWithPrimaryKey()
    {
        object : Table(name = "City")
        {
            val id = Column(name = "id", type = SQLType.Text, notNull = true)
            val name = Column(name = "name", type = SQLType.Text)

            override val primaryKey : PrimaryKey get() = PrimaryKey(id)
            override val foreignKeys : List<ForeignKey> get() = listOf()

        }.toCreateTableSQL().let { createTable ->
            assertEquals("create table if not exists `City` (`id` text not null, `name` text, primary key (`id`))", createTable)
        }
    }

    @Test
    fun testCreateTableWithComposedPrimaryKey()
    {
        object : Table(name = "City")
        {
            val id = Column(name = "id", type = SQLType.Text, notNull = true)
            val name = Column(name = "name", type = SQLType.Text, notNull = true)

            override val primaryKey : PrimaryKey get() = PrimaryKey(id, name)
            override val foreignKeys : List<ForeignKey> get() = listOf()

        }.toCreateTableSQL().let { createTable ->
            assertEquals("create table if not exists `City` (`id` text not null, `name` text not null, primary key (`id`,`name`))", createTable)
        }
    }

    @Test
    fun testCreateTableWithForeignKeys()
    {
        val countryTable=object : Table(name = "Country")
        {
            val id = Column(name = "id", type = SQLType.Text, notNull = true)
            val name = Column(name = "name", type = SQLType.Text)

            override val primaryKey : PrimaryKey get() = PrimaryKey(id)
            override val foreignKeys : List<ForeignKey> get() = listOf()
        }

        object : Table(name = "City")
        {
            val id = Column(name = "id", type = SQLType.Text, notNull = true)
            val countryId = Column(name = "countryId", type = SQLType.Text, notNull = true)
            val name = Column(name = "name", type = SQLType.Text)

            override val primaryKey : PrimaryKey get() = PrimaryKey(id)
            override val foreignKeys : List<ForeignKey> get() = listOf(
                ForeignKey(sourceColumn = countryId,
                    destinationTable = countryTable,
                    destinationColumn = countryTable.id))

        }.toCreateTableSQL().let { createTable ->
            assertEquals("create table if not exists `City` (`id` text not null, "+
                "`countryId` text not null, `name` text, primary key (`id`), "+
                "foreign key (`countryId`) references `Country`(`id`))", createTable)
        }
    }

    @Test
    fun testCreateTableWithoutColumns()
    {
        try
        {
            object : Table(name = "Some")
            {
                override val primaryKey : PrimaryKey? = null
                override val foreignKeys : List<ForeignKey> = listOf()
            }.toCreateTableSQL()

            fail()
        }
        catch (ex : Exception)
        {
            ex.printStackTrace()
        }
    }

    @Test
    fun testCreateSchema()
    {
        val schema=object : Schema()
        {
            val countryTable = CountryTable()
            inner class CountryTable : Table(name = "Country")
            {
                val id = Column(name = "id", type = SQLType.Text, notNull = true)
                val name = Column(name = "name", type = SQLType.Text)

                override val primaryKey : PrimaryKey get() = PrimaryKey(id)
                override val foreignKeys : List<ForeignKey> get() = listOf()
            }

            val cityTable = CityTable()
            inner class CityTable : Table(name = "City")
            {
                val id = Column(name = "id", type = SQLType.Text, notNull = true)
                val countryId = Column(name = "countryId", type = SQLType.Text, notNull = true)
                val name = Column(name = "name", type = SQLType.Text)

                override val primaryKey : PrimaryKey get() = PrimaryKey(id)
                override val foreignKeys : List<ForeignKey> get() = listOf(
                    ForeignKey(sourceColumn = countryId,
                        destinationTable = countryTable,
                        destinationColumn = countryTable.id))
            }

            override val indices : List<Index> get() = listOf(
                Index(table = countryTable, column = countryTable.name, unique = true),
                Index(table = cityTable, column = cityTable.name),
                Index(table = cityTable, column = cityTable.countryId))
        }

        val ddl=(schema.tables.map { it.toCreateTableSQL() }
            .plus(schema.indices.map { it.toCreateIndexSQL() }))
            .joinToString(separator = "\n")

        assertEquals("create table if not exists `Country` (`id` text not null, `name` text, primary key (`id`))\n"+
            "create table if not exists `City` (`id` text not null, `countryId` text not null, `name` text, "+
                "primary key (`id`), foreign key (`countryId`) references `Country`(`id`))\n"+
            "create unique index if not exists `index_Country_name` on `Country`(`name`)\n"+
            "create  index if not exists `index_City_name` on `City`(`name`)\n"+
            "create  index if not exists `index_City_countryId` on `City`(`countryId`)", ddl)
    }
}
