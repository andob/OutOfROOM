package ro.andob.outofroom.sample.router

import android.text.InputType
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputLayout
import ro.andob.outofroom.sample.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

object ShowDialog
{
    @SuppressLint("CheckResult")
    fun withList
    (
        context : AppCompatActivity,
        title : String,
        items : List<DialogListItem>,
    )
    {
        val itemsAsStrings = items.map { it.text }
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, itemsAsStrings)

        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setAdapter(adapter) { _, index ->
                if (index >= 0 && index < items.size)
                    items[index].onClick?.invoke()
            }
            .show()

        dialog.dismissOnLifecycleDestroyEvent(context)
    }

    fun withTwoButtons
    (
        context : AppCompatActivity,
        title : String,
        message : String? = null,
        yesButtonText : String = context.getString(R.string.yes),
        yesAction : (() -> Unit)?,
        noButtonText : String = context.getString(R.string.no),
        noAction : (() -> Unit)? = null,
    )
    {
        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(yesButtonText) { _,_ -> yesAction?.invoke() }
            .setNegativeButton(noButtonText) { _,_ -> noAction?.invoke() }
            .setOnCancelListener { noAction?.invoke() }
            .show()

        dialog.dismissOnLifecycleDestroyEvent(context)
    }

    @SuppressLint("CheckResult")
    fun withInput
    (
        context : AppCompatActivity,
        inputType : Int = InputType.TYPE_CLASS_TEXT,
        title : String,
        hint : String,
        contents : String = "",
        okAction : ((String) -> Unit)? = null,
        cancelAction : (() -> Unit)? = null,
    )
    {
        fun createLayout(editText : EditText) : ViewGroup
        {
            val textInputLayout = TextInputLayout(context)
            editText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            editText.setText(contents)
            textInputLayout.addView(editText)

            val topMargin = context.resources.getDimensionPixelSize(R.dimen.dialog_input_top_margin)
            val lateralMargin = context.resources.getDimensionPixelSize(R.dimen.dialog_input_lateral_margin)

            val frameLayout = FrameLayout(context)
            textInputLayout.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
                .also { it.topMargin = topMargin }
                .also { it.marginStart = lateralMargin }
                .also { it.marginEnd = lateralMargin }
            frameLayout.addView(textInputLayout)

            return frameLayout
        }

        fun openKeyboard(editText : EditText)
        {
            Handler(Looper.getMainLooper()).post {
                editText.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                }, 100)
            }
        }

        fun closeKeyboard()
        {
            Handler(Looper.getMainLooper()).postDelayed({
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val windowToken = context.window.decorView.rootView.windowToken
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            }, 100)
        }

        fun setupEnterKey(editText : EditText, dialog : AlertDialog)
        {
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    if (positiveButton.isEnabled)
                    {
                        positiveButton.performClick()
                        dialog.dismiss()
                    }
                }

                return@setOnEditorActionListener false
            }
        }

        fun setupPositiveButtonEnabled(editText : EditText, dialog : AlertDialog)
        {
            Handler(Looper.getMainLooper()).post {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
            }

            editText.addTextChangedListener(object : TextWatcher
            {
                override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}
                override fun onTextChanged(s : CharSequence?, start : Int, before : Int, count : Int) {}

                override fun afterTextChanged(s : Editable?)
                {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled =
                        !editText.text?.toString().isNullOrBlank()
                }
            })
        }

        val editText = EditText(context)
        editText.inputType = inputType
        editText.hint = hint

        val okButtonCallback = DialogInterface.OnClickListener { _,_ ->
            editText.text?.toString()?.ifBlank { null }?.let { text ->
                okAction?.invoke(text)
            }
            closeKeyboard()
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(createLayout(editText))
            .setNegativeButton(context.getString(R.string.cancel)) { _,_ -> cancelAction?.invoke() }
            .setPositiveButton(context.getString(R.string.ok), okButtonCallback)
            .setOnDismissListener { closeKeyboard() }
            .create()

        setupPositiveButtonEnabled(editText, dialog)

        setupEnterKey(editText, dialog)

        dialog.show()

        openKeyboard(editText)

        dialog.dismissOnLifecycleDestroyEvent(context)
    }

    private fun Dialog.dismissOnLifecycleDestroyEvent(context : AppCompatActivity)
    {
        val dialog = this
        context.lifecycle.addObserver(object : LifecycleEventObserver
        {
            override fun onStateChanged(source : LifecycleOwner, event : Lifecycle.Event)
            {
                if (event == Lifecycle.Event.ON_DESTROY && dialog.isShowing)
                {
                    dialog.dismiss()
                }
            }
        })
    }
}
