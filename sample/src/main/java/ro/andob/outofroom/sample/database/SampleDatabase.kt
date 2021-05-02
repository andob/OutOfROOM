package ro.andob.outofroom.sample.database

import ro.andob.outofroom.sample.database.dao.NoteDao

interface SampleDatabase
{
    fun noteDao() : NoteDao
}
