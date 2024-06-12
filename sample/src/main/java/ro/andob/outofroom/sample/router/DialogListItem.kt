package ro.andob.outofroom.sample.router

class DialogListItem
(
    val text : String?,
    val onClick : (() -> (Unit))? = null
)
{
    override fun toString() = text?:""
}
