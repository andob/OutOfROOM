package ro.andob.outofroom

enum class InsertOr
{
    //todo MED sa fac sa mearga throw exception
    //todo MED care e diferenta dintre toate?
    ThrowException, Replace, Abort, Fail, Ignore, Rollback;

    override fun toString() : String = when(this)
    {
        Replace -> "replace"
        Ignore -> "ignore"
        Abort -> "abort"
        Rollback -> "rollback"
        else -> "fail"
    }
}
