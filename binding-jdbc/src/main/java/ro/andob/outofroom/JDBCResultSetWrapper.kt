package ro.andob.outofroom

import java.sql.ResultSet

class JDBCResultSetWrapper
(
    private val resultSet : ResultSet
) : ICursor
{
    override fun getColumnIndexOrThrow(name : String) : Int = resultSet.findColumn(name)

    override fun getString(index : Int) : String? = resultSet.getString(index)
    override fun getInt(index : Int) : Int = resultSet.getInt(index)
    override fun getLong(index : Int) : Long = resultSet.getLong(index)
    override fun getFloat(index : Int) : Float = resultSet.getFloat(index)
    override fun getDouble(index : Int) : Double = resultSet.getDouble(index)

    override fun moveToFirst() : Boolean = resultSet.first()
    override fun moveToNext(): Boolean = resultSet.next()

    override fun close() = resultSet.close()
}
