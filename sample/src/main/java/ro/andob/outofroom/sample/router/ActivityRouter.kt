package ro.andob.outofroom.sample.router

import android.content.Context
import android.content.Intent
import ro.andob.outofroom.sample.model.Note
import ro.andob.outofroom.sample.model.StringId
import ro.andob.outofroom.sample.note.details.NoteDetailsActivityBundleBuilder
import ro.andob.outofroom.sample.note.list.NoteListActivity

object ActivityRouter
{
    fun startNoteListActivity(from : Context?)
    {
        val intent=Intent(from, NoteListActivity::class.java)
        from!!.startActivity(intent)
    }

    fun startNoteDetailsActivity(from : Context?, noteId : StringId<Note>)
    {
        NoteDetailsActivityBundleBuilder()
            .noteId(noteId)
            .startActivity(from!!)
    }
}