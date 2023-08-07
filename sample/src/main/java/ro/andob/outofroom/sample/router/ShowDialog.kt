package ro.andob.outofroom.sample.router

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import ro.andob.outofroom.sample.R

object ShowDialog
{
    fun withList(context : Context,
                 title : String,
                 items : List<DialogListItem>)
    {
        MaterialDialog(context).show {
            title(text = title)
            listItems(items = items.map { it.toString() },
                selection = { dialog, index, text ->
                    if (index>=0 && index<items.size)
                        items[index].onClickListener?.invoke()
                })
        }
    }

    fun withTwoButtons(context : Context,
                       title : String,
                       message : String? = null,
                       customContentView : View? = null,
                       yesButtonText : String = context.getString(R.string.yes),
                       yesAction : (() -> (Unit))?,
                       noButtonText : String = context.getString(R.string.no),
                       noAction : (() -> (Unit))? = null,
                       shouldCallNoActionOnDismiss : Boolean = true)
    {
        MaterialDialog(context).show {
            title(text = title)
            if (message!=null)
                message(text = message)
            if (customContentView!=null)
                customView(view = customContentView, scrollable = true)
            cancelable(cancelable)
            positiveButton(text = yesButtonText, click = { yesAction?.invoke() })
            negativeButton(text = noButtonText, click = { noAction?.invoke() })
            if (shouldCallNoActionOnDismiss)
                onCancel { noAction?.invoke() }
        }
    }

    fun withInput(context : Context,
                  inputType : Int = InputType.TYPE_CLASS_TEXT,
                  title : String,
                  hint : String,
                  contents : String = "",
                  okButtonClickedListener : (String) -> (Unit))
    {
        val okButtonCallback = { dialog : MaterialDialog ->
            okButtonClickedListener(dialog.getInputField().text.toString().trim())
        }

        val dialog = MaterialDialog(context).show {
            title(text = title)
            input(inputType = inputType, hint = hint, prefill = contents, allowEmpty = false)
            negativeButton(text = context.resources.getString(R.string.cancel))
            positiveButton(text = context.resources.getString(R.string.ok), click = okButtonCallback)
        }

        dialog.getInputField().setOnEditorActionListener { view, actionId, event ->
            if (actionId==EditorInfo.IME_ACTION_DONE)
            {
                okButtonCallback.invoke(dialog)
                dialog.dismiss()
            }

            return@setOnEditorActionListener false
        }
    }
}
