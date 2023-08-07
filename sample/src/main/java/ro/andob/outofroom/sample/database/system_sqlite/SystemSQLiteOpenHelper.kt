package ro.andob.outofroom.sample.database.system_sqlite

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ro.andob.outofroom.sample.App
import ro.andob.outofroom.sample.database.SampleDatabaseSchema

object SystemSQLiteOpenHelper : SQLiteOpenHelper
(
    /*context*/ App.context,
    /*name*/ "notes_systemsqlite.db",
    /*factory*/ null,
    /*version*/ 1,
    /*onCorruption*/ { throw Error("DETECTED A CORRUPT DATABASE FILE") }
)
{
    override fun onCreate(db : SQLiteDatabase)
    {
        for (table in SampleDatabaseSchema.tables)
            db.execSQL(table.toCreateTableSQL())

        for (index in SampleDatabaseSchema.indices)
            db.execSQL(index.toCreateIndexSQL())
    }

    override fun onConfigure(db : SQLiteDatabase)
    {
        super.onConfigure(db)
        db.enableWriteAheadLogging()
        db.setForeignKeyConstraintsEnabled(false)
    }

    override fun onUpgrade(db : SQLiteDatabase, oldVersion : Int, newVersion : Int)
    {
        //for the sake of simplicity, we won't implement migrations
        //you can use a migration library such as Flyway
    }

    //note to self : to close and delete thread safely, use it like this:
    //fun closeAnd(more : () -> (Unit)) = synchronized(this) { super.close(); more() }
    //openHelper.closeAnd { databaseFile.delete() }
}
