package ro.andob.outofroom

import java.io.Closeable

interface IStatement : Closeable
{
    fun bindString(index : Int, value : String)
    fun bindLong(index : Int, value : Long)
    fun bindDouble(index : Int, value : Double)
    fun bindNull(index : Int)

    fun executeInsert()
}
