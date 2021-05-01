package ro.andob.outofroom

import android.database.sqlite.SQLiteStatement

class SQLiteInsertData
(
    private val statement : SQLiteStatement,
    private val columns : List<Column>,
)
{
    private fun index(column : Column) : Int
    {
        val zeroBasedIndex=columns.indexOf(column)
        if (zeroBasedIndex<0||zeroBasedIndex>=columns.size)
            throw RuntimeException("Invalid index for column: $column")

        return zeroBasedIndex+1
    }

    fun putString(column : Column, value : String?)
    {
        if (value!=null)
            statement.bindString(index(column), value)
        else statement.bindNull(index(column))
    }

    fun putInt(column : Column, value : Int?)
    {
        if (value!=null)
            statement.bindLong(index(column), value.toLong())
        else statement.bindNull(index(column))
    }

    fun putLong(column : Column, value : Long?)
    {
        if (value!=null)
            statement.bindLong(index(column), value)
        else statement.bindNull(index(column))
    }

    fun putFloat(column : Column, value : Float?)
    {
        if (value!=null)
            statement.bindDouble(index(column), value.toDouble())
        else statement.bindNull(index(column))
    }

    fun putDouble(column : Column, value : Double?)
    {
        if (value!=null)
            statement.bindDouble(index(column), value)
        else statement.bindNull(index(column))
    }

    fun putBoolean(column : Column, value : Boolean?)
    {
        if (value!=null)
            statement.bindLong(index(column), if (value) 1 else 0)
        else statement.bindNull(index(column))
    }
}
