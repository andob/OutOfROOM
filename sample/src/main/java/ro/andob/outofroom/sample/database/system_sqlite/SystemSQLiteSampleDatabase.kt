package ro.andob.outofroom.sample.database.system_sqlite

import ro.andob.outofroom.sample.database.SampleDatabase
import ro.andob.outofroom.sample.database.SampleDatabaseSchema
import ro.andob.outofroom.sample.database.dao.NoteDao
import ro.andob.outofroom.system_sqlite.toEntityManager

class SystemSQLiteSampleDatabase : SampleDatabase
{
    private val entityManager = SystemSQLiteOpenHelper.toEntityManager()
    private val schema get() = SampleDatabaseSchema

    override fun noteDao() = NoteDao(entityManager, schema)
}
