package ro.andob.outofroom.sample.database.latest_sqlite

import android.annotation.SuppressLint
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteOpenHelper
import ro.andob.outofroom.sample.App
import ro.andob.outofroom.sample.database.SampleDatabaseSchema

@SuppressLint("StaticFieldLeak")
object LatestSQLiteOpenHelper : SQLiteOpenHelper
(
    /*context*/ App.context,
    /*name*/ "notes_latestsqlite.db",
    /*factory*/ null,
    /*version*/ 1
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
}
