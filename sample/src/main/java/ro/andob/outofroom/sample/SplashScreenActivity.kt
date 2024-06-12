package ro.andob.outofroom.sample

import android.os.Bundle
import ro.andob.outofroom.sample.database.SampleDatabase
import ro.andob.outofroom.sample.database.latest_sqlite.LatestSQLiteSampleDatabase
import ro.andob.outofroom.sample.database.system_sqlite.SystemSQLiteSampleDatabase
import ro.andob.outofroom.sample.router.ActivityRouter
import ro.andob.outofroom.sample.router.DialogListItem
import ro.andob.outofroom.sample.router.ShowDialog
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        showChooseDatabaseAPIDialogThen { database ->
            SampleDatabase.instance = database
            ActivityRouter.startNoteListActivity(from = this)
            finish()
        }
    }

    private fun showChooseDatabaseAPIDialogThen(onAPIChoosed : (SampleDatabase) -> (Unit))
    {
        ShowDialog.withList(context = this,
            title = getString(R.string.choose_db_api),
            items = listOf(
                DialogListItem(text = getString(R.string.system_sqlite)) {
                    onAPIChoosed(SystemSQLiteSampleDatabase())
                },
                DialogListItem(text = getString(R.string.latest_sqlite)) {
                    onAPIChoosed(LatestSQLiteSampleDatabase())
                }))
    }
}
