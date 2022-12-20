package com.app.entity.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.app.entity.R

class DialogFragmentImp(
    private val yes: String, private val no: String, private val
    onAlertDialogItemInterface: OnAlertDialogItemInterface
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.order_confirmation))
            .setPositiveButton(yes) { _, _ ->
                onAlertDialogItemInterface.onPositiveButtonClick()
            }
            .setNegativeButton(no) { _, _ ->
                onAlertDialogItemInterface.onNegativeButtonClick()
            }
            .create()

    companion object {
        const val TAG = "DialogFragmentImp"
    }
}