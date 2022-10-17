package ro.andob.outofroom.sample

import android.annotation.SuppressLint
import android.app.Application
import ro.andreidobrescu.declarativeadapterkt.listeners.CellViewGlobalEvents
import ro.andreidobrescu.viewbinding_compat.ReflectiveViewBindingFieldSetter

class App : Application()
{
    companion object
    {
        @SuppressLint("StaticFieldLeak")
        lateinit var context : App
    }

    override fun onCreate()
    {
        super.onCreate()

        context=this

        CellViewGlobalEvents.setOnCellViewInflatedListener { cellView ->
            ReflectiveViewBindingFieldSetter.setup(cellView)
        }
    }
}
