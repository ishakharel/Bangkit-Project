package com.ecoloops.ecoloopsapp.utils

import android.app.AlertDialog
import android.content.Context

fun showAlert(context: Context, message: String) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Log.d")
    builder.setMessage(message)
    builder.setPositiveButton("OK", null)
    val dialog = builder.create()
    dialog.show()
}