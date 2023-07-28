package ro.andob.outofroom

interface IStatement
{
    fun bindString(index : Int, value : String)
    fun bindLong(index : Int, value : Long)
    fun bindDouble(index : Int, value : Double)
    fun bindBytes(index : Int, value : ByteArray)
    fun bindNull(index : Int)

    fun executeInsert()

    fun close()
}
