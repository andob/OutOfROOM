package ro.andob.outofroom.sample.router

class DialogListItem
(
    private val text : String?,
    val onClickListener : (() -> (Unit))? = null
)
{
    override fun toString() = text?:""
}
