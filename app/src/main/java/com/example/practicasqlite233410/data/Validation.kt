package com.example.practicasqlite233410.data
import android.content.Context
import android.widget.Toast
class Validation(private val context: Context)
{

    fun validateTitle(title: String): Boolean {
        if (title.isBlank()) {
            showToast("Error: Title can not be blank.")
            return false
        }
        if (title.trim() == "") {
            showToast("Error: Title can not be only spaces.")
            return false
        }
        return true
    }

    fun validateDescription(description: String): Boolean {
        if (description.isBlank()) {
            showToast("Error: Description can not be blank.")
            return false
        }
        if (description.trim() == "") {
            showToast("Error: Description can not be only spaces.")
            return false
        }
        return true
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
