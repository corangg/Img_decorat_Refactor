package com.app

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("hueValueFormatted")
fun setHueValue(editText: EditText, value: Int?) {
    val displayValue = (value ?: 100) - 100
    if (editText.text.toString() != displayValue.toString()) {
        editText.setText(displayValue.toString())
    }
}

@InverseBindingAdapter(attribute = "hueValueFormatted", event = "hueValueFormattedAttrChanged")
fun getHueValue(editText: EditText): Int {
    return try {
        editText.text.toString().toInt() + 100
    } catch (e: NumberFormatException) {
        100
    }
}

@BindingAdapter("hueValueFormattedAttrChanged")
fun setHueValueListener(
    editText: EditText,
    listener: InverseBindingListener?
) {
    if (listener != null) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                listener.onChange()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}