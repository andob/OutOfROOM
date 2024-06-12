package ro.andob.outofroom.sample.note.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michaelflisar.bundlebuilder.Arg
import com.michaelflisar.bundlebuilder.BundleBuilder
import ro.andob.outofroom.sample.R
import ro.andob.outofroom.sample.database.SampleDatabase
import ro.andob.outofroom.sample.databinding.ActivityNoteDetailsBinding
import ro.andob.outofroom.sample.model.Note
import ro.andob.outofroom.sample.model.StringId
import ro.andob.outofroom.sample.router.ShowDialog
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding
import ro.andreidobrescu.viewbinding_compat.ReflectiveViewBindingFieldSetter

@BundleBuilder
class NoteDetailsActivity : AppCompatActivity()
{
    @Arg
    lateinit var noteId : StringId<Note>

    @AutoViewBinding
    lateinit var binding : ActivityNoteDetailsBinding

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)
        ReflectiveViewBindingFieldSetter.setup(this)
        NoteDetailsActivityBundleBuilder.inject(intent.extras, this)

        //for the sake of simplicity, we will query the database on the UI thread
        val database = SampleDatabase.instance
        val note = database.noteDao().getById(noteId)!!
        binding.titleLabel.text = note.title
        binding.messageLabel.text = note.message

        binding.editButton.setOnClickListener {
            showInputNoteForEditDialog(note) { newNote ->
                database.noteDao().update(newNote)
                finish()
            }
        }

        binding.deleteButton.setOnClickListener {
            ShowDialog.withTwoButtons(context = this,
                title = getString(R.string.delete_this_note_question),
                yesAction = {
                    database.noteDao().delete(note)
                    finish()
                })
        }
    }

    private fun showInputNoteForEditDialog(note : Note, onNoteInputted : (Note) -> Unit)
    {
        ShowDialog.withInput(context = this,
            title = getString(R.string.edit_note),
            hint = getString(R.string.message),
            contents = note.message,
            okAction = { noteText ->
                onNoteInputted(Note(
                    id = note.id,
                    title = StringId.newRandomUUID<Any>().toString(),
                    message = noteText,
                ))
            })
    }
}
