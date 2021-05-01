package ro.andob.outofroom

import java.io.Closeable

interface ICursor : Closeable
{
    fun getColumnIndexOrThrow(name : String) : Int

    fun getString(index : Int) : String?
    fun getInt(index : Int) : Int
    fun getLong(index : Int) : Long
    fun getFloat(index : Int) : Float
    fun getDouble(index : Int) : Double

    fun moveToFirst() : Boolean
    fun moveToNext() : Boolean
}
