package ro.andob.outofroom

import ro.andob.outofroom.dao.NoteDao
import java.sql.DriverManager

object TestDatabase
{
    private val connection by lazy {
        val connection=DriverManager.getConnection("jdbc:sqlite:file:memory?mode=memory")
        TestDatabaseSchema.tables.forEach { connection.prepareStatement(it.toCreateTableSQL()).execute() }
        TestDatabaseSchema.indices.forEach { connection.prepareStatement(it.toCreateIndexSQL()).execute() }
        return@lazy connection
    }

    private val entityManager = EntityManager(JDBCConnectionWrapper { connection })
    private val schema get() = TestDatabaseSchema

    val noteDao = NoteDao(entityManager, schema)
}
