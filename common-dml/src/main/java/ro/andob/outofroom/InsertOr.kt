package ro.andob.outofroom

enum class InsertOr
{
    //todo care e diferenta dintre toate?
    Replace, Abort, Fail, Ignore, Rollback;

    override fun toString() : String = when(this)
    {
        Replace -> "replace"
        Abort -> "abort"
        Fail -> "fail"
        Ignore -> "ignore"
        Rollback -> "rollback"
    }
}
