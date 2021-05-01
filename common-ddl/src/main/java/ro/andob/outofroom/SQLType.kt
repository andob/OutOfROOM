package ro.andob.outofroom;

enum class SQLType
{
    Null, Integer, Real, Text, Blob;

    override fun toString() : String = when(this)
    {
        Null -> "null"
        Integer -> "integer"
        Real -> "real"
        Text -> "text"
        Blob -> "blob"
    }
}
