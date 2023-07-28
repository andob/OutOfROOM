package ro.andob.outofroom

interface ICursor
{
    fun getColumnIndexOrThrow(name : String) : Int

    fun getString(index : Int) : String?
    fun getInt(index : Int) : Int?
    fun getLong(index : Int) : Long?
    fun getFloat(index : Int) : Float?
    fun getDouble(index : Int) : Double?
    fun getBytes(index : Int) : ByteArray?

    fun moveToFirst() : Boolean
    fun moveToNext() : Boolean

    fun close()
}
