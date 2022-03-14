package ro.andob.outofroom.latest_sqlite

import io.requery.android.database.sqlite.SQLiteOpenHelper
import ro.andob.outofroom.EntityManager
import ro.andob.outofroom.LatestSQLiteDBWrapper
import ro.andob.outofroom.QueryArgumentConverter

class EntityManagerBuilder
(
    private val sqLiteOpenHelper : SQLiteOpenHelper
)
{
    private var queryArgumentConverter : QueryArgumentConverter = QueryArgumentConverter { a -> a }
    fun queryArgumentConverter(value : QueryArgumentConverter) = also { queryArgumentConverter=value }

    fun build() = EntityManager(
        database = LatestSQLiteDBWrapper { sqLiteOpenHelper.writableDatabase },
        queryArgumentConverter = queryArgumentConverter)
}
