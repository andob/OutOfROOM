package ro.andob.outofroom

enum class InsertOr
{
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
