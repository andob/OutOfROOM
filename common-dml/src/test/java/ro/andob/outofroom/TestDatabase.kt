package ro.andob.outofroom

import java.io.File
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ro.andob.outofroom.dao.NoteDao

object TestDatabase
{
    private val connectionProvider by lazy {
        val workingDirectory = File(System.getProperty("user.dir"))
        val databaseFile = File(workingDirectory, "test.db")

        val hikariDataSource = HikariDataSource(HikariConfig().also { config ->
            config.jdbcUrl = "jdbc:sqlite:${databaseFile.absolutePath}"
        })

        hikariDataSource.connection.let { connection ->
            (schema.tables.map { table -> table.toCreateTableSQL() })
                .plus(schema.indices.map { index -> index.toCreateIndexSQL() })
                .forEach { sql -> connection.prepareStatement(sql).execute() }
        }

        return@lazy { hikariDataSource.connection!! }
    }

    private val entityManager = EntityManagerBuilder(connectionProvider).build()
    private val schema get() = TestDatabaseSchema

    val noteDao = NoteDao(entityManager, schema)
}
