package ro.andob.outofroom.sample

import android.app.Activity
import android.os.Bundle
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import ro.andob.outofroom.sample.database.SampleDatabase
import ro.andob.outofroom.sample.database.latest_sqlite.LatestSQLiteSampleDatabase
import ro.andob.outofroom.sample.database.system_sqlite.SystemSQLiteSampleDatabase
import ro.andob.outofroom.sample.router.ActivityRouter
import ro.andob.outofroom.sample.router.DialogListItem
import ro.andob.outofroom.sample.router.ShowDialog

class SplashScreenActivity : Activity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        showChooseDatabaseAPIDialogThen { database ->

            stopKoin()
            startKoin{
                androidLogger()
                androidContext(App.context)
                modules(listOf(module {
                    single { database }
                }))
            }

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
