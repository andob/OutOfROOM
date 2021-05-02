package ro.andob.outofroom.sample.note.list

import android.content.Context
import android.util.AttributeSet
import ro.andob.outofroom.sample.R
import ro.andob.outofroom.sample.databinding.CellNoteBinding
import ro.andob.outofroom.sample.model.Note
import ro.andob.outofroom.sample.router.ActivityRouter
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding

class NoteCellView : CellView<Note>
{
    @AutoViewBinding
    lateinit var binding : CellNoteBinding
    override fun layout() = R.layout.cell_note

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @ModelBinder
    fun setNote(note : Note)
    {
        binding.titleLabel.text=note.title
        binding.messageLabel.text=note.message

        binding.cellContentContainer.setOnClickListener {
            ActivityRouter.startNoteDetailsActivity(from = it.context, noteId = note.id)
        }
    }
}
