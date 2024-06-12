package ro.andob.outofroom.sample.note.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ro.andob.outofroom.sample.R
import ro.andob.outofroom.sample.database.SampleDatabase
import ro.andob.outofroom.sample.databinding.ActivityNoteListBinding
import ro.andob.outofroom.sample.model.Note
import ro.andob.outofroom.sample.model.NoteFilter
import ro.andob.outofroom.sample.model.StringId
import ro.andob.outofroom.sample.router.ShowDialog
import ro.andob.outofroom.sample.view.setOnTextChangedListener
import ro.andreidobrescu.declarativeadapterkt.SimpleDeclarativeAdapter
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding
import ro.andreidobrescu.viewbinding_compat.ReflectiveViewBindingFieldSetter

class NoteListActivity : AppCompatActivity()
{
    @AutoViewBinding
    lateinit var binding : ActivityNoteListBinding

    private val adapter = SimpleDeclarativeAdapter(::NoteCellView)

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        ReflectiveViewBindingFieldSetter.setup(this)

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

        val database = SampleDatabase.instance

        //for the sake of simplicity, we will query the database on the UI thread
        adapter.setItems(database.noteDao().getAll(NoteFilter()))

        binding.searchEditText.setOnTextChangedListener { search ->
            adapter.setItems(database.noteDao().getAll(NoteFilter(search)))
        }

        binding.addFloatingActionButton.setOnClickListener {
            showInputNoteForAddDialog { note ->
                database.noteDao().insert(note)
                adapter.setItems(database.noteDao().getAll(NoteFilter()))
            }
        }
    }

    private fun showInputNoteForAddDialog(onNoteInputted : (Note) -> Unit)
    {
        ShowDialog.withInput(context = this,
            title = getString(R.string.add_note),
            hint = getString(R.string.message),
            okAction = { noteText ->
                onNoteInputted(Note(
                    id = StringId.newRandomUUID(),
                    title = StringId.newRandomUUID<Any>().toString(),
                    message = noteText,
                ))
            })
    }

    override fun onResume()
    {
        super.onResume()

        //for the sake of simplicity, this will be called when the user returns from NoteDetailsActivity to NoteListActivity
        adapter.setItems(SampleDatabase.instance.noteDao().getAll(NoteFilter()))
    }
}
