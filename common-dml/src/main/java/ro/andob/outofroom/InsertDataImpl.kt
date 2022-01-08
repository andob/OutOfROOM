package ro.andob.outofroom

import ro.andob.outofroom.optional.toLong

class InsertDataImpl
(
    private val statement : IStatement,
    private val columns : List<Column>,
) : InsertData
{
    private fun index(column : Column) : Int
    {
        val zeroBasedIndex=columns.indexOf(column)
        if (zeroBasedIndex<0||zeroBasedIndex>=columns.size)
            throw RuntimeException("Invalid index for column: $column")

        return zeroBasedIndex+1
    }

    override fun hasKey(column : Column) : Boolean
    {
        val zeroBasedIndex=columns.indexOf(column)
        return zeroBasedIndex>=0&&zeroBasedIndex<columns.size
    }

    override fun putString(column : Column, value : String?)
    {
        if (value!=null)
            statement.bindString(index(column), value)
        else statement.bindNull(index(column))
    }

    override fun putInt(column : Column, value : Int?)
    {
        if (value!=null)
            statement.bindLong(index(column), value.toLong())
        else statement.bindNull(index(column))
    }

    override fun putLong(column : Column, value : Long?)
    {
        if (value!=null)
            statement.bindLong(index(column), value)
        else statement.bindNull(index(column))
    }

    override fun putFloat(column : Column, value : Float?)
    {
        if (value!=null)
            statement.bindDouble(index(column), value.toDouble())
        else statement.bindNull(index(column))
    }

    override fun putDouble(column : Column, value : Double?)
    {
        if (value!=null)
            statement.bindDouble(index(column), value)
        else statement.bindNull(index(column))
    }

    override fun putBoolean(column : Column, value : Boolean?)
    {
        statement.bindLong(index(column), (value?:false).toLong())
    }
}
