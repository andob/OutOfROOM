package ro.andob.outofroom.sample.database.latest_sqlite

import ro.andob.outofroom.sample.database.SampleDatabase
import ro.andob.outofroom.sample.database.SampleDatabaseSchema
import ro.andob.outofroom.sample.database.dao.NoteDao
import ro.andob.outofroom.latest_sqlite.toEntityManager

class LatestSQLiteSampleDatabase : SampleDatabase
{
    private val entityManager = LatestSQLiteOpenHelper.toEntityManager()
    private val schema get() = SampleDatabaseSchema

    override fun noteDao() = NoteDao(entityManager, schema)
}
