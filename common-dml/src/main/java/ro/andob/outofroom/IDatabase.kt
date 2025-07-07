package ro.andob.outofroom

interface IDatabase
{
    fun rawQuery(sql : String, args : Array<String?>) : ICursor

    fun execSQL(sql : String, args : Array<String?>)

    fun compileStatement(sql : String) : IStatement
}
