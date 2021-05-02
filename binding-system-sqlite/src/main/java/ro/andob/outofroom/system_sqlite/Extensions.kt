package ro.andob.outofroom.system_sqlite

import android.database.sqlite.SQLiteOpenHelper
import ro.andob.outofroom.EntityManager
import ro.andob.outofroom.SystemSQLiteDBWrapper

fun SQLiteOpenHelper.toEntityManager() = EntityManager(SystemSQLiteDBWrapper { writableDatabase })
