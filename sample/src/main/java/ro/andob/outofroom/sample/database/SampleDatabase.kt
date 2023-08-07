package ro.andob.outofroom.sample.database

import ro.andob.outofroom.sample.database.dao.NoteDao
import ro.andob.outofroom.sample.database.system_sqlite.SystemSQLiteSampleDatabase

interface SampleDatabase
{
    fun noteDao() : NoteDao

    companion object
    {
        var instance : SampleDatabase = SystemSQLiteSampleDatabase()
    }
}
