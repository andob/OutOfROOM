package ro.andob.outofroom.system_sqlite

import android.database.sqlite.SQLiteOpenHelper
import ro.andob.outofroom.EntityManager
import ro.andob.outofroom.QueryArgumentConverter
import ro.andob.outofroom.SystemSQLiteDBWrapper

class EntityManagerBuilder
(
    private val sqLiteOpenHelper : SQLiteOpenHelper
)
{
    private var queryArgumentConverter : QueryArgumentConverter = QueryArgumentConverter { a -> a }
    fun queryArgumentConverter(value : QueryArgumentConverter) = also { queryArgumentConverter=value }

    fun build() = EntityManager(
        database = SystemSQLiteDBWrapper { sqLiteOpenHelper.writableDatabase },
        queryArgumentConverter = queryArgumentConverter)
}
