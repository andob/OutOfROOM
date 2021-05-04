package ro.andob.outofroom

import java.sql.ResultSet

class JDBCResultSetWrapper
(
    private val resultSet : ResultSet
) : ICursor
{
    override fun getColumnIndexOrThrow(name : String) : Int = resultSet.findColumn(name)-1

    override fun getString(index : Int) : String? = resultSet.getString(index+1)
    override fun getInt(index : Int) : Int = resultSet.getInt(index+1)
    override fun getLong(index : Int) : Long = resultSet.getLong(index+1)
    override fun getFloat(index : Int) : Float = resultSet.getFloat(index+1)
    override fun getDouble(index : Int) : Double = resultSet.getDouble(index+1)

    override fun moveToFirst() : Boolean = resultSet.first()
    override fun moveToNext() : Boolean = resultSet.next()

    override fun close() = resultSet.close()
}
