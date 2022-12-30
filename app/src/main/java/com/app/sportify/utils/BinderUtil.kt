package com.app.sportify.utils

import androidx.databinding.BindingAdapter
import com.app.sportify.R
import com.google.android.material.textfield.TextInputLayout

object BinderUtil {
    @JvmStatic
    @BindingAdapter("app:errorText")
    fun setError(textInputLayout: TextInputLayout, error: Int?) {
        if (error == R.string.empty) {
            textInputLayout.error = null
        } else {
            textInputLayout.error = textInputLayout.context.getString(error!!)
            textInputLayout.requestFocus()
        }
    }

}