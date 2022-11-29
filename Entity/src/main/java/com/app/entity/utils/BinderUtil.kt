package com.app.entity.utils

import androidx.databinding.BindingAdapter
import com.app.entity.R
import com.google.android.material.textfield.TextInputLayout

object BinderUtil {
    @JvmStatic
    @BindingAdapter("entity:errorText")
    fun setError(textInputLayout: TextInputLayout, error: Int?) {
        if (error == R.string.empty) {
            textInputLayout.error = null
        } else {
            textInputLayout.error = textInputLayout.context.getString(error!!)
            textInputLayout.requestFocus()
        }
    }

//    @JvmStatic
//    @BindingAdapter("app:errorText")
//    fun setError(checkBox: CheckBox, error: Int?) {
//        if (error == R.string.empty) {
//            checkBox.error = null
//        } else {
//            checkBox.error = checkBox.context.getString(error!!)
//            checkBox.requestFocus()
//        }
//    }

}
