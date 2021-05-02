package ro.andob.outofroom.sample.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.setOnTextChangedListener(listener : (String) -> (Unit))
{
    addTextChangedListener(object : TextWatcher
    {
        override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}
        override fun onTextChanged(s : CharSequence?, start : Int, before : Int, count : Int) = listener(text.toString())
        override fun afterTextChanged(s : Editable?) {}
    })
}
