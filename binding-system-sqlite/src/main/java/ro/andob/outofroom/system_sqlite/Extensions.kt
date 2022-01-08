package ro.andob.outofroom.system_sqlite

import android.database.sqlite.SQLiteOpenHelper

fun SQLiteOpenHelper.toEntityManager() = EntityManagerBuilder(this).build()
