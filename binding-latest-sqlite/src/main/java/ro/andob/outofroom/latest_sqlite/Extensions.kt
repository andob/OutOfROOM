package ro.andob.outofroom.latest_sqlite

import io.requery.android.database.sqlite.SQLiteOpenHelper
import ro.andob.outofroom.EntityManager
import ro.andob.outofroom.LatestSQLiteDBWrapper

fun SQLiteOpenHelper.toEntityManager() = EntityManager(LatestSQLiteDBWrapper { writableDatabase })
