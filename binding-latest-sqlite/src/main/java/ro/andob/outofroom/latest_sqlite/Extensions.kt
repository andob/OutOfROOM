package ro.andob.outofroom.latest_sqlite

import io.requery.android.database.sqlite.SQLiteOpenHelper

fun SQLiteOpenHelper.toEntityManager() = EntityManagerBuilder(this).build()
